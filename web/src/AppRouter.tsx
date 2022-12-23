import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";

import DashboardLayout from "./layout/DashboardLayout";

import Login from "./pages/Login";
import Overview from "./pages/Overview";
import ListBankSampah from "./pages/ListBankSampah";
import ListWarga from "./pages/ListWarga";
import Logout from "./pages/Logout";
import DetailPengelola from "./pages/DetailPengelola";
import TambahBankSampah from "./pages/TambahBankSampah";
import ListWarung from "./pages/ListWarung";
import DetailWarga from "./pages/DetailWarga";
import DetailWarung from "./pages/DetailWarung";

const AppRouter: React.FC = () => {
  return (
    <Routes>
      <Route path="/dashboard" element={<DashboardLayout />}>
        <Route path="ringkasan" element={<Overview />} />
        <Route path="bank-sampah" element={<ListBankSampah />} />
        <Route path="bank-sampah/tambah" element={<TambahBankSampah />} />
        <Route path="bank-sampah/:idPengelola" element={<DetailPengelola />} />
        <Route path="warung" element={<ListWarung />} />
        <Route path="warung/:idWarung" element={<DetailWarung />} />
        <Route path="warga" element={<ListWarga />} />
        <Route path="warga/:idWarga" element={<DetailWarga />} />
        <Route path="" element={<Navigate to="/dashboard/ringkasan" />} />
      </Route>
      <Route index element={<Login />} />
      <Route path="logout" element={<Logout />} />
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
};

export default AppRouter;
