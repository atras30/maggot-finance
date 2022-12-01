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
  HStack,
  Spinner,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import { FaSearch } from "react-icons/fa";
import { Link } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { getDashboardData } from "../services/service";

const ListBankSampah: React.FC = () => {
  const auth = useContext(AuthContext);
  const [userList, setUserList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");

  const fetchData = async () => {
    try {
      const res = await getDashboardData(window.sessionStorage.getItem("token") || "");
      setUserList(res.trash_managers.trash_managers);
    } catch (error: any) {
      console.log(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <Box w="100%" bgColor="var(--color-white)" borderRadius="15px" p="2rem">
      <Flex justifyContent="space-between" alignItems="center">
        <Heading fontSize="xl">Daftar Pengelola Bank Sampah</Heading>
        <HStack spacing="1rem">
          <InputGroup w="20rem">
            <InputLeftElement pointerEvents="none" children={<Icon as={FaSearch} color="gray.300" />} />
            <Input
              placeholder="Cari nama pengelola bank sampah"
              borderRadius="30px"
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </InputGroup>
          <Link to="/dashboard/bank-sampah/tambah">
            <Button colorScheme="blue">+ Pengelola</Button>
          </Link>
        </HStack>
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
            {!loading ? (
              userList
                .filter((user: any) => user?.nama_pengelola.toLowerCase().includes(searchQuery.toLowerCase()))
                .map((user: any, idx: number) => (
                  <Tr key={`bank-sampah-${idx}`}>
                    <Td>{user.nama_pengelola}</Td>
                    <Td>{user.email}</Td>
                    <Td>{user.tempat}</Td>
                    <Td>
                      <Button colorScheme="blue" as={Link} to={`/dashboard/bank-sampah/${user.id}`}>
                        Detail
                      </Button>
                    </Td>
                  </Tr>
                ))
            ) : (
              <Tr>
                <Td colSpan={5} textAlign="center">
                  <Spinner emptyColor="gray.200" color="blue.500" />
                </Td>
              </Tr>
            )}
          </Tbody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default ListBankSampah;
