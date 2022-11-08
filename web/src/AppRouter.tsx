import React from "react";
import { Route, Routes } from "react-router-dom";
import App from "./App";
import Login from "./pages/Login";

const AppRouter: React.FC = () => {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/" element={<App />} />
    </Routes>
  );
};

export default AppRouter;
