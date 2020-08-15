package controllers;

import org.eclipse.jetty.websocket.api.*;

import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsHandler;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Web socket class: DO NOT MODIFY
 * @author Shirish Singh
 *
 */
public class UiWebSocket implements Consumer<WsHandler>  {

	// Store sessions to broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

	@Override
	public void accept(WsHandler t) {

		// On Connect
		t.onConnect(new WsConnectHandler() {

			@Override
			public void handleConnect(WsConnectContext ctx) throws Exception {
				// TODO Auto-generated method stub
				sessions.add(ctx.session);
			}
			
		});
		
		// On Close
		t.onClose(new WsCloseHandler() {
			
			@Override
			public void handleClose(WsCloseContext ctx) throws Exception {
				sessions.remove(ctx.session);
			}
		});
	}

	public static Queue<Session> getSessions() {
		return sessions;
	}

}
