import {
  Flex,
  Heading,
  Input,
  Box,
  Button,
  VStack,
  FormControl,
  FormErrorIcon,
  FormErrorMessage,
  FormLabel,
  Spacer,
} from "@chakra-ui/react";
import React, { useContext } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import { AuthContext } from "../context/AuthContext";
import { addNewBankSampah } from "../services/service";

type formTypes = {
  nama: string;
  tempat: string;
  email: string;
};

const TambahBankSampah: React.FC = () => {
  const auth = useContext(AuthContext);
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<formTypes>();

  const handleOnSubmit: SubmitHandler<formTypes> = async (data: any) => {
    try {
      const res = await addNewBankSampah(data);
      console.log(res);
      Swal.fire({
        icon: "success",
        title: "Berhasil",
        text: "Pengelola bank sampah baru berhasil ditambahkan",
      }).then((res) => {
        if (res.isConfirmed) {
          navigate("/dashboard/bank-sampah");
        }
      });
    } catch (err: any) {
      Swal.fire({
        icon: "error",
        title: "Oops...",
        text: err.response.data.message,
      });
    }
  };

  return (
    <Box w="100%" bgColor="var(--color-white)" borderRadius="15px" p="2rem">
      <Flex justifyContent="space-between" alignItems="center">
        <Heading fontSize="xl">Tambah Pengelola Bank Sampah</Heading>
      </Flex>
      <VStack as="form" onSubmit={handleSubmit(handleOnSubmit)} mt="2rem" autoComplete="off">
        <FormControl isInvalid={errors.nama as any}>
          <FormLabel>Nama Pengelola</FormLabel>
          <Input
            bgColor="var(--color-white)"
            {...register("nama", {
              required: "Nama pengelola tidak boleh kosong",
            })}
          />
          {errors.nama && (
            <FormErrorMessage>
              <FormErrorIcon />
              {errors.nama.message}
            </FormErrorMessage>
          )}
        </FormControl>
        <FormControl isInvalid={errors.tempat as any}>
          <FormLabel>Tempat</FormLabel>
          <Input
            bgColor="var(--color-white)"
            {...register("tempat", {
              required: "Tempat tidak boleh kosong",
            })}
          />
          {errors.tempat && (
            <FormErrorMessage>
              <FormErrorIcon />
              {errors.tempat.message}
            </FormErrorMessage>
          )}
        </FormControl>
        <FormControl isInvalid={errors.email as any}>
          <FormLabel>E-mail</FormLabel>
          <Input
            bgColor="var(--color-white)"
            {...register("email", {
              required: "Email tidak boleh kosong",
              pattern: {
                value:
                  /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
                message: "Silahkan masukkan email yang valid",
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
        <Flex w="100%" pt="1rem">
          <Spacer />
          <Button type="submit" colorScheme="blue" size="lg" px="3rem">
            Tambah
          </Button>
        </Flex>
      </VStack>
    </Box>
  );
};

export default TambahBankSampah;
