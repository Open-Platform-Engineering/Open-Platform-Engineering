import {
  Card,
  Input,
  Checkbox,
  Button,
  Alert,
  Typography,
} from "@material-tailwind/react";
import {
  CognitoUserPool,
  CognitoUserAttribute,
  CognitoUser,
} from "amazon-cognito-identity-js";
import { useState, useEffect, useContext } from "react";
import { useForm } from "react-hook-form";
import { Form, Link, redirect, useNavigate } from "react-router-dom";
import AwsCognitoUserPool from "../components/AwsCognito";
import { AccountContext } from "../AccountContext";
import AwsConfigContext from "../AwsConfigContext";

export async function action({ request, params }) {
  return redirect(`/email-validation`);
}

const SignUp = () => {
  
  const [clickedSignUpButton, setClickedSignUpButton] = useState(false);
  
  const {signUp} = useContext(AccountContext);
  const navigate = useNavigate();

  const [existsUser, setExistsUser] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = (data) => {
    setClickedSignUpButton(true);
    signUp(data.email, data.password).then((result) => {
      console.log(result);
      navigate("/email-validation/" + data.email, { replace: true });
    }).catch((err) => {
      setClickedSignUpButton(false);
      if (err.code === "UsernameExistsException") {
        setExistsUser(true);
      } else {
        navigate("/email-validation/" + data.email, { replace: true });
      }
      
    });
  };

  return (
    <div className="mt-4">
      <Card color="transparent" shadow={false}>
        <Typography variant="h4" color="blue-gray">
          Sign Up
        </Typography>
        <Typography color="gray" className="mt-1 font-normal">
          Enter your details to register.
        </Typography>

        <Form
          method="post"
          action="/sign-up"
          className="mt-8 mb-2 w-80 max-w-screen-lg sm:w-96"
          onSubmit={handleSubmit(onSubmit)}
        >
          <div className="mb-4 flex flex-col gap-4">
            <div>
              <Input
                size="lg"
                label="Name"
                {...register("name", {
                  required: "Username is required",
                  minLength: {
                    value: 3,
                    message: "Username must be at least 3 characters",
                  },
                })}
              />
              {errors.name && (
                <p className="text-red-500 text-sm">{errors.name.message}</p>
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
            </div>
          </div>
          {/* <Checkbox
            label={
              <Typography
                variant="small"
                color="gray"
                className="flex items-center font-normal"
              >
                I agree the
                <a
                  href="#"
                  className="font-medium transition-colors hover:text-gray-900"
                >
                  &nbsp;Terms and Conditions
                </a>
              </Typography>
            }
            containerProps={{ className: "-ml-2.5" }}
          /> */}
          {existsUser && (
            <Typography color="red">
              The email is exists. Would you like to sign in?
              <Link to="/sign-in">
                <a href="#" className="font-medium text-gray-900">
                  Sign In
                </a>
              </Link>
            </Typography>
          )}
          <Button className="mt-6 " fullWidth type="submit" disabled={clickedSignUpButton}>
            Sign Up
          </Button>
          <Typography color="gray" className="mt-4 text-center font-normal">
            Already have an account?{" "}
            <Link to="/sign-in">
              <a href="#sign-in" className="font-medium text-gray-900">
                Sign In
              </a>
            </Link>
          </Typography>
        </Form>
      </Card>
    </div>
  );
};

export default SignUp;
