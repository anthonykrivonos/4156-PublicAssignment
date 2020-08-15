package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;

/**
 * @author Shirish Singh
 *
 */
public class PlayGame {

	private static int PORT_NUMBER = 8080;
	
	private static Javalin app;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 
		app = Javalin.create(config -> {
			config.addStaticFiles("/public");
			}).start(PORT_NUMBER);
        
        // Test Echo Server
 		app.post("/echo", ctx -> {
 			ctx.result(ctx.body());
 		});
 		
 		/**
 		 * Please add your end points here.
 		 * 
 		 * 
 		 * 
 		 * 
 		 * Please add your end points here.
 		 * 
 		 * 
 		 * 
 		 * 
 		 * Please add your end points here.
 		 * 
 		 * 
 		 * 
 		 * 
 		 * Please add your end points here.
 		 * 
 		 */
 		
		// Web sockets - DO NOT DELETE or CHANGE
		app.ws("/gameboard", new UiWebSocket());
	}
	
	/**
	 * Send message to all players
	 * @param gameBoardJson
	 * @throws IOException
	 */
	private static void sendGameBoardToAllPlayers(String gameBoardJson){
		Queue<Session> sessions = UiWebSocket.getSessions();
		for(Session sessionPlayer : sessions) {
			try {
				sessionPlayer.getRemote().sendString(gameBoardJson);
			} catch (IOException e) {
				// Add logger here
			}
		}
	}

    public static void stop() {
        app.stop();
    }
}
