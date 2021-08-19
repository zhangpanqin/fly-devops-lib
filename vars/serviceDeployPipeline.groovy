import com.mflyyou.PipelineParam

def call(Map config) {
    node {
        sh 'git rev-parse --abbrev-ref HEAD > GIT_BRANCH'
        git_branch = readFile('GIT_BRANCH').trim()
        echo git_branch

        sh 'git rev-parse HEAD > GIT_COMMIT'
        git_commit = readFile('GIT_COMMIT').trim()
        echo git_commit
    }
    echo "serviceDeployPipeline.groovy echo ${env.GIT_BRANCH}"
    def pipelineParam = new PipelineParam(config.serviceName, "${env.GIT_BRANCH}")
    serviceDeployMaster(pipelineParam)
}
