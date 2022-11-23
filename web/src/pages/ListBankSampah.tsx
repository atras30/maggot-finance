import {
  Flex,
  Heading,
  InputGroup,
  InputLeftElement,
  Icon,
  Input,
  Box,
  Table,
  TableCaption,
  TableContainer,
  Tbody,
  Th,
  Thead,
  Tr,
  Td,
  Button,
} from "@chakra-ui/react";
import React, { useContext } from "react";
import { FaSearch } from "react-icons/fa";
import { AuthContext } from "../context/AuthContext";

const ListBankSampah: React.FC = () => {
  const auth = useContext(AuthContext);

  const userList = auth.data?.trash_managers;
  console.log(userList);
  return (
    <Box w="100%" bgColor="var(--color-white)" borderRadius="15px" p="2rem">
      <Flex justifyContent="space-between" alignItems="center">
        <Heading fontSize="xl">Daftar Pengelola Bank Sampah</Heading>
        <InputGroup w="20rem">
          <InputLeftElement pointerEvents="none" children={<Icon as={FaSearch} color="gray.300" />} />
          <Input placeholder="Cari nama pengelola bank sampah" borderRadius="30px" />
        </InputGroup>
      </Flex>
      <TableContainer>
        <Table variant="striped">
          <TableCaption>Lorem ipsum dolor sit, amet consectetur adipisicing elit. Possimus, doloribus.</TableCaption>
          <Thead>
            <Tr>
              <Th>Nama Pengelola</Th>
              <Th>Kontak</Th>
              <Th>Alamat</Th>
              <Th>Aksi</Th>
            </Tr>
          </Thead>
          <Tbody>
            {userList?.map((user: any, idx: number) => (
              <Tr key={`bank-sampah-${idx}`}>
                <Td>{user.nama_pengelola}</Td>
                <Td>{user.email}</Td>
                <Td>{user.tempat}</Td>
                <Td>
                  <Button colorScheme="teal">Detail</Button>
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default ListBankSampah;
