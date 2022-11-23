import axios from "axios";
import { loginInputs } from "../types/type";

const APP_CONFIG = {
  VITE_ENV: import.meta.env,
  API_URL: import.meta.env.VITE_API_URL,
};

export const authLogin = async (email: string, password: string) => {
  const res = await axios.post(`${APP_CONFIG.API_URL}/auth/login/super-admin`, {
    email: email,
    password: password,
  });
  return res.data;
};

export const getDashboardData = async (token: string) => {
  const res = await axios.get(`${APP_CONFIG.API_URL}/super-admin`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return res.data;
};
