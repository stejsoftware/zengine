package com.stejsoftware.zengine.controller;

import io.socket.engineio.server.EngineIoServer;
import io.socket.socketio.server.SocketIoNamespace;
import io.socket.socketio.server.SocketIoServer;
import io.socket.socketio.server.SocketIoSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebServlet("/socket.io/*")
@Component
public class SocketIoServlet extends HttpServlet {
	private final EngineIoServer engineIoServer;
	private final SocketIoServer socketIoServer;

	public SocketIoServlet(EngineIoServer engineIoServer, SocketIoServer socketIoServer) {
		this.engineIoServer = engineIoServer;
		this.socketIoServer = socketIoServer;
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		engineIoServer.handleRequest(request, response);
		SocketIoNamespace namespace = socketIoServer.namespace("/");

		namespace.on("connection", args -> {
			SocketIoSocket socket = (SocketIoSocket) args[0];
			socket.on("foo", fooArgs -> {
				log.info("{}", fooArgs);
			});
		});

		namespace.on("error", args -> {
			log.error(">>>>", args);
		});

//		namespace.broadcast("public", "foo", "Hello Socket World");
	}
}
