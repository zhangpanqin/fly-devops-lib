import com.mflyyou.PipelineParam

def call(Map config) {
    def pipelineParam = new PipelineParam(config.serviceName)
    serviceDeployMaster(pipelineParam)
}
