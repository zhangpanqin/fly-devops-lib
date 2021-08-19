package com.mflyyou

class GitHelper implements Serializable {
    def script

    GitHelper(script) {
        this.script = script
    }

    String getCurrentBranchName() {
        return script.sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
    }

    String getCommitter() {
        return script.sh(returnStdout: true, script: "git show -s --pretty=%an").trim()
    }

    String getImageTag(String branchName = "master") {
        def commitNumber = this.getFullGitCommitHash()
        return "${branchName}-${commitNumber}".replaceAll("/", "-")
    }

    String getLastImageTag(String branchName = "master") {
        def commitNumber = this.getFullLastGitCommitHash()
        return "${branchName}-${commitNumber}".replaceAll("/", "-")
    }

    String getFullGitCommitHash() {
        return script.sh(returnStdout: true, script: "git rev-parse HEAD").trim()
    }

    String getFullLastGitCommitHash() {
        return script.sh(returnStdout: true, script: "git rev-parse HEAD^").trim()
    }

    String getGitCommitHash() {
        return script.sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
    }

    def loadResourceFromLibrary(String path) {
        def resourceContent = script.libraryResource(path)
        script.writeFile file: path, text: resourceContent
        script.sh "chmod +x ${path}"
    }

    String loadResourceFromLibraryToString(String path) {
        return script.libraryResource(path)
    }
}
