import com.mflyyou.PipelineParam

def call(Map config) {
    def pipelineParam = new PipelineParam(config.serviceName)
    echo config.toString()
    echo pipelineParam.toString()
    echo env.BRANCH_NAME
    serviceDeployMaster(pipelineParam)

}
