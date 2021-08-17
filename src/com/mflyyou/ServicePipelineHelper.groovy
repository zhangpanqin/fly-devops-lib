package com.mflyyou

class ServicePipelineHelper implements Serializable {
    def script
    def GitHelper gitHelper
    def String branchName = script.sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()

    ServicePipelineHelper(script, serviceName) {
        this.script = script
        this.gitHelper = new GitHelper(script)
        this.serviceName = "fly-devops"
    }

    boolean isImageExisted() {
        script.echo "Checking Image..."
        script.echo "Checking Image... ${branchName}"
        def imageTag = gitHelper.getImageTag(branchName)
        def exist = false;
        script.withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {
            try {
                def status = script.sh(returnStatus: true, script: "aws ecr describe-images --region us-east-2 --repository-name=${serviceName} --image-ids=imageTag=${imageTag}")
                if (status == 0) {
                    script.echo "Image exists"
                    exist = true
                }
                script.echo "Image not exists"
            } catch (Exception e) {
                script.echo e.getMessage()
            }
        }
        return exist
    }

    def build() {
        script.echo 'Building...'
        script.sh './gradlew clean build --write-verification-metadata sha256 --info'
    }

    def publishToEcr() {
        script.echo 'Push Image to ECR...'
        script.sh "./gradlew jib"
    }

    def deployTo(Environments environment) {
        script.echo "Deploy to ${environment}..."
        script.sh helmScript
    }
}
