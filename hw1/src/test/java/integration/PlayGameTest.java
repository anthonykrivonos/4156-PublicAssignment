package integration;

import controllers.PlayGame;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import models.GameBoard;
import models.Message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

public class PlayGameTest {

  /**
   * Runs only once before the testing starts.
   */
  @BeforeAll
  public static void init() {
    // Start Server
    PlayGame.main(new String[0]);
    System.out.println("[Before All] Starting server");
  }

  @BeforeEach
  public void startNewGame() {
    // Call to /newgame
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    int resStatus = response.getStatus();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);
    System.out.println("[Before Each] Started /newgame");
  }

  @Test
  @Order(1)
  public void testStartGameSuccess() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    GameBoard gameBoard = new Gson().fromJson(resBody, GameBoard.class);

    Assertions.assertEquals('X', gameBoard.getP1().getType());
    Assertions.assertEquals(1, gameBoard.getTurn());
    Assertions.assertEquals(0, gameBoard.getWinner());
    Assertions.assertEquals(false, gameBoard.isDraw());
    Assertions.assertArrayEquals(new char[3][3], gameBoard.getBoardState());

    System.out.println("[Order 1] Tested /startgame Success");
  }

  @Test
  @Order(2)
  public void testStartGameFailure_InvalidType_NoType() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 500 Internal Server Error
    Assertions.assertEquals(500, resStatus);
    Assertions.assertEquals("Invalid type", resBody);

    System.out.println("[Order 2] Tested /startgame Invalid Type (No Type)");
  }

  @Test
  @Order(3)
  public void testStartGameFailure_InvalidType_WrongType() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame").body("type=A").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 500 Internal Server Error
    Assertions.assertEquals(500, resStatus);
    Assertions.assertEquals("Invalid type", resBody);

    System.out.println("[Order 3] Tested /startgame Invalid Type (Wrong Type)");
  }

  @Test
  @Order(4)
  public void testJoinGameSuccess() {
    // Start the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();

    // Do not let Uni-rest follow redirects
    Unirest.config().reset().followRedirects(false);
    
    // Join the game successfully
    HttpResponse<String> response = Unirest.get("http://localhost:8080/joingame").asString();
    int resStatus = response.getStatus();

    // Reset settings
    Unirest.config().reset().followRedirects(true);

    // Assert the server responds with 302 Found
    Assertions.assertEquals(302, resStatus);

    System.out.println("[Order 4] Tested /joingame Success");
  }

  @Test
  @Order(5)
  public void testJoinGameFailure_BoardNotInitialized() {
    // Join the game without starting it
    HttpResponse<String> response = Unirest.get("http://localhost:8080/joingame").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 500 Internal Server Error
    Assertions.assertEquals(500, resStatus);
    Assertions.assertEquals("Board not initialized", resBody);

    System.out.println("[Order 5] Tested /startgame Board Not Initialized");
  }

  @Test
  @Order(6)
  public void testJoinGameFailure_GameAlreadyStarted() {
    // Start the game and join the game already
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Play until the game ends in a draw
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=2&y=1").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();

    // Join the game after having already ended it
    HttpResponse<String> response = Unirest.get("http://localhost:8080/joingame").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 400 Bad Request
    Assertions.assertEquals(400, resStatus);
    Assertions.assertEquals("Game already started", resBody);

    System.out.println("[Order 6] Tested /startgame Game Already Started");
  }

  @Test
  @Order(7)
  public void testMoveSuccess() {
    // Start the game and join the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Make a move as player 1
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    Message message = new Gson().fromJson(resBody, Message.class);

    Assertions.assertEquals(true, message.isMoveValid());
    Assertions.assertEquals(100, message.getCode());
    Assertions.assertEquals("", message.getMessage());

    System.out.println("[Order 7] Tested /move Success");
  }

  @Test
  @Order(8)
  public void testMoveFailure_GameNotStarted_NoStart() {
    // Make a move as player 1 without starting the game or joining the game
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 400 Bad Request
    Assertions.assertEquals(400, resStatus);
    Assertions.assertEquals("Game not started", resBody);

    System.out.println("[Order 8] Tested /move Game Not Started (No Start)");
  }

  @Test
  @Order(9)
  public void testMoveFailure_GameNotStarted_NoJoin() {
    Unirest.post("http://localhost:8080/startgame").body("type=X");

    // Make a move as player 1 without joining the game
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 400 Bad Request
    Assertions.assertEquals(400, resStatus);
    Assertions.assertEquals("Game not started", resBody);

    System.out.println("[Order 9] Tested /move Game Not Started (No Join)");
  }

  @Test
  @Order(10)
  public void testMoveSuccess_GameAlreadyOver() {
    // Start the game and join the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Play until the game ends in a draw
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=2&y=1").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();

    // Make a move as player 2
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    Message message = new Gson().fromJson(resBody, Message.class);

    Assertions.assertEquals(false, message.isMoveValid());
    Assertions.assertEquals(101, message.getCode());
    Assertions.assertEquals("Game already over", message.getMessage());

    System.out.println("[Order 10] Tested /move Success (Game Already Over)");
  }

  @Test
  @Order(11)
  public void testMoveSuccess_InvalidPlayerId() {
    // Start the game and join the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Make a move with a player id of 100
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/100").body("x=1&y=1").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    Message message = new Gson().fromJson(resBody, Message.class);

    Assertions.assertEquals(false, message.isMoveValid());
    Assertions.assertEquals(102, message.getCode());
    Assertions.assertEquals("Invalid playerId", message.getMessage());

    System.out.println("[Order 11] Tested /move Success (Invalid playerId)");
  }

  @Test
  @Order(12)
  public void testMoveSuccess_IncorrectPlayerId() {
    // Start the game and join the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Make a move as player 3
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/3").body("x=1&y=1").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    Message message = new Gson().fromJson(resBody, Message.class);

    Assertions.assertEquals(false, message.isMoveValid());
    Assertions.assertEquals(103, message.getCode());
    Assertions.assertEquals("Incorrect playerId", message.getMessage());

    System.out.println("[Order 12] Tested /move Success (Incorrect playerId)");
  }

  @Test
  @Order(13)
  public void testMoveSuccess_MissingPosition_X() {
    // Start the game and join the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Make a move as player 1 without an X position
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=&y=1").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    Message message = new Gson().fromJson(resBody, Message.class);

    Assertions.assertEquals(false, message.isMoveValid());
    Assertions.assertEquals(104, message.getCode());
    Assertions.assertEquals("Missing position", message.getMessage());

    System.out.println("[Order 13] Tested /move Success (Missing Position - X)");
  }

  @Test
  @Order(14)
  public void testMoveSuccess_MissingPosition_Y() {
    // Start the game and join the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Make a move as player 1 without a Y position
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    Message message = new Gson().fromJson(resBody, Message.class);

    Assertions.assertEquals(false, message.isMoveValid());
    Assertions.assertEquals(104, message.getCode());
    Assertions.assertEquals("Missing position", message.getMessage());

    System.out.println("[Order 14] Tested /move Success (Missing Position - Y)");
  }

  @Test
  @Order(15)
  public void testMoveSuccess_InvalidPosition() {
    // Start the game and join the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Make a move as player 1 with an invalid position
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=3&y=-1").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    Message message = new Gson().fromJson(resBody, Message.class);

    Assertions.assertEquals(false, message.isMoveValid());
    Assertions.assertEquals(105, message.getCode());
    Assertions.assertEquals("Invalid position", message.getMessage());

    System.out.println("[Order 15] Tested /move Success (Invalid Position)");
  }

  @Test
  @Order(16)
  public void testMoveSuccess_PositionAlreadyFilled() {
    // Start the game and join the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Make a move as player 1 in the middle tile
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();

    // Make a move as player 2 in an already-filled position
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    Message message = new Gson().fromJson(resBody, Message.class);

    Assertions.assertEquals(false, message.isMoveValid());
    Assertions.assertEquals(105, message.getCode());
    Assertions.assertEquals("Position already filled", message.getMessage());

    System.out.println("[Order 16] Tested /move Success (Position Already Filled)");
  }

  @Test
  @Order(17)
  public void testMoveSuccess_NotYourTurn() {
    // Start the game and join the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Make a move as player 1 in the middle tile
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();

    // Make a move as player 1 a second time in a row
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    Message message = new Gson().fromJson(resBody, Message.class);

    Assertions.assertEquals(false, message.isMoveValid());
    Assertions.assertEquals(105, message.getCode());
    Assertions.assertEquals("Not your turn", message.getMessage());

    System.out.println("[Order 17] Tested /move Success (Not Your Turn)");
  }

  @Test
  @Order(18)
  public void testMoveSuccess_Draw() {
    // Start the game and join the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Play until the game ends in a draw
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=2&y=1").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=0").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();

    // Make a move as player 1 to draw
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    Message message = new Gson().fromJson(resBody, Message.class);

    Assertions.assertEquals(true, message.isMoveValid());
    Assertions.assertEquals(100, message.getCode());
    Assertions.assertEquals("", message.getMessage());

    System.out.println("[Order 18] Tested /move Success (Draw)");
  }

  @Test
  @Order(19)
  public void testMoveSuccess_P1Wins() {
    // Start the game and join the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Play until the game ends in a draw
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();

    // Make a move as player 1 to win
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=1").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    Message message = new Gson().fromJson(resBody, Message.class);

    Assertions.assertEquals(true, message.isMoveValid());
    Assertions.assertEquals(100, message.getCode());
    Assertions.assertEquals("", message.getMessage());

    System.out.println("[Order 19] Tested /move Success (P1 Wins)");
  }

  @Test
  @Order(20)
  public void testMoveSuccess_P2Wins() {
    // Start the game and join the game
    Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    Unirest.get("http://localhost:8080/joingame").asString();

    // Play until the game ends in a draw
    Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    Unirest.post("http://localhost:8080/move/1").body("x=0&y=2").asString();

    // Make a move as player 2 to win
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    int resStatus = response.getStatus();
    String resBody = response.getBody();

    // Assert the server responds with 200 OK
    Assertions.assertEquals(200, resStatus);

    // Parse the response
    Message message = new Gson().fromJson(resBody, Message.class);

    Assertions.assertEquals(true, message.isMoveValid());
    Assertions.assertEquals(100, message.getCode());
    Assertions.assertEquals("", message.getMessage());

    System.out.println("[Order 20] Tested /move Success (P2 Wins)");
  }

  @AfterAll
  public static void close() {
    // Stop Server
    PlayGame.stop();
    System.out.println("[After All] Close");
  }

}
