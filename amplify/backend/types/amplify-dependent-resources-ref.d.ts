export type AmplifyDependentResourcesAttributes = {
  "analytics": {
    "taskmaster": {
      "Id": "string",
      "Region": "string",
      "appName": "string"
    }
  },
  "api": {
    "taskmaster": {
      "GraphQLAPIEndpointOutput": "string",
      "GraphQLAPIIdOutput": "string",
      "GraphQLAPIKeyOutput": "string"
    }
  },
  "auth": {
    "taskmaster48769bd0": {
      "AppClientID": "string",
      "AppClientIDWeb": "string",
      "IdentityPoolId": "string",
      "IdentityPoolName": "string",
      "UserPoolArn": "string",
      "UserPoolId": "string",
      "UserPoolName": "string"
    }
  },
  "predictions": {
    "speechGenerator8508474ctaskmaster": {
      "language": "string",
      "region": "string",
      "voice": "string"
    },
    "translateText52add065taskmaster": {
      "region": "string",
      "sourceLang": "string",
      "targetLang": "string"
    }
  },
  "storage": {
    "taskmasterstorege": {
      "BucketName": "string",
      "Region": "string"
    }
  }
}