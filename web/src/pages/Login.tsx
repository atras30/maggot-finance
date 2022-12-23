import React, { useEffect, useContext, useState } from "react";
import {
  Box,
  Button,
  Center,
  FormControl,
  FormLabel,
  Heading,
  Input,
  Link,
  Text,
  VStack,
  Image,
  FormErrorMessage,
  FormErrorIcon,
} from "@chakra-ui/react";
import { SubmitHandler, useForm } from "react-hook-form";
import { loginInputs } from "../types/type";
import { authLogin } from "../services/service";
import Swal from "sweetalert2";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<loginInputs>();
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const auth = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    document.title = "Login - Magfin";
  }, []);

  useEffect(() => {
    if (auth.isLoggedIn) {
      navigate("/dashboard/ringkasan");
    }
  }, [auth]);

  const handleOnSubmit: SubmitHandler<loginInputs> = async (data) => {
    try {
      setIsLoading(true);
      const res = await authLogin(data.email, data.password);
      auth.setData({ token: res.token, userData: {} });
      navigate("/dashboard");
    } catch (error: any) {
      console.log(error.response.data.message);
      Swal.fire({
        icon: "error",
        title: "Oops...",
        text: error.response.data.message,
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Center bgColor="var(--color-primary)" h="100vh" w="100%" position="relative">
      <Link pos="absolute" top="3rem">
        <Image src="/assets/login-header-v2.png" />
      </Link>
      <Box bgColor="var(--color-light)" borderRadius="15px" overflow="hidden">
        <VStack bgColor="var(--color-primary-accent)" p="2rem" color="var(--color-light)">
          <Heading>MagFin</Heading>
          <Text>Satu Aplikasi Untuk Kebutuhan Transaksi Maggotmu!</Text>
        </VStack>
        <VStack
          as="form"
          p="2rem 3rem"
          color="var(--color-dark)"
          alignItems="flex-start"
          gap="0.5rem"
          onSubmit={handleSubmit(handleOnSubmit)}
        >
          <FormControl isInvalid={errors.email as any}>
            <FormLabel>Email</FormLabel>
            <Input
              bgColor="var(--color-white)"
              {...register("email", {
                required: "Email is required",
                pattern: {
                  value:
                    /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
                  message: "Please enter a valid email",
                },
              })}
            />
            {errors.email && (
              <FormErrorMessage>
                <FormErrorIcon />
                {errors.email.message}
              </FormErrorMessage>
            )}
          </FormControl>
          <FormControl isInvalid={errors.password as any}>
            <FormLabel>Password</FormLabel>
            <Input
              type="password"
              bgColor="var(--color-white)"
              {...register("password", {
                required: "Password is required",
              })}
            />
            {errors.password && (
              <FormErrorMessage>
                <FormErrorIcon />
                {errors.password.message}
              </FormErrorMessage>
            )}
          </FormControl>
          <Box pt="1.5rem" w="100%">
            <Button w="100%" colorScheme="blue" type="submit" isLoading={isLoading}>
              Login
            </Button>
          </Box>
          <Link color="var(--color-secondary)" href="/">
            Forgot your password?
          </Link>
        </VStack>
      </Box>
    </Center>
  );
};

export default Login;
