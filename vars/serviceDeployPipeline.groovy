import com.mflyyou.PipelineParam

def call(Map config) {
    echo "serviceDeployPipeline.groovy echo ${env.GIT_BRANCH}"
    def pipelineParam = new PipelineParam(config.serviceName, "${env.GIT_BRANCH}")
    serviceDeployMaster(pipelineParam)
}
