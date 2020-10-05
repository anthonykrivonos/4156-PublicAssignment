package unit;

import models.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessageTest {

  @Test
  public void testMessageConstructor() {
    final boolean moveValidity = true;
    final int code = 100;
    final String message = "Valid move.";
    final Message msg = new Message(moveValidity, code, message);
    
    Assertions.assertEquals(moveValidity, msg.isMoveValid());
    Assertions.assertEquals(code, msg.getCode());
    Assertions.assertEquals(message, msg.getMessage());
  }
  
  @Test
  public void testSetMoveValidity() {
    final Message msg = new Message(true, 100, "");
    
    msg.setMoveValidity(false);
    
    Assertions.assertEquals(false, msg.isMoveValid());
  }
  
  @Test
  public void testSetCode() {
    final Message msg = new Message(true, 100, "");
    
    msg.setCode(300);
    
    Assertions.assertEquals(300, msg.getCode());
  }
  
  @Test
  public void testSetMessage() {
    final Message msg = new Message(true, 100, "");
    
    msg.setMessage("Message");
    
    Assertions.assertEquals("Message", msg.getMessage());
  }
  
  @Test
  public void testToJson() {
    final Message msg = new Message(true, 100, "");
    
    Assertions.assertNotEquals("", msg.toJson());
  }

}
