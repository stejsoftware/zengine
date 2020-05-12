var io = require("socket.io-client");
var port = process.argv[2] || 8080;

Promise.all(
  ["/", "/admin", "/foo"].map((namespace) => {
    return new Promise((resolve, reject) => {
      const uri = `http://localhost:${port}${namespace}`;
      
      const t = setTimeout(() => {
        resolve({ uri, success: false, err: "connected with no event" });
      }, 5000);

      io(uri, {
        autoConnect: false,
        timeout: 1000,
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
