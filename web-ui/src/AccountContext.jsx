import React, { createContext, useContext, useState, useEffect } from "react";
import axios from "axios";

const AccountContext = createContext();

const Account = ({ children }) => {
  const [session, setSession] = useState();
  const [logined, setLogined] = useState(false);

  const signOut = async () => await new Promise((resolve, reject) => {});
  const emailValidation = async (email, code) =>
    await new Promise((resolve, reject) => {
      axios
        .get(`/v1/sign/up/email/validate`, {
          params: { email: email, code: code },
        })
        // Handle the response from backend here
        .then((res) => {
          resolve(res);
        })
        // Catch errors if any
        .catch((err) => {
          reject(err);
        });
    });
  const signUp = async (email, password) =>
    await new Promise((resolve, reject) => {
      let formData = new FormData();
      formData.append("email", email);
      formData.append("password", password);

      axios({
        // Endpoint to send files
        url: "/v1/sign-up",
        method: "POST",
        headers: {
          "content-type": "application/json",
        },
        // Attaching the form data
        data: formData,
      })
        // Handle the response from backend here
        .then((res) => {
          resolve(res);
        })

        // Catch errors if any
        .catch((err) => {
          reject(err);
        });
    });
  const getSession = async () => {};

  const authenticate = async (email, password) =>
    await new Promise((resolve, reject) => {
      let formData = new FormData();
      formData.append("email", email);
      formData.append("password", password);

      axios({
        // Endpoint to send files
        url: "/v1/sign-in",
        method: "POST",
        // Attaching the form data
        data: formData,
      })
        // Handle the response from backend here
        .then((res) => {
          resolve(res);
        })

        // Catch errors if any
        .catch((err) => {
          reject(err);
        });
    });

  return (
    <AccountContext.Provider
      value={{
        authenticate,
        getSession,
        session,
        setSession,
        signOut,
        signUp,
        logined,
        emailValidation,
      }}
    >
      {children}
    </AccountContext.Provider>
  );
};

export { Account, AccountContext };
