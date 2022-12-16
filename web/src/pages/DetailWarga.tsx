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
  Divider,
  Flex,
  Spacer,
  Icon,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import { IoMdArrowBack } from "react-icons/io";
import { useNavigate, useParams } from "react-router-dom";
import stc from "string-to-color";
import { AuthContext } from "../context/AuthContext";
import { currencyFormatter, getDashboardData, getListWarga } from "../services/service";

const DetailWarga: React.FC = () => {
  const auth = useContext(AuthContext);
  const [data, setData] = useState<any>([]);
  const [loading, setLoading] = useState(true);
  const { idWarga } = useParams();
  const navigate = useNavigate();

  const fetchData = async () => {
    try {
      const res = await getListWarga();
      setData(res.users.filter((warga: any) => warga.id === parseInt(idWarga || "0"))[0]);
      setLoading(false);
    } catch (error: any) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <Box w="100%" bgColor="var(--color-white)" borderRadius="15px" p="2rem">
      <Skeleton isLoaded={!loading} w="100%" h={loading ? "3rem" : "max-content"}>
        <Button variant="link" onClick={() => navigate(-1)} mb="1rem">
          <Icon as={IoMdArrowBack} mr="0.2rem" />
          kembali
        </Button>
        <Flex alignItems="center">
          <Heading fontSize="2xl">{data?.full_name}</Heading>
          <Spacer />
          <Badge bgColor={stc(data?.nama_pengelola)} color="white" p="5px 10px">
            {data?.nama_pengelola}
          </Badge>
        </Flex>
      </Skeleton>
      <Divider my="1rem" />
      <Skeleton isLoaded={!loading} w="100%" h={loading ? "10rem" : "max-content"}>
        <TableContainer>
          <Table variant="striped">
            <Tbody>
              <Tr>
                <Td>Status Keanggotaan</Td>
                <Td>
                  <Badge colorScheme={data?.is_verified == 1 ? "green" : "red"}>
                    {data?.is_verified == 1 ? "Aktif" : "Tidak Aktif"}
                  </Badge>
                </Td>
              </Tr>
              <Tr>
                <Td>Telepone</Td>
                <Td>{data?.phone_number || "-"}</Td>
              </Tr>
              <Tr>
                <Td>Email</Td>
                <Td>{data?.email}</Td>
              </Tr>
              <Tr>
                <Td>Alamat</Td>
                <Td>{data?.address}</Td>
              </Tr>
              <Tr>
                <Td>Saldo</Td>
                <Td>{currencyFormatter(data?.balance)}</Td>
              </Tr>
            </Tbody>
          </Table>
        </TableContainer>
      </Skeleton>
    </Box>
  );
};

export default DetailWarga;
