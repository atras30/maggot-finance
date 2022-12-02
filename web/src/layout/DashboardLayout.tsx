import { Heading, Box, VStack, Button, Divider, Flex, Input, Image, Text, Icon, Center, InputGroup, InputLeftElement } from "@chakra-ui/react";
import React from "react";
import { NavLink, Outlet } from "react-router-dom";
import "./DashboardLayout.scss";
import { FaHome, FaSearch } from "react-icons/fa";

const NavButton = (props: any) => {
  return (
    <NavLink to={props.to} className="dash-link" end>
      <Flex w="100%" className="container" alignItems="center" gap="1rem">
        <Center boxSize="3rem" className="icon">
          <Icon as={FaHome} boxSize="1.2rem" />
        </Center>
        <Text fontWeight="bold" fontSize="md">
          {props.children}
        </Text>
      </Flex>
    </NavLink>
  );
};

const DashboardLayout: React.FC = () => {
  return (
    <Flex bgColor="var(--color-light)" minH="100vh" w="100%" p="2rem" className="dashboard">
      <Flex as="aside" flexDir="column" w="20rem" pos="fixed" gap="0.5rem">
        <Flex alignItems="center" gap="0.5rem" justifyContent="center">
          <Image src="/assets/MagFinance_logo.png" boxSize="50px" />
          <Heading fontSize="2xl" color="var(--color-secondary)">
            MagFinance
          </Heading>
        </Flex>
        <Divider h="2px" bgColor="var(--color-primary)" my="1rem" />
        <NavButton to="/dashboard">Overview</NavButton>
        <NavButton to="/dashboard/user">Pengelola Bank Sampah</NavButton>
        <NavButton to="/">Warga</NavButton>
        <NavButton to="/">Warung</NavButton>
      </Flex>
      <VStack w="100%" ml="22rem" alignItems="flex-start" gap="1rem">
        <Heading>Dashboard</Heading>
        <Outlet />
        <Text as="footer">
          @ 2022, Made with ❤️️ by <b>Adrian Fiantnyo, Atras Salhaan, Reynard Yaputra</b> & <b>Jonathan Putra</b>.
        </Text>
      </VStack>
    </Flex>
  );
};

export default DashboardLayout;
