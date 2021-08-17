import com.mflyyou.PipelineParam

def call(Map config) {
    def pipelineParam = new PipelineParam(config.serviceName)
    echo config
    echo pipelineParam
//    serviceDeployMaster(new PipelineParam(config.serviceName))
}
