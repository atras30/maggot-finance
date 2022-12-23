import {
  Flex,
  Heading,
  InputGroup,
  InputLeftElement,
  Icon,
  Input,
  Box,
  Wrap,
  HStack,
  Skeleton,
} from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { FaSearch } from "react-icons/fa";
import Swal from "sweetalert2";
import { getDashboardData, getListWarga, getListWarung } from "../services/service";

const OverviewCard = (props: any) => {
  return (
    <Skeleton isLoaded={!props.isLoading} borderRadius="15px" overflow="hidden">
      <HStack w="18rem" h="10rem" p="2rem" bgColor="var(--color-white-alt)" spacing="1.5rem">
        <Heading fontSize="xl">{props.text}</Heading>
        <Heading fontSize="6xl">{props.count > 0 ? props.count : "-"}</Heading>
      </HStack>
    </Skeleton>
  );
};

const Overview: React.FC = () => {
  const [data, setData] = useState<any>({});
  const [loading, setLoading] = useState(true);

  const fetchData = async () => {
    try {
      Promise.all([getDashboardData(), getListWarga(), getListWarung()]).then((res) => {
        const tempData = {
          trash_managers: res[0].trash_managers,
          warga: res[1],
          warung: res[2],
        };
        setData(tempData);
      });
    } catch (error: any) {
      Swal.fire({
        icon: "error",
        title: "Oops...",
        text: error.message,
      });
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
        <Heading fontSize="xl">Ringkasan MagFin</Heading>
      </Flex>
      <Wrap mt="2rem" p="1rem" spacing="2rem">
        <OverviewCard
          isLoading={loading}
          text="Total Pengelola Bank Sampah"
          count={data?.trash_managers?.trash_managers?.length || 0}
        />
        <OverviewCard isLoading={loading} text="Total Warga Binaan" count={data?.warga?.total_record || 0} />
        <OverviewCard isLoading={loading} text="Total Warung Mitra" count={data?.warung?.total_record || 0} />
      </Wrap>
    </Box>
  );
};

export default Overview;
