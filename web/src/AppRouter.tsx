import React from "react";
import { Routes, Route } from "react-router-dom";
import App from "./App";
import DashboardLayout from "./layout/DashboardLayout";
import Login from "./pages/Login";
import Overview from "./pages/Overview";
import UserList from "./pages/UserList";

const AppRouter: React.FC = () => {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/dashboard" element={<DashboardLayout />}>
        <Route path="" element={<Overview />} />
        <Route path="user" element={<UserList />} />
      </Route>
      <Route path="/" element={<App />} />
    </Routes>
  );
};

export default AppRouter;
