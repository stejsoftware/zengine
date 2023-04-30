const { createProxyMiddleware } = require("http-proxy-middleware");
const morgan = require("morgan");

module.exports = function (app) {
  app.use(morgan());

  app.use(
    "/swagger-ui",
    createProxyMiddleware({
      target: "http://localhost:8080",
      changeOrigin: true,
    })
  );
};
