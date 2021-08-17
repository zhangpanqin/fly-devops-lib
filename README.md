# aws

```groovy
withAWS(credentials: 'aws-iam-fly-devops', region: 'us-east-2') {
  sh 'aws iam get-user'
}
```

