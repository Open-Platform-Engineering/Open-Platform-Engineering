import React, { createContext, useContext, useState,useEffect } from "react";
import {
  CognitoUserPool,
  CognitoUserAttribute,
  CognitoUser,
  AuthenticationDetails,
  CookieStorage,
} from "amazon-cognito-identity-js";
import AwsCognitoUserPool from "./components/AwsCognito";
import AwsConfigContext from "./AwsConfigContext";

const AccountContext = createContext();

const Account = ({ children }) => {
  const awsConfig = useContext(AwsConfigContext);
  const [session, setSession] = useState();
  const [logined,setLogined] = useState(false);
  useEffect(() => {
    const fetchData = async () => {
      await fetch("/_aws_config")
        .then((response) => response.json())
        .then((data) => {
          const userPool = AwsCognitoUserPool(
            data.UserPoolId,
            data.ClientId
          );
          const currentUser = userPool.getCurrentUser();
          if(currentUser){
            currentUser.getSession((err, session) => {
              console.log(session);
              setLogined(true);
              setSession(session);
            });
            // console.log(currentUser);
            // 
          }
        })
        .catch((err) => console.log("Request Failed", err));
    };
    fetchData();
  }, []);


  const signOut = async () => await new Promise((resolve, reject) => {
    const userPool = AwsCognitoUserPool(
      awsConfig.UserPoolId,
      awsConfig.ClientId
    );
    const currentUser = userPool.getCurrentUser();
    if(currentUser){
      currentUser.signOut();
    }
  });
  const signUp = async (email, password) => await new Promise((resolve, reject) => {
    const userPool = AwsCognitoUserPool(
      awsConfig.UserPoolId,
      awsConfig.ClientId,
    );

    var attributeList = [];

    var dataEmail = {
      Name: "email",
      Value: email,
    };

    var attributeEmail = new CognitoUserAttribute(dataEmail);

    attributeList.push(attributeEmail);

    userPool.signUp(
      email,
      password,
      attributeList,
      null,
      function (err, result) {
        if (err) {
          reject(err)
        }else{
          resolve(result)
        }
      }
    );

  });
  const getSession = async () =>
    await new Promise((resolve, reject) => {
      const userPool = AwsCognitoUserPool(
        awsConfig.UserPoolId,
        awsConfig.ClientId

      );
      const currentUser = userPool.getCurrentUser();
      if (currentUser) {
        currentUser.getSession((err, session) => {
          if (err) {
            reject(err);
          } else {
            resolve(session);
          }
        });
      } else {
        reject();
      }
    });
  const authenticate = async (email, password) =>
    await new Promise((resolve, reject) => {
      var authenticationData = {
        Username: email,
        Password: password,
      };
      var authenticationDetails = new AuthenticationDetails(authenticationData);
      const userPool = AwsCognitoUserPool(
        awsConfig.UserPoolId,
        awsConfig.ClientId
      );
      var userData = {
        Username: email,
        Pool: userPool,
        Storage: new CookieStorage({ domain: 'localhost', secure: false, expires: 365 })
      };
      var cognitoUser = new CognitoUser(userData);
      cognitoUser.authenticateUser(authenticationDetails, {
        onSuccess: function (result) {
          resolve(result);
        },
        onFailure: function (err) {
          reject(err);
        },
        // mfaRequired: codeDeliveryDetails => reject(codeDeliveryDetails),
        // newPasswordRequired: (fields, required) => reject({ fields, required })
      });
    });

  return (
    <AccountContext.Provider value={{authenticate,getSession,session,setSession,signOut,signUp,logined}}>
      {children}
    </AccountContext.Provider>
  );
};

export { Account, AccountContext };
