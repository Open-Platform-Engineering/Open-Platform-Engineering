import {
  Card,
  Input,
  Checkbox,
  Button,
  Alert,
  Typography,
} from "@material-tailwind/react";
import React from "react";
import { useState, useEffect, useContext } from "react";
import { AccountContext } from "../AccountContext";

import { useForm } from "react-hook-form";
import { Form, Link, redirect, useNavigate } from "react-router-dom";


const SignIn = () => {
  const [isForgetPsw, setIsForgetPsw] = useState(false);
  const [signInError, setSignInError] = useState();
  const [clickedButton, setClickedButton] = useState(false);
  const {authenticate,getSession,setSession} = useContext(AccountContext);
  
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = (data) => {
    setClickedButton(true);

    authenticate(data.email, data.password).then(result => {
      setSignInError(undefined);
      // getSession().then(setSession).catch((err) => {console.log(err)});
      // navigate("/", { replace: true });
      window.location.href = '/';
    })
    .catch(err => {
      setClickedButton(false);
      setSignInError(err)
      
    })
  };

  return (
    <div className="mt-4">
      <Card color="transparent" shadow={false}>
        <Typography variant="h4" color="blue-gray">
          Sign In
        </Typography>
        <Form
          method="post"
          action="/login"
          className="mt-8 mb-2 w-80 max-w-screen-lg sm:w-96"
          onSubmit={handleSubmit(onSubmit)}
        >
          <div className="mb-4 flex flex-col gap-4">
            <div>
              <Input
                size="lg"
                label="Email"
                {...register("email", {
                  required: "Email is required",
                  pattern: {
                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i,
                    message: "Invalid email address",
                  },
                })}
              />
              {errors.email && (
                <div className="text-red-500 text-sm">
                  {errors.email.message}
                </div>
              )}
            </div>
            <div>
              <Input
                type="password"
                size="lg"
                label="Password"
                {...register("password", {
                  required: "Password is required",
                  minLength: {
                    value: 8,
                    message: "Password must be at least 8 characters",
                  },
                })}
              />
              {errors.password && (
                <p className="text-red-500 text-sm">
                  {errors.password.message}
                </p>
              )}
              {isForgetPsw && (
                <p className="text-red-500 text-sm">
                  Incorrect username or password.
                  <Link to="/forget-password-code-send">
                    <a href="#" className="font-medium text-gray-900">
                      Find the password?
                    </a>
                  </Link>
                </p>
              )}
              {
                signInError && (
                  <p className="text-red-500 text-sm">
                  {signInError.message}
                </p>
                )
              }
            </div>
          </div>

          <Button className="mt-6 " fullWidth type="submit" disabled={clickedButton}>
            Submit
          </Button>
          <Typography color="gray" className="mt-4 text-center font-normal">
            Already have not an account?{" "}
            <Link to="/sign-up">
              <a href="#" className="font-medium text-gray-900">
                Sign Up
              </a>
            </Link>
          </Typography>

          <Typography color="gray" className="mt-4 text-center font-normal">
            Forget the password?{" "}
            <Link to="/forget-password-code-send">
              <a href="#" className="font-medium text-gray-900">
                Forget Password
              </a>
            </Link>
          </Typography>
        </Form>
      </Card>
    </div>
  );
};

export default SignIn;
