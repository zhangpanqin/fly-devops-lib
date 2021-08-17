import com.mflyyou.PipelineParam

def call(Map config) {
    serviceDeployMaster(new PipelineParam(config.serviceName))
}
