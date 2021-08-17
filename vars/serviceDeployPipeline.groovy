import com.mflyyou.PipelineParam

def call(Map config) {
    if (env.BRANCH_NAME == 'master') {
        serviceDeployMaster(new PipelineParam(config.serviceName))
    }
}
