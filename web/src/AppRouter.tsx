import React from "react";
import { Routes, Route } from "react-router-dom";
import App from "./App";
import DashboardLayout from "./layout/DashboardLayout";
import Login from "./pages/Login";

const AppRouter: React.FC = () => {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/dashboard" element={<DashboardLayout />} />
      <Route path="/" element={<App />} />
    </Routes>
  );
};

export default AppRouter;
