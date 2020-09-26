package unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import models.Move;
import models.Player;

public class MoveTest {
	
	/**
	 * Constructors
	 */

	@Test
	public void testMoveConstructor() {
		final Player p1 = new Player('X', 1);
		final int x = 0;
		final int y = 0;
		final Move move = new Move(p1, x, y);
		
		Assertions.assertEquals(p1.getId(), move.getPlayer().getId());
		Assertions.assertEquals(p1.getType(), move.getPlayer().getType());
		Assertions.assertEquals(x, move.getMoveX());
		Assertions.assertEquals(y, move.getMoveY());
	}
	
	/**
	 * Setters
	 */
	
	@Test
	public void testSetPlayer() {
		final Move move = new Move(new Player('X', 1), 0, 0);
		
		final Player p2 = new Player('O', 2);
		move.setPlayer(p2);

		Assertions.assertEquals(p2.getId(), move.getPlayer().getId());
		Assertions.assertEquals(p2.getType(), move.getPlayer().getType());
	}
	
	@Test
	public void testSetMoveX() {
		final Move move = new Move(new Player('X', 1), 0, 0);
		
		final int x = 1;
		move.setMoveX(x);
		
		Assertions.assertEquals(x, move.getMoveX());
	}
	
	@Test
	public void testSetMoveY() {
		final Move move = new Move(new Player('X', 1), 0, 0);
		
		final int y = 1;
		move.setMoveY(y);
		
		Assertions.assertEquals(y, move.getMoveY());
	}

}
