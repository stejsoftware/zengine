import { useEffect } from "react";
import { Card, Container, Stack } from "react-bootstrap";
import { io } from "socket.io-client";
import "./App.css";

function App() {
  // const [socket, setSocket] = useState<Socket>();

//   useEffect(() => {
//     const socket = io("http://localhost:8080");

//     console.log("socket", socket.id);

//     socket.on("connect", () => {
//       console.log("connect", socket.id);
//     });

//     socket.on("disconnect", () => {
//       console.log("disconnect", socket.id);
//     });
//   }, []);

  return (
    <Container>
      <Card className="mt-1">
        <Card.Header>ZEngine</Card.Header>
        <Card.Body>
          <ul>
            <li>
              <a href="/v0/stories">Stories</a>
            </li>
          </ul>
        </Card.Body>
        <Card.Footer>
          <Stack direction="horizontal">
            <span>&copy; 2023 Jonathan Meyer</span>
            <a className="ms-auto" href="/swagger-ui/index.html">
              API Docs
            </a>
          </Stack>
        </Card.Footer>
      </Card>
    </Container>
  );
}

export default App;
