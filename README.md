## 脚本例子

[查看插件提供的 api 还有 jenkins 自带的基础 api](https://www.jenkins.io/doc/pipeline/steps/)

[sh 命令说明](https://www.jenkins.io/doc/pipeline/steps/labelled-steps/)

### 基础例子

```groovy
stage('echo') {
  steps {
    echo "${currentBranchName}"
    echo gitHelper.getCurrentBranchName()
    echo fileExists("/git-2.33.0").toString()
    echo "${fileExists("/git-2.33.0")}"
    script {
      if (fileExists("/git-2.33.0")) {
        echo "git-2.33.0 exist"
      } else {
        echo "git-2.33.0 not exist"
      }

      if (fileExists("./settings.gradle")) {
        echo "settings.gradle exist"
      } else {
        echo "settings.gradle not exist"
      }
    }
  }
}
```



### aws

```groovy
withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {
  sh 'aws iam get-user'
}

// 
boolean isImageExisted() {
  script.echo "Checking Image..."
  def imageTag = gitHelper.getImageTag(branchName)
  def exist=false;
  script.withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {
    try {
      def status = script.sh(returnStatus: true, script: "aws ecr describe-images --region us-east-2 --repository-name=${serviceName} --image-ids=imageTag=${imageTag}")
      if (status == 0) {
        script.echo "Image exists"
        exist= true
      }
      script.echo "Image not exists"
    }catch(Exception e){
      script.echo e.getMessage()
    }
  }
  return exist
}

// 
echo "${env}"
echo "${SERVICE_NAME}"
echo SERVICE_NAME
```



