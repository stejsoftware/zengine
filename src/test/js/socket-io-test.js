#!/usr/bin/env node
const { argv } = require("yargs");
const io = require("socket.io-client");

const port = argv.port || 8080;
const forceNew = argv.new || false;

console.log({ argv });

Promise.all(
  ["/", "/admin", "/foo"].map((namespace) => {
    return new Promise((resolve, reject) => {
      const uri = `http://localhost:${port}${namespace}`;

      const t = setTimeout(() => {
        resolve({ uri, success: false, err: "connected with no event" });
      }, 5000);

      io(uri, {
        autoConnect: false,
        forceNew,
      })
        .on("connect", () => {
          clearTimeout(t);
          resolve({ uri, success: true });
        })
        .on("error", (err) => {
          clearTimeout(t);
          resolve({ uri, success: false, err });
        })
        .on("connect_error", (err) => {
          clearTimeout(t);
          resolve({ uri, success: false, connect_err: err });
        })
        .on("connect_timeout", (err) => {
          clearTimeout(t);
          resolve({ uri, success: false, timeout: err });
        })
        .connect();
    });
  })
)
  .then((res) => {
    console.log(res);
    res.reduce((p, c) => c.success && p, true)
      ? process.exit(0)
      : process.exit(1);
  })
  .catch((ex) => {
    console.error(ex);
    process.exit(1);
  });
