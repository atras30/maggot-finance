import {
  Box,
  Heading,
  VStack,
  Skeleton,
  Text,
  Badge,
  Button,
  Spinner,
  Table,
  TableCaption,
  TableContainer,
  Tbody,
  Td,
  Th,
  Thead,
  Tr,
  Icon,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import { IoMdArrowBack } from "react-icons/io";
import { Link, useNavigate, useParams } from "react-router-dom";
import Swal from "sweetalert2";
import { AuthContext } from "../context/AuthContext";
import { currencyFormatter, getDashboardData } from "../services/service";

const DetailPengelola: React.FC = () => {
  const auth = useContext(AuthContext);
  const [data, setData] = useState<any>([]);
  const [loading, setLoading] = useState(true);
  const { idPengelola } = useParams();
  const navigate = useNavigate();

  const fetchData = async () => {
    try {
      const res = await getDashboardData();
      setData(
        res.trash_managers.trash_managers.filter((pengelola: any) => pengelola.id === parseInt(idPengelola || "0"))[0]
      );
      setLoading(false);
    } catch (error: any) {
      Swal.fire({
        icon: "error",
        title: "Oops...",
        text: error.message,
      });
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <Box w="100%" bgColor="var(--color-white)" borderRadius="15px" p="2rem">
      <Skeleton isLoaded={!loading} w="100%" h={loading ? "5rem" : "max-content"}>
        <Button variant="link" onClick={() => navigate(-1)} mb="1rem">
          <Icon as={IoMdArrowBack} mr="0.2rem" />
          kembali
        </Button>
        <VStack alignItems="flex-start" spacing="0.2rem">
          <Heading fontSize="2xl">{data?.nama_pengelola}</Heading>
          <Text>{data?.tempat}</Text>
          <Text>{data?.email}</Text>
        </VStack>
      </Skeleton>
      <Skeleton isLoaded={!loading} w="100%" h={loading ? "10rem" : "max-content"}>
        <TableContainer mt="2rem">
          <Table variant="striped">
            <TableCaption>Lorem ipsum dolor sit, amet consectetur adipisicing elit. Possimus, doloribus.</TableCaption>
            <Thead>
              <Tr>
                <Th>Nama Warga</Th>
                <Th>Alamat</Th>
                <Th>Telepon</Th>
                <Th>Saldo</Th>
                <Th>Aksi</Th>
              </Tr>
            </Thead>
            <Tbody w="50%">
              {data?.users?.length > 0 ? (
                data?.users.map((warga: any, idx: number) => (
                  <Tr key={`warga-${idx}`}>
                    <Td>
                      <Text whiteSpace="initial">{warga.full_name}</Text>
                    </Td>
                    <Td>
                      <Text whiteSpace="initial">{warga.address}</Text>
                    </Td>
                    <Td>
                      <Text whiteSpace="initial">{warga.phone_number}</Text>
                    </Td>
                    <Td>{currencyFormatter(warga.balance)}</Td>
                    <Td>
                      <Link
                        to={warga.role === "shop" ? `/dashboard/warung/${warga.id}` : `/dashboard/warga/${warga.id}`}
                      >
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
      </Skeleton>
    </Box>
  );
};

export default DetailPengelola;
