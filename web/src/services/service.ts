import axios from "axios";
import { loginInputs } from "../types/type";

const token = window.sessionStorage?.getItem("token");

const APP_CONFIG = {
  VITE_ENV: import.meta.env,
  API_URL: import.meta.env.VITE_API_URL,
};

export const currencyFormatter = (value: number) => {
  return new Intl.NumberFormat("id-ID", {
    style: "currency",
    currency: "IDR",
  }).format(value);
};

export const authLogin = async (email: string, password: string) => {
  const res = await axios.post(`${APP_CONFIG.API_URL}/auth/login/super-admin`, {
    email: email,
    password: password,
  });
  return res.data;
};

export const getDashboardData = async () => {
  const res = await axios.get(`${APP_CONFIG.API_URL}/super-admin`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return res.data;
};

export const addNewBankSampah = async (data: { nama: string; tempat: string; email: string }) => {
  console.log(token);
  const res = await axios.post(
    `${APP_CONFIG.API_URL}/trash-manager`,
    {
      nama_pengelola: data.nama,
      tempat: data.tempat,
      email: data.email,
    },
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return res.data;
};

export const getListWarga = async () => {
  const res = await axios.get(`${APP_CONFIG.API_URL}/user/role/farmer`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return res.data;
};

export const getListWarung = async () => {
  const res = await axios.get(`${APP_CONFIG.API_URL}/user/role/shop`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return res.data;
};
