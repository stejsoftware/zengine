import React, { useState, useEffect } from "react";
import { ReactTerminal, TerminalContextProvider } from "react-terminal";

function Zio() {
  // Define commands here
  const commands = {
    "*": async () =>
      await new Promise((resolve) =>
        setTimeout(() => {
          resolve("Hello");
        }, 1000)
      ),
  };

  return (
    <div>
      zio
      <TerminalContextProvider>
        <ReactTerminal
          commands={commands}
          caret={false}
          theme="dark"
          showControlButtons={false}
          controlButtonLabels={["close"]}
          prompt=">"
        />
      </TerminalContextProvider>
    </div>
  );
}

export default Zio;
