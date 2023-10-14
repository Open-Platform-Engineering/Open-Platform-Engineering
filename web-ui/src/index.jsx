import * as React from "react";
import { useState, createContext, useContext } from "react";

import * as ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { ThemeProvider } from "@material-tailwind/react";
import "./index.css";
import SignUp, { action as signUpAction } from "./pages/SignUp";
import EmailValidation from "./pages/EmailValidation";
import Root from "./routes/Root";
import SignIn from "./pages/SignIn";
import ForgetPassword from "./pages/ForgetPassword";
import { loader as emailValidationLoader } from "./pages/EmailValidation";
import { loader as rootLoader } from "./routes/Root";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    loader: rootLoader,
    children: [
      {
        path: "sign-up",
        element: <SignUp />,
        action: signUpAction,
      },
      {
        path: "sign-in",
        element: <SignIn />,
      },
      {
        path: "forget-password",
        element: <ForgetPassword/>
      },
    ],
  },
  {
    path: "email-validation/:email",
    element: <EmailValidation />,
    loader: emailValidationLoader,
  },
]);



ReactDOM.createRoot(document.getElementById("app")).render(
  <React.StrictMode>
    <ThemeProvider>
      <RouterProvider router={router} />
    </ThemeProvider>
  </React.StrictMode>
);
