const React = require("react");
const ReactDOM = require("react-dom");

import Card from "react-bootstrap/Card";
import Container from "react-bootstrap/Container";
import Image from "react-bootstrap/Image";

import "bootstrap/dist/css/bootstrap.css";

const App = () => (
  <Container className="p-2">
    <Card>
      <Card.Header>
        <h1>Zork Virtual Machine</h1>
      </Card.Header>
      <Card.Body>
        <Image thumbnail src="/zork-screen_5.webp" />
      </Card.Body>
      <Card.Footer>Copyright &copy; 2020</Card.Footer>
    </Card>
  </Container>
);

ReactDOM.render(<App />, document.getElementById("react"));
