import {
  Box,
  Heading,
  Skeleton,
  Badge,
  Table,
  TableContainer,
  Tbody,
  Td,
  Tr,
  Divider,
  Flex,
  Spacer,
  Button,
  Link,
  Icon,
} from "@chakra-ui/react";
import React, { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { currencyFormatter, getListWarung } from "../services/service";
import { IoMdArrowBack } from "react-icons/io";
import { useNavigate } from "react-router-dom";
import stc from "string-to-color";

const DetailWarung: React.FC = () => {
  const auth = useContext(AuthContext);
  const [data, setData] = useState<any>([]);
  const [loading, setLoading] = useState(true);
  const { idWarung } = useParams();
  const navigate = useNavigate();

  const fetchData = async () => {
    try {
      const res = await getListWarung();
      setData(res.users.filter((warung: any) => warung.id === parseInt(idWarung || "0"))[0]);
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

export default DetailWarung;
