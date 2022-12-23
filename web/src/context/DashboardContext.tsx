import React, { createContext } from "react";

type dashboardProviderValueType = {};

export const DashboardContext = createContext<dashboardProviderValueType>({});

export const DashboardProvider: React.FC<{ children: React.ReactNode }> = (props) => {
  return <DashboardContext.Provider value={{}}>{props.children}</DashboardContext.Provider>;
};
