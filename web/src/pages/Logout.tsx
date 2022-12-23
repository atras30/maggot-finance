import React, { useContext } from "react";
import { Navigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";

const Logout: React.FC = () => {
  const auth = useContext(AuthContext);

  auth.clearSession();

  return <Navigate to="/" />;
};

export default Logout;
