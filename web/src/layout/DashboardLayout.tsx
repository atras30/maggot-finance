import {
  Heading,
  Box,
  VStack,
  Button,
  Divider,
  Flex,
  Input,
  Image,
  Text,
  Icon,
  Center,
  InputGroup,
  InputLeftElement,
} from "@chakra-ui/react";
import React, { useContext, useEffect } from "react";
import { Navigate, NavLink, Outlet } from "react-router-dom";
import "./DashboardLayout.scss";
import { FaHome, FaSearch } from "react-icons/fa";
import { AuthContext } from "../context/AuthContext";
import { getDashboardData } from "../services/service";

const NavButton = (props: any) => {
  return (
    <NavLink to={props.to} className="dash-link" end>
      <Flex w="100%" className={`container ${props.type == "logout" && "danger"}`} alignItems="center" gap="1rem">
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
  const auth = useContext(AuthContext);

  const fetchData = async () => {
    try {
      const res = await getDashboardData(auth.token);
      auth.setData({ token: auth.token, userData: res.trash_managers });
    } catch (error: any) {
      auth.clearSession();
      console.log(error);
    }
  };

  useEffect(() => {
    if (auth.isLoggedIn) {
      fetchData();
    }
  }, []);

  if (auth.isLoggedIn)
    return (
      <Flex bgColor="var(--color-light)" minH="100vh" w="100%" p="2rem" className="dashboard">
        <Flex as="aside" flexDir="column" w="20rem" pos="fixed" gap="0.5rem">
          <Flex alignItems="center" gap="0.5rem" justifyContent="center">
            <Image src="/assets/MagFinance_logo.png" boxSize="50px" />
            <Heading fontSize="3xl" color="var(--color-secondary)">
              MagFin
            </Heading>
          </Flex>
          <Divider h="2px" bgColor="var(--color-primary)" my="1rem" />
          <NavButton to="/dashboard">Ringkasan</NavButton>
          <NavButton to="/dashboard/bank-sampah">Pengelola Bank Sampah</NavButton>
          <NavButton to="/dashboard/warga">Warga</NavButton>
          <NavButton to="/dashboard/warung">Warung</NavButton>
          <Divider h="2px" bgColor="var(--color-primary)" my="1rem" />
          <NavButton to="/logout" type="logout">
            Logout
          </NavButton>
        </Flex>
        <VStack w="100%" ml="22rem" alignItems="flex-start" gap="1rem">
          <Heading>Dashboard</Heading>
          <Outlet />
          <Text as="footer">
            @ 2022, Made with ❤️️ by <b>Adrian Fiantnyo, Atras Shalhan, Jonathan Putra</b> & <b>Reynard Yaputra</b>.
          </Text>
        </VStack>
      </Flex>
    );
  else return <Navigate to="/" />;
};

export default DashboardLayout;
