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
import React from "react";
import { useForm } from "react-hook-form";
import {
  Form,
  redirect,
  Link,
  useNavigate,
  useLoaderData,
} from "react-router-dom";
import AwsCognitoUserPool from "../components/AwsCognito";

export async function loader({ params }) {
  const email = params.email;
  return { params };
}

function ResendConfirmationCode({ email }) {
  const [hadSent, setHadSent] = React.useState();
  const onChange = ({ target }) => setHadSent(target.value);

  function doResendConfirmationCode(e, email) {
    e.preventDefault();
    const userPool = AwsCognitoUserPool();
    alert(email);
    var userData = {
      Username: email,
      Pool: userPool,
    };
    var cognitoUser1 = new CognitoUser(userData);
    cognitoUser1.resendConfirmationCode(function (err, result) {
      if (err) {
        alert(err.message || JSON.stringify(err));
        return;
      }
      
    });
  }
  return (
    <Button
      size="sm"
      color={hadSent ? "gray" : "blue-gray"}
      disabled={hadSent}
      className="rounded"
      onClick={(e) => doResendConfirmationCode(e, email)}
    >
      Resend
    </Button>
  );
}

const EmailValidation = () => {

  const email = useLoaderData().params.email;
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = (data) => {
    const email = data.email;
    console.log(email);
    const userPool = AwsCognitoUserPool();
    var userData = {
      Username: email,
      Pool: userPool,
    };
    var cognitoUser = new CognitoUser(userData);
    cognitoUser.confirmRegistration(data.code, true, function (err, result) {
      if (err) {
        alert(err.message || JSON.stringify(err));
        return;
      }
    });
    // const navigate = useNavigate();
    // navigate("/sign-in", { replace: true });
  };

  return (
    <div className="flex flex-row justify-center mt-100">
      <Card color="transparent" shadow={false}>
        <Typography color="gray" className="mt-1 font-normal">
          Enter your confirmation code
        </Typography>
        <Form
          method="post"
          action="/email-validation"
          className="mt-8 mb-2 w-80 max-w-screen-lg sm:w-96"
          onSubmit={handleSubmit(onSubmit)}
        >
          <div className="mb-4 flex flex-col gap-4">
            <input
              type="hidden"
              value={email}
              name="email"
              {...register("email")}
            />
            <div className="flex flex-row gap-1">
            <Input
              size="lg"
              label="Code"
              {...register("code", {
                required: "Code is required",
              })}
            />
            <ResendConfirmationCode email={email} />
            </div>
            {errors.code && (
              <p className="text-red-500 text-sm">{errors.code.message}</p>
            )}
            <Button className="mt-4 " fullWidth type="submit">
              Confirm
            </Button>
          </div>

          <Typography color="gray" className="mt-4 text-center font-normal">
            Already have an account?{" "}
            <Link to="/sign-in">
            <a href="#" className="font-medium text-gray-900">
              Sign In
            </a>
            </Link>
          </Typography>
          
        </Form>
      </Card>
    </div>
  );
};

export default EmailValidation;
