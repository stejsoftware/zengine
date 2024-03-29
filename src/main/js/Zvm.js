import React, { useState, useEffect } from "react";

import { ReactTerminal } from "react-terminal-component";
import {
  EmulatorState,
  CommandMapping,
  FileSystem,
  EnvironmentVariables,
  History,
  Outputs,
  OutputFactory,
} from "javascript-terminal";

import Card from "react-bootstrap/Card";
import Alert from "react-bootstrap/Alert";

import socketIOClient from "socket.io-client";

function Zvm() {
  const [socket, setSocket] = useState(
    socketIOClient("/", { autoConnect: false })
  );
  const [connected, setConnected] = useState(false);
  const [text, setText] = useState("");
  const [inputStr, setInputStr] = useState("");
  const [emulatorState, setEmulatorState] = useState(
    EmulatorState.create({
      fs: FileSystem.create({}),
      environmentVariables: EnvironmentVariables.create(),
      history: History.create(),
      outputs: Outputs.create(),
      commandMapping: CommandMapping.create({
        foo: {
          function: () => {
            return {
              output: OutputFactory.makeTextOutput(
                JSON.stringify({ bar: Date.now() })
              ),
            };
          },
          optDef: {},
        },
        list: {
          function: (state, opts) => {
            socket.emit("list", ...opts, (list) => {
              console.log(list);
            });

            return { output: OutputFactory.makeTextOutput("list") };
          },
          optDef: {},
        },
      }),
    })
  );

  useEffect(() => {
    socket
      .on("connect", () => {
        setConnected(true);
        socket.emit("list", (list) => {
          console.log(list);
        });
      })
      .on("disconnect", () => {
        setConnected(false);
      })
      .on("error", (data) => {
        setText(data);
      })
      .connect();
  }, []);

  return (
    <Card>
      <Card.Header>
        <h1>Zork Virtual Machine</h1>
      </Card.Header>
      <Card.Body>
        <Alert variant={connected ? "success" : "primary"}>
          Connected: {connected ? "True" : "False"}
        </Alert>
        {text && <Alert variant="danger">{text}</Alert>}
        <ReactTerminal
          autoFocus={false}
          clickToFocus={true}
          promptSymbol=">"
          emulatorState={emulatorState}
          inputStr={inputStr}
        />
      </Card.Body>
      <Card.Footer>Copyright &copy; 2020</Card.Footer>
    </Card>
  );
}

export default Zvm;
