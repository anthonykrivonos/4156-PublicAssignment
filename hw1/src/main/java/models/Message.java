package models;

import com.google.gson.Gson;

public class Message {

  private boolean moveValidity;

  private int code;

  private String message;

  /**
   * Constructs a new game message.
   * @param moveValidity True if the move is valid, false otherwise.
   * @param code The move's code.
   * @param message A string message associated with the move.
   */
  public Message(boolean moveValidity, int code, String message) {
    super();
    this.moveValidity = moveValidity;
    this.code = code;
    this.message = message;
  }

  /**
   * Checks if the move is a valid one.
   * @return True if the move is valid, false otherwise.
   */
  public boolean isMoveValid() {
    return moveValidity;
  }

  /**
   * Sets whether or not the move is a valid one.
   * @param moveValidity True if the move is valid, false otherwise.
   */
  public void setMoveValidity(boolean moveValidity) {
    this.moveValidity = moveValidity;
  }

  /**
   * Gets the response code of the message.
   * @return The response code as an int.
   */
  public int getCode() {
    return code;
  }

  /**
   * Sets the response code of the message.
   * @param code The response code as an int.
   */
  public void setCode(int code) {
    this.code = code;
  }

  /**
   * Gets the string content of the message.
   * @return The string message content.
   */
  public String getMessage() {
    return message;
  }

  /**
   * Sets the string content of the message.
   * @param message The string message content.
   */
  public void setMessage(String message) {
    this.message = message;
  }
  
  /**
   * Convert this Message into a JSON string.
   * @return A JSON Message string.
   */
  public String toJson() {
    return new Gson().toJson(this);
  }

}
