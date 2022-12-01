import { Flex, Heading, InputGroup, InputLeftElement, Icon, Input, Box } from "@chakra-ui/react";
import React from "react";
import { FaSearch } from "react-icons/fa";

const Overview: React.FC = () => {
  return (
    <Box w="100%" h="15rem" bgColor="var(--color-white)" borderRadius="15px" p="2rem">
      <Flex justifyContent="space-between" alignItems="center">
        <Heading fontSize="xl">Ringkasan MagFin</Heading>
      </Flex>
    </Box>
  );
};

export default Overview;
