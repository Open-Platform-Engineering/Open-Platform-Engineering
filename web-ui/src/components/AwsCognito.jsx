
import {
  CognitoUserPool,
  CookieStorage
} from "amazon-cognito-identity-js";


const AwsCognitoUserPool = (userPoolId, clientId) => {
  return new CognitoUserPool({
    UserPoolId: userPoolId,
    ClientId: clientId,
    Storage: new CookieStorage({ domain: 'localhost', secure: false, expires: 365 })
  });
};

export default AwsCognitoUserPool;