package com.mflyyou

class GitHelper implements Serializable{
    def script

    GitHelper(script,config) {
        this.script = script
    }
    String getCommitter() {
        return script.sh(returnStdout: true, script: "git show -s --pretty=%an").trim()
    }

    String getImageTag(String branchName = "master") {
        def commitNumber = script.sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
        return "${branchName}-${commitNumber}".replaceAll("/", "-")
    }

    String getFullGitCommitHash() {
        return script.sh(returnStdout: true, script: "git rev-parse HEAD").trim()
    }

    String getGitCommitHash() {
        return script.sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
    }

    def loadResourceFromLibrary(String path) {
        try {
            def resourceContent = script.libraryResource(path)
            script.writeFile file: path, text: resourceContent
            script.sh "chmod +x ${path}"
        } catch (Exception e) {
            script.echo "load resource from library failed, path=${path}, message=${e.getMessage()}"
        }
    }

    String loadResourceFromLibraryToString(String path) {
        try {
            return script.libraryResource(path)
        } catch (Exception e) {
            return "error";
        }
    }
}
