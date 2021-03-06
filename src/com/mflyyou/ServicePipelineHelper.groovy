package com.mflyyou

class ServicePipelineHelper implements Serializable {
    def script
    def GitHelper gitHelper
    def String serviceName

    ServicePipelineHelper(script, serviceName) {
        this.script = script
        this.gitHelper = new GitHelper(script)
        this.serviceName = serviceName
    }

    boolean isImageExisted(String branchName) {
        if (Objects.isNull(branchName) || "".equals(branchName)) {
            branchName = "master"
        }
        def imageTag = gitHelper.getImageTag(branchName)
        def exist = false;
        script.withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {
            def status = script.sh(returnStatus: true, script: "aws ecr describe-images --region us-east-2 --repository-name=${serviceName} --image-ids=imageTag=${imageTag}")
            if (status == 0) {
                script.echo "Image exists"
                exist = true
            } else {
                script.echo "Image not exists"
            }
        }
        return exist
    }

    boolean isLastImageExisted(String branchName) {
        if (Objects.isNull(branchName) || "".equals(branchName)) {
            branchName = "master"
        }
        def imageTag = gitHelper.getLastImageTag(branchName)
        def exist = false;
        script.withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {
            def status = script.sh(returnStatus: true, script: "aws ecr describe-images --region us-east-2 --repository-name=${serviceName} --image-ids=imageTag=${imageTag}")
            if (status == 0) {
                script.echo "Last Image exists"
                exist = true
            } else {
                script.echo "Last Image not exists"
            }
        }
        return exist
    }

    def build() {
        script.echo 'Building...'
        script.sh './gradlew clean build --write-verification-metadata sha256 --info'
    }

    def deleteLastImageFromEcr() {
        // 因为 ecr 免费空间时 500m, 所以每次提交之后,先删除上一个镜像
        script.echo 'Delete Image from ECR...'
        def lastImageTag = gitHelper.getLastImageTag(gitHelper.getCurrentBranchName())
        script.echo "lastImageTag is ${lastImageTag}"
        script.withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {
            script.sh """
                        aws ecr batch-delete-image \
                            --repository-name ${serviceName} \
                            --image-ids imageTag=${lastImageTag} imageTag=latest
                      """
        }
    }

    def publishToEcr() {
        def currentImageTag = gitHelper.getImageTag(gitHelper.getCurrentBranchName())
        script.echo "currentImageTag is ${currentImageTag}"
        script.withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {
            script.echo 'Push Image to ECR...'
            script.sh "./gradlew jib -Djib.to.tags=${currentImageTag}"
        }
    }

    def deployTo(Environments environment, String timeout = "5m0s") {
        script.echo "Deploy to ${environment}..."

        script.withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {

        }
        def helmScript = """aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 626246113265.dkr.ecr.us-east-2.amazonaws.com \
             helm upgrade ${serviceName}-${environment}  \
             --install --wait --namespace fly-devops \
             --timeout ${timeout} \
             --set nameOverride=${serviceName}
             --set image.tag=${gitHelper.getImageTag(branchName)} """
//             --set image.credentials.password=$(aws ecr get-login-password) ./
    }
}
