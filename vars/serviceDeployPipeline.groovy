import com.mflyyou.PipelineParam

def call(Map config) {
    sh config.toString()
    if (env.BRANCH_NAME == 'master') {
        serviceDeployMaster(new PipelineParam(config.serviceName))
    }
}
