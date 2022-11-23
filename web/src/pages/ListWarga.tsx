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
  Badge,
} from "@chakra-ui/react";
import React, { useContext, useEffect } from "react";
import { FaSearch } from "react-icons/fa";
import { AuthContext } from "../context/AuthContext";

const ListWarga: React.FC = () => {
  const auth = useContext(AuthContext);

  const bankSampah = auth.data.trash_managers;
  let warga: any = [];

  bankSampah.map((bs: any) =>
    bs.users.map((user: any) => {
      user.nama_pengelola = bs.nama_pengelola;
      warga.push(user);
    })
  );

  const [listWarga, setListWarga] = React.useState(warga);

  const handleSearch = (e: any) => {
    const value = e.target.value;
    setListWarga(warga.filter((warga: any) => warga.full_name.toLowerCase().includes(value.toLowerCase())));
  };

  return (
    <Box w="100%" bgColor="var(--color-white)" borderRadius="15px" p="2rem">
      <Flex justifyContent="space-between" alignItems="center">
        <Heading fontSize="xl">Daftar Pengelola Bank Sampah</Heading>
        <InputGroup w="20rem">
          <InputLeftElement pointerEvents="none" children={<Icon as={FaSearch} color="gray.300" />} />
          <Input placeholder="Cari nama pengelola bank sampah" borderRadius="30px" onKeyUp={handleSearch} />
        </InputGroup>
      </Flex>
      <TableContainer>
        <Table variant="striped">
          <TableCaption>Lorem ipsum dolor sit, amet consectetur adipisicing elit. Possimus, doloribus.</TableCaption>
          <Thead>
            <Tr>
              <Th>Nama Warga</Th>
              <Th>Pengelola Bank Sampah</Th>
              <Th>Alamat</Th>
              <Th>Telepon</Th>
              <Th>Saldo</Th>
              <Th>Aksi</Th>
            </Tr>
          </Thead>
          <Tbody>
            {listWarga.map((warga: any, idx: number) => (
              <Tr key={`warga-${idx}`}>
                <Td>{warga.full_name}</Td>
                <Td>
                  <Badge colorScheme="red">{warga.nama_pengelola}</Badge>
                </Td>
                <Td>{warga.address}</Td>
                <Td>{warga.phone_number}</Td>
                <Td>{warga.balance}</Td>
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

export default ListWarga;
