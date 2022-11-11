import React, { useEffect } from "react";
import { Box, Button, Center, FormControl, FormLabel, Heading, Input, Link, Text, VStack, Image } from "@chakra-ui/react";

const Login = () => {
  return (
    <Center bgColor="var(--color-primary)" h="100vh" w="100%" position="relative">
      <Link pos="absolute" top="3rem">
        <Image src="/assets/login-header.png" />
      </Link>
      <Box bgColor="var(--color-light)" borderRadius="15px" overflow="hidden">
        <VStack bgColor="var(--color-primary-accent)" p="2rem" color="var(--color-light)">
          <Heading>MagFinance</Heading>
          <Text>Satu Aplikasi Untuk Kebutuhan Transaksi Maggotmu!</Text>
        </VStack>
        <VStack as="form" p="2rem 3rem" color="var(--color-dark)" alignItems="flex-start" gap="0.5rem">
          <FormControl>
            <FormLabel>Username</FormLabel>
            <Input bgColor="var(--color-white)" />
          </FormControl>
          <FormControl>
            <FormLabel>Password</FormLabel>
            <Input type="password" bgColor="var(--color-white)" />
          </FormControl>
          <Box pt="1.5rem" w="100%">
            <Button w="100%" colorScheme="teal" type="submit">
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
