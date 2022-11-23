import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";

import DashboardLayout from "./layout/DashboardLayout";

import Login from "./pages/Login";
import Overview from "./pages/Overview";
import ListBankSampah from "./pages/ListBankSampah";
import ListWarga from "./pages/ListWarga";

const AppRouter: React.FC = () => {
  return (
    <Routes>
      <Route path="/dashboard" element={<DashboardLayout />}>
        <Route index element={<Overview />} />
        <Route path="bank-sampah" element={<ListBankSampah />} />
        <Route path="warga" element={<ListWarga />} />
        <Route path="*" element={<Navigate to="/dashboard" />} />
      </Route>
      <Route index element={<Login />} />
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
};

export default AppRouter;
