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
  Text,
  Badge,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import { FaSearch } from "react-icons/fa";
import { Link } from "react-router-dom";
import { getListWarung } from "../services/service";
import stc from "string-to-color";
import invert from "invert-color";

const ListWarung: React.FC = () => {
  const [listWarung, setListWarung] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");

  const fetchData = async () => {
    try {
      const res = await getListWarung();
      console.log(res.users);
      setListWarung(res.users);
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
        <Heading fontSize="xl">Daftar Warung Binaan</Heading>
        <HStack spacing="1rem">
          <InputGroup w="20rem">
            <InputLeftElement pointerEvents="none" children={<Icon as={FaSearch} color="gray.300" />} />
            <Input
              placeholder="Cari nama warung binaan"
              borderRadius="30px"
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </InputGroup>
        </HStack>
      </Flex>
      <TableContainer mt="1rem">
        <Table variant="striped">
          <TableCaption>Tabel Daftar Warung Binaan</TableCaption>
          <Thead>
            <Tr>
              <Th>Nama Warung</Th>
              <Th>Nama Pengelola</Th>
              <Th>Alamat</Th>
              <Th>Status Keanggotaan</Th>
              <Th>Aksi</Th>
            </Tr>
          </Thead>
          <Tbody>
            {!loading ? (
              listWarung
                .filter((warung: any) => warung?.shop_name.toLowerCase().includes(searchQuery.toLowerCase()))
                .map((warung: any, idx: number) => (
                  <Tr key={`bank-sampah-${idx}`}>
                    <Td>
                      <Text whiteSpace="initial">{warung.full_name}</Text>
                    </Td>
                    <Td>
                      <Badge bgColor={stc(warung.nama_pengelola)} color="white" p="5px 10px">
                        {warung.nama_pengelola}
                      </Badge>
                    </Td>
                    <Td>
                      <Text whiteSpace="initial">{warung.address}</Text>
                    </Td>
                    <Td>
                      <Badge colorScheme={warung.is_verified == 1 ? "green" : "red"} p="5px 10px">
                        {warung.is_verified == 1 ? "Aktif" : "Tidak Aktif"}
                      </Badge>
                    </Td>
                    <Td>
                      <Button colorScheme="blue" as={Link} to={`/dashboard/warung/${warung.id}`}>
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

export default ListWarung;
