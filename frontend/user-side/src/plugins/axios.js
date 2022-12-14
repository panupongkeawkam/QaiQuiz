import axios from "axios";

const instance = axios.create({});

instance.interceptors.request.use(
  function (config) {
    config.headers["Access-Control-Allow-Origin"] = "*";
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

export default instance;
