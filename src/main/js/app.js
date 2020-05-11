import React from "react";
import ReactDOM from "react-dom";

import Container from "react-bootstrap/Container";

import Zvm from "./Zvm";

import "bootstrap/dist/css/bootstrap.css";

ReactDOM.render(
  <Container className="p-2">
    <Zvm />
  </Container>,
  document.getElementById("app")
);
