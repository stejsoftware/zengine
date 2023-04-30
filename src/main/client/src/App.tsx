import React, {useEffect} from 'react';
import './App.css';
import {Card, Container} from "react-bootstrap";
import {io} from "socket.io-client";

function App() {
    // const [socket, setSocket] = useState<Socket>();

    useEffect(() => {
        const socket = io('http://localhost:8080');

        console.log("socket", socket.id);

        socket.on("connect", () => {
            console.log("connect", socket.id);
        });

        socket.on("disconnect", () => {
            console.log("disconnect", socket.id);
        });
    }, [])

    return (
        <Container>
            <Card>
                <Card.Header>ZEngine</Card.Header>
                <Card.Body>
                    <ul>
                        <li><a href="/stories">Stories</a></li>
                    </ul>
                </Card.Body>
                <Card.Footer>&copy; 2023 Jonathan Meyer</Card.Footer>
            </Card>
        </Container>
    );
}

export default App;
