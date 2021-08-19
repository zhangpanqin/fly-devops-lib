import com.mflyyou.PipelineParam

def call(Map config) {
    env.GIT_BRANCH = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
    echo "serviceDeployPipeline.groovy echo ${env.GIT_BRANCH}"
    def pipelineParam = new PipelineParam(config.serviceName,"${env.GIT_BRANCH}")
    serviceDeployMaster(pipelineParam)
}
