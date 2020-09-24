package models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessageTest {
	
	/**
	 * Constructors
	 */

	@Test
	void testMessageConstructor() {
		final boolean moveValidity = true;
		final int code = 100;
		final String message = "Valid move.";
		final Message msg = new Message(moveValidity, code, message);
		
		Assertions.assertEquals(moveValidity, msg.isMoveValid());
		Assertions.assertEquals(code, msg.getCode());
		Assertions.assertEquals(message, msg.getMessage());
	}
	
	/**
	 * Setters
	 */
	
	@Test
	void testSetMoveValidity() {
		final Message msg = new Message(true, 100, "");
		
		msg.setMoveValidity(false);
		
		Assertions.assertEquals(false, msg.isMoveValid());
	}
	
	@Test
	void testSetCode() {
		final Message msg = new Message(true, 100, "");
		
		msg.setCode(300);
		
		Assertions.assertEquals(300, msg.getCode());
	}
	
	@Test
	void testSetMessage() {
		final Message msg = new Message(true, 100, "");
		
		msg.setMessage("Message");
		
		Assertions.assertEquals("Message", msg.getMessage());
	}
	
	/**
	 * JSON stringification
	 */
	
	@Test
	void testToJson() {
		final Message msg = new Message(true, 100, "");
		
		Assertions.assertNotEquals("", msg.toJson());
	}

}
