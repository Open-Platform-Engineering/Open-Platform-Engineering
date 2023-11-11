import { Outlet, useNavigation } from "react-router-dom";
import { useState, useEffect, useReducer } from "react";
import Nav from "../components/Nav";
import Footer from "../components/Footer";
import axios from "axios";
import { Account, AccountContext } from "../AccountContext";
export async function loader() {
  return {};
}

export default function Root() {
  axios.defaults.baseURL = 'http://localhost:8080';
  axios.defaults.headers.post['Content-Type'] = 'application/json';

  return (
    <>
        <Account >
          <Nav></Nav>
          <div className="flex flex-row justify-center">
            <Outlet></Outlet>
          </div>
        </Account>
        <Footer></Footer>
    </>
  );
}
