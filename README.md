# aws

```groovy
withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {
  sh 'aws iam get-user'
}

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
```

