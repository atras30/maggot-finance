export type loginInputs = {
  email: string;
  password: string;
};

export type authProviderValueType = {
  token: string;
  data: Object | any;
  setData: (data: resApiAuth) => void;
  isLoggedIn: boolean;
  clearSession: () => void;
};

export type resApiAuth = {
  token: string;
  userData: Object;
};
