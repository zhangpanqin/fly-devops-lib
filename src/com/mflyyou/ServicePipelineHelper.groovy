package com.mflyyou

class ServicePipelineHelper implements Serializable{
    def script

    ServicePipelineHelper(script) {
        this.script = script
    }

    def isImageExisted() {
        script.echo "Checking Image..."
        def imageTag = gitHelper.getImageTag(branchName)
        def status = script.sh(returnStatus: true, script: "aws ecr describe-images --region us-east-2 --repository-name=${serviceName} --image-ids=imageTag=${imageTag}")
        if (status == 0) {
            script.echo "Image exists"
            return true
        }
        script.echo "Image not exists"
        return false
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
