import com.mflyyou.PipelineParam

def call(Map config) {
    sh 'git rev-parse --abbrev-ref HEAD'
    echo "serviceDeployPipeline.groovy echo ${env.GIT_BRANCH}"
    def pipelineParam = new PipelineParam(config.serviceName, "${env.GIT_BRANCH}")
    serviceDeployMaster(pipelineParam)
}
