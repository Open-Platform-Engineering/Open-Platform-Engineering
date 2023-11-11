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

import { useForm } from "react-hook-form";
import { Form, Link } from "react-router-dom";



const ForgetPassword = () => {

  const [stage, setStage] = useState(0);
  const [confirmationError, setConfirmationError] = useState(undefined);
  const [sendError, setSendError] = useState(undefined);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = (data) => {

    cognitoUser.confirmPassword(data.code, data.password, {
      onSuccess: function (success) {
        console.log(success);
      },
      onFailure: function (err) {
        setConfirmationError(err);
      },
    });
  };

  const sendVerificationCode = (data) => {
    
    var cognitoUser = new CognitoUser(userData);
    cognitoUser.forgotPassword({
      onSuccess: function (data) {
        // successfully initiated reset password request
        console.log("CodeDeliveryData from forgotPassword: " + data);
      },
      onFailure: function (err) {
        setSendError(err);
      },
      //Optional automatic callback
      inputVerificationCode: function (verificationData) {
        setStage(1);
      },
    });
  };

  return (
    <div className="mt-4">
      <Card color="transparent" shadow={false}>
        {stage == 0 && (
          <>
            <Typography variant="h4" color="blue-gray">
              Send a VerificationCode
            </Typography>
            <Form
              method="post"
              action="/"
              className="mt-8 mb-2 w-80 max-w-screen-lg sm:w-96"
              onSubmit={handleSubmit(sendVerificationCode)}
            >
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

                {sendError && (
                  <div className="text-red-500 text-sm">
                    {sendError.message}
                  </div>
                )}
              </div>

              <Button className="mt-6 " fullWidth type="submit">
                Send
              </Button>
            </Form>
          </>
        )}

        {stage == 1 && (
          <>
            <Typography variant="h4" color="blue-gray">
              Forget the Password
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
                    label="Code"
                    {...register("code", {
                      required: "VerificationCode is required",
                    })}
                  />
                  {errors.code && (
                    <div className="text-red-500 text-sm">
                      {errors.code.message}
                    </div>
                  )}
                </div>
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
                    label="New Password"
                    {...register("password", {
                      required: "New password is required",
                      minLength: {
                        value: 8,
                        message: "New password must be at least 8 characters",
                      },
                    })}
                  />
                  {errors.password && (
                    <p className="text-red-500 text-sm">
                      {errors.password.message}
                    </p>
                  )}
                </div>

                {confirmationError && (
                  <div className="text-red-500 text-sm">
                    {confirmationError.message}
                  </div>
                )}
              </div>

              <Button className="mt-6 " fullWidth type="submit">
                Confirm
              </Button>
              <Typography color="gray" className="mt-4 text-center font-normal">
                Already have not an account?{" "}
                <Link to="/sign-up">
                  <a href="#" className="font-medium text-gray-900">
                    Sign Up
                  </a>
                </Link>
              </Typography>
            </Form>
          </>
        )}
      </Card>
    </div>
  );
};

export default ForgetPassword;
