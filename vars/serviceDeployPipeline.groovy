import com.mflyyou.PipelineParam

def call(Map config) {
    echo "serviceDeployPipeline.groovy echo ${env.BRANCH_NAME}"
    def pipelineParam = new PipelineParam(config.serviceName,"${env.BRANCH_NAME}")
    serviceDeployMaster(pipelineParam)
}
