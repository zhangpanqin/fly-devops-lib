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
        options {
            disableConcurrentBuilds()
            // 保存构建历史
            buildDiscarder(logRotator(daysToKeepStr: '5'))
        }
        environment {
            IMAGE_EXIST = servicePipelineHelper.isImageExisted(gitHelper.currentBranchName())
            LAST_IMAGE_EXIST = servicePipelineHelper.isLastImageExisted(gitHelper.currentBranchName())
        }
        parameters {
            booleanParam(name: 'CHECK_IMAGE_AND_BUILD', defaultValue: true, description: 'If enabled, jenkins will build only if image not exists. If disabled, jenkins will build everytime.')
            booleanParam(name: 'DEPLOY_TO_QA', defaultValue: false, description: 'Deploy to QA.')
            booleanParam(name: 'DEPLOY_TO_UAT', defaultValue: false, description: 'Deploy to UAT.')
        }
        stages {
            stage('echo env') {
                steps {
                    echo "IMAGE EXIST ${env.MAGE_EXIST}"
                    echo "LAST IMAGE EXIST ${env.LAST_IMAGE_EXIST}"
                }
            }
            stage('Build') {
                when {
                    expression {
                        return IMAGE_EXIST == "false"
                    }
                }
                steps {
                    script {
                        servicePipelineHelper.build()
                    }
                }
            }
            stage('Delete Last Image') {
                when {
                    expression {
                        return params.CHECK_IMAGE_AND_BUILD && LAST_IMAGE_EXIST == "true" && IMAGE_EXIST == "false"
                    }
                }
                steps {
                    script {
                        servicePipelineHelper.deleteLastImageFromEcr()
                    }
                }
            }
            stage('Publish Image') {
                when {
                    expression {
                        return params.CHECK_IMAGE_AND_BUILD && IMAGE_EXIST == "false"
                    }
                }
                steps {
                    script {
                        servicePipelineHelper.publishToEcr()
                    }
                }
            }
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
