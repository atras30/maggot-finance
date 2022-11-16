import { Flex, Heading, InputGroup, InputLeftElement, Icon, Input, Box } from "@chakra-ui/react";
import React from "react";
import { FaSearch } from "react-icons/fa";

const UserList: React.FC = () => {
  return (
    <Box w="100%" h="15rem" bgColor="var(--color-white)" borderRadius="15px" p="2rem">
      <Flex justifyContent="space-between" alignItems="center">
        <Heading fontSize="xl">Daftar Pengelola Bank Sampah</Heading>
        <InputGroup w="20rem">
          <InputLeftElement pointerEvents="none" children={<Icon as={FaSearch} color="gray.300" />} />
          <Input placeholder="Cari nama pengelola bank sampah" borderRadius="30px" />
        </InputGroup>
      </Flex>
    </Box>
  );
};

export default UserList;
