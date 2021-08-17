package com.mflyyou

import com.mflyyou.GitHelper
import com.mflyyou.PipelineParam
import com.mflyyou.ServicePipelineHelper

def call(PipelineParam config) {
    def servicePipelineHelper = new ServicePipelineHelper(this)
    def gitHelper = new GitHelper(this,config)
    pipeline {
        agent none
        options {
            disableConcurrentBuilds()
//            保存构建历史
            buildDiscarder(logRotator(daysToKeepStr: '5'))
        }
        environment {
            SERVICE_NAME = "${config.getServiceName()}"
            IMAGE_EXIST = "${servicePipelineHelper.isImageExisted()}"
        }
        parameters {
            booleanParam(name: 'CHECK_IMAGE_AND_BUILD', defaultValue: true, description: 'If enabled, jenkins will build only if image not exists. If disabled, jenkins will build everytime.')
            booleanParam(name: 'DEPLOY_TO_QA', defaultValue: false, description: 'Deploy to QA.')
            booleanParam(name: 'DEPLOY_TO_UAT', defaultValue: false, description: 'Deploy to UAT.')
        }
        stages {
            stage('echo 打印参数') {
                steps {
                    script {
                        sh config.toString()
                    }
                }
            }
            stage('Build') {
                when {
                    expression {
                        return !params.CHECK_IMAGE_AND_BUILD || (params.CHECK_IMAGE_AND_BUILD && IMAGE_EXIST == "false")
                    }
                }
                steps {
                    script {
                        sh config.toString()
                        servicePipelineHelper.build()
                    }
                }
            }
            stage('Publish Image') {
                when {
                    expression {
                        return !params.CHECK_IMAGE_AND_BUILD || (params.CHECK_IMAGE_AND_BUILD && IMAGE_EXIST == "false")
                    }
                }
                steps {
                    script {
                        servicePipelineHelper.publishToEcr()
                    }
                }
            }
            stage('Deploy to QA') {
                when {
                    expression { return params.DEPLOY_TO_QA }
                }
                steps {
                    script {
                        servicePipelineHelper.deployTo(Environments.qa)
                    }
                }
            }
            stage('Deploy to UAT') {
                when {
                    expression { return params.DEPLOY_TO_UAT }
                }
                steps {
                    script {
                        servicePipelineHelper.deployTo(Environments.uat)
                    }
                }
            }
        }
        post {
            fixed {
                script {
                    sh "fixed"
                }
            }
            failure {
                script {
                    sh "failure"
                }
            }
            always {
                script {
                    sh gitHelper.loadResourceFromLibraryToString("test.json")
                }
            }
        }
    }
}