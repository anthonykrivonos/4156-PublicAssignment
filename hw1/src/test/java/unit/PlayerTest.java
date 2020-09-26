package unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import models.Player;

public class PlayerTest {
	
	/**
	 * Constructors
	 */

	@Test
	public void testPlayerConstructor() {
		final char type = 'X';
		final int id = 1;
		final Player player = new Player(type, id);
		
		Assertions.assertEquals(type, player.getType());
		Assertions.assertEquals(id, player.getId());
	}
	
	/**
	 * Setters
	 */
	
	@Test
	public void testSetType() {
		final Player player = new Player('X', 1);
		
		final char type = 'O';
		player.setType(type);

		Assertions.assertEquals(type, player.getType());
	}
	
	@Test
	public void testSetId() {
		final Player player = new Player('X', 1);
		
		final int id = 2;
		player.setId(id);

		Assertions.assertEquals(id, player.getId());
	}
	
}
