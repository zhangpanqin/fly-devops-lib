import com.mflyyou.GitHelper
import com.mflyyou.PipelineParam
import com.mflyyou.ServicePipelineHelper

/**
 * env.BRANCH_NAME,
 * env.BUILD_NUMBER,
 * env.JOB_NAME,
 * env.BUILD_URL
 */
def call(PipelineParam config) {
    def gitHelper = new GitHelper(this)
    def servicePipelineHelper = new ServicePipelineHelper(this, config.getServiceName())
    pipeline {
        agent any
//        options {
//            disableConcurrentBuilds()
////            保存构建历史
//            buildDiscarder(logRotator(daysToKeepStr: '5'))
//        }
//        environment {
//            SERVICE_NAME = "${config.getServiceName()}"
//            IMAGE_EXIST = servicePipelineHelper.isImageExisted()
//            LAST_IMAGE_EXIST = servicePipelineHelper.isLastImageExisted(gitHelper.currentBranchName())
//        }
//        parameters {
//            booleanParam(name: 'CHECK_IMAGE_AND_BUILD', defaultValue: true, description: 'If enabled, jenkins will build only if image not exists. If disabled, jenkins will build everytime.')
//            booleanParam(name: 'DEPLOY_TO_QA', defaultValue: false, description: 'Deploy to QA.')
//            booleanParam(name: 'DEPLOY_TO_UAT', defaultValue: false, description: 'Deploy to UAT.')
//        }
        stages {
            stage('echo') {
                steps {
//                    withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {
//                        def images = ecrListImages(repositoryName: "${serviceName}", filter: "imageTag=HEAD-24b17ec")
//                    }
                    withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {
                        def data = sh(returnStdout: true, script: "aws ecr describe-images --region us-east-2 --repository-name=${config.getServiceName()} --image-ids=imageTag=HEAD-24b17ec")
                        echo "${data}"
                    }
                    script {
                        withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {
                            def data = sh(returnStdout: true, script: "aws ecr describe-images --region us-east-2 --repository-name=${config.getServiceName()} --image-ids=imageTag=HEAD-24b17ec")
                            echo "${data}"
                        }
                        if (fileExists("/git-2.33.0")) {
                            echo "git-2.33.0 exist"
                        } else {
                            echo "git-2.33.0 not exist"
                        }

                        if (fileExists("./settings.gradle")) {
                            echo "settings.gradle exist"
                        } else {
                            echo "settings.gradle not exist"
                        }
                    }
                }
            }
//            stage('Build') {
//                when {
//                    expression {
//                        return IMAGE_EXIST == "false"
//                    }
//                }
//                steps {
//                    script {
//                        servicePipelineHelper.build()
//                    }
//                }
//            }
//            stage('Delete Last Image') {
//                when {
//                    expression {
//                        return params.CHECK_IMAGE_AND_BUILD && LAST_IMAGE_EXIST == "true" && IMAGE_EXIST == "false"
//                    }
//                }
//                steps {
//                    script {
//                        servicePipelineHelper.deleteLastImageFromEcr()
//                    }
//                }
//            }
//            stage('Publish Image') {
//                when {
//                    expression {
//                        return params.CHECK_IMAGE_AND_BUILD && IMAGE_EXIST == "false"
//                    }
//                }
//                steps {
//                    script {
//                        servicePipelineHelper.publishToEcr()
//                    }
//                }
//            }
//            stage('Deploy to QA') {
//                when {
//                    expression { return params.DEPLOY_TO_QA }
//                }
//                steps {
//                    script {
//                        servicePipelineHelper.deployTo(Environments.qa)
//                    }
//                }
//            }
//            stage('Deploy to UAT') {
//                when {
//                    expression { return params.DEPLOY_TO_UAT }
//                }
//                steps {
//                    script {
//                        servicePipelineHelper.deployTo(Environments.uat)
//                    }
//                }
//            }
        }
        post {
            fixed {
                script {
                    echo "fixed"
                }
            }
            failure {
                script {
                    echo "failure"
                }
            }
            always {
                script {
                    echo gitHelper.loadResourceFromLibraryToString("test.json")
                }
            }
        }
    }
}
