import React, { createContext, useEffect, useState } from "react";
import { getDashboardData } from "../services/service";
import { authProviderValueType, resApiAuth } from "../types/type";

export const AuthContext = createContext<authProviderValueType>({
  token: "",
  data: {},
  setData: () => {},
  isLoggedIn: false,
  clearSession: () => {},
});

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [authData, setAuthData] = useState<resApiAuth>({ token: "", userData: {} });
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);

  const handleSetData = (data: resApiAuth) => {
    window.sessionStorage.setItem("token", data.token);
    setAuthData(data);
    setIsLoggedIn(true);
  };

  const handleClearSession = () => {
    window.sessionStorage.clear();
    setAuthData({ token: "", userData: {} });
    setIsLoggedIn(false);
  };

  const providerValue = {
    token: authData.token,
    data: authData.userData,
    setData: handleSetData,
    isLoggedIn,
    clearSession: handleClearSession,
  };

  const fetchUserData = async (token: string) => {
    try {
      const res = await getDashboardData();
      const userData = res.trash_managers;
      delete userData.trash_managers;
      setAuthData({ token: token, userData: userData });
    } catch (error: any) {
      console.log(error);
    }
  };

  useEffect(() => {
    const token = window.sessionStorage.getItem("token");
    if (token) {
      fetchUserData(token);
      setIsLoggedIn(true);
    }
  }, []);

  return <AuthContext.Provider value={providerValue}>{children}</AuthContext.Provider>;
};
