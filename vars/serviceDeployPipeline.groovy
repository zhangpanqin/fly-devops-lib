import com.mflyyou.PipelineParam

def call(PipelineParam config) {
    if (env.BRANCH_NAME == 'master') {
        serviceDeployMaster(config)
    }
}
