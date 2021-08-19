import com.mflyyou.PipelineParam

def call(Map config) {
    echo "serviceDeployPipeline.groovy echo ${env.GIT_BRANCH}"
    echo "serviceDeployPipeline.groovy echo ${env.BRANCH_NAME}"
    def pipelineParam = new PipelineParam(config.serviceName, "${env.GIT_BRANCH}")
    serviceDeployMaster(pipelineParam)
}
