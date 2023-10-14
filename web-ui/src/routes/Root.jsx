import { Outlet, useNavigation } from "react-router-dom";
import { useState, useEffect, useReducer } from "react";
import AwsCognitoUserPool from "../components/AwsCognito";
import Nav from "../components/Nav";
import Footer from "../components/Footer";
import { AwsConfigContextProvider } from "../AwsConfigContext";
import { Account, AccountContext } from "../AccountContext";
export async function loader() {
  return {};
}

export default function Root() {
  const [awsConfig, setAwsConfig] = useState({});
  const [session, setSession] = useState();
  useEffect(() => {
    const fetchData = async () => {
      await fetch("/_aws_config")
        .then((response) => response.json())
        .then((data) => {
          setAwsConfig(data);
          const userPool = AwsCognitoUserPool(
            data.UserPoolId,
            data.ClientId
          );
          const currentUser = userPool.getCurrentUser();
          if(currentUser){
            currentUser.getSession((session) => {
              console.log("sessionnnnnnn");
              setSession(session);
            });
            // console.log(currentUser);
            // 
          }
        })
        .catch((err) => console.log("Request Failed", err));
    };
    fetchData();
  }, [session]);

  return (
    <>
      <AwsConfigContextProvider value={awsConfig}>
        <Account >
          <Nav></Nav>
          <div className="flex flex-row justify-center">
            <Outlet></Outlet>
          </div>
        </Account>
        <Footer></Footer>
      </AwsConfigContextProvider>
    </>
  );
}
