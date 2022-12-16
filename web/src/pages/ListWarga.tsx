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
  Text,
  Spinner,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useRef, useState } from "react";
import { FaSearch } from "react-icons/fa";
import { Link } from "react-router-dom";
import stc from "string-to-color";
import { AuthContext } from "../context/AuthContext";
import { getListWarga } from "../services/service";

const ListWarga: React.FC = () => {
  const [listWarga, setListWarga] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");

  const fetchData = async () => {
    try {
      const res = await getListWarga();
      setListWarga(res.users);
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
        <Heading fontSize="xl">Daftar Warga Binaan</Heading>
        <InputGroup w="20rem">
          <InputLeftElement pointerEvents="none" children={<Icon as={FaSearch} color="gray.300" />} />
          <Input
            placeholder="Cari nama warga binaan"
            borderRadius="30px"
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </InputGroup>
      </Flex>
      <TableContainer mt="1rem">
        <Table variant="striped">
          <TableCaption>Tabel Daftar Warga Binaan</TableCaption>
          <Thead>
            <Tr>
              <Th>Nama Warga</Th>
              <Th>Nama Pengelola</Th>
              <Th>Telepon</Th>
              <Th>Status Keanggotaan</Th>
              <Th>Aksi</Th>
            </Tr>
          </Thead>
          <Tbody w="50%">
            {!loading ? (
              listWarga
                .filter((warga: any) => warga?.full_name.toLowerCase().includes(searchQuery.toLowerCase()))
                .map((warga: any, idx: number) => (
                  <Tr key={`warga-${idx}`}>
                    <Td>
                      <Text whiteSpace="initial">{warga.full_name}</Text>
                    </Td>
                    <Td>
                      <Badge bgColor={stc(warga.nama_pengelola)} color="white" p="5px 10px">
                        {warga.nama_pengelola}
                      </Badge>
                    </Td>
                    <Td>
                      <Text whiteSpace="initial">{warga.phone_number || "-"}</Text>
                    </Td>
                    <Td>
                      <Badge colorScheme={warga.is_verified == 1 ? "green" : "red"} p="5px 10px">
                        {warga.is_verified == 1 ? "Aktif" : "Tidak Aktif"}
                      </Badge>
                    </Td>
                    <Td>
                      <Link to={`/dashboard/warga/${warga.id}`}>
                        <Button colorScheme="blue">Detail</Button>
                      </Link>
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

export default ListWarga;
