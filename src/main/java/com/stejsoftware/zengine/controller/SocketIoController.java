package com.stejsoftware.zengine.controller;

import io.socket.engineio.server.EngineIoServer;
import io.socket.socketio.server.SocketIoNamespace;
import io.socket.socketio.server.SocketIoServer;
import io.socket.socketio.server.SocketIoSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SocketIoController {
	private final EngineIoServer engineIoServer;
	private final SocketIoServer socketIoServer;

	public SocketIoController(EngineIoServer engineIoServer, SocketIoServer socketIoServer) {
		this.engineIoServer = engineIoServer;
		this.socketIoServer = socketIoServer;
	}

	@RequestMapping("/socket.io/*")
	public void socketIo(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		engineIoServer.handleRequest(request, response);
//		SocketIoNamespace namespace = socketIoServer.namespace("/");
//
//		namespace.on("connection", args -> {
//			SocketIoSocket socket = (SocketIoSocket) args[0];
//			socket.on("foo", fooArgs -> {
//				log.info("{}", fooArgs);
//			});
//		});
//
//		namespace.on("error", args -> {
//			log.error(">>>>", args);
//		});

//		namespace.broadcast("public", "foo", "Hello Socket World");
	}
}
