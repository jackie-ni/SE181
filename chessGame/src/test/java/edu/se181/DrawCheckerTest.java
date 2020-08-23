package edu.se181;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DrawCheckerTest {
	private DrawChecker drawChecker;
	private Board board;

	@Before
	public void beforeEach() {
		drawChecker = new DrawChecker();
		board = new Board();
	}

	@Test
	public void isDeadPosition_GivenKingVsKing_returnsTrue(){
		board.loadBoard("00000000000k0000000000000000000000000000000000000K00000000000000000000");
		// https://lichess.org/editor/5k2/8/8/8/8/3K4/8/8_b_-_-_0_1

		boolean result = drawChecker.isDeadPosition(board);

		Assert.assertTrue(result);
	}

	@Test
	public void isDeadPosition_GivenKingRookVsKing_returnsFalse(){
		board.loadBoard("00000000000k000r000000000000000000000000000000000K00000000000000000000");
		// https://lichess.org/editor/5k2/1r6/8/8/8/3K4/8/8_b_-_-_0_1

		boolean result = drawChecker.isDeadPosition(board);

		Assert.assertFalse(result);
	}

	@Test
	public void isDeadPosition_GivenKingKnightVsKing_returnsTrue(){
		board.loadBoard("00000000000k000n000000000000000000000000000000000K00000000000000000000");
		// https://lichess.org/editor/5k2/1n6/8/8/8/3K4/8/8_b_-_-_0_1

		boolean result = drawChecker.isDeadPosition(board);

		Assert.assertTrue(result);
	}

	@Test
	public void isDeadPosition_GivenKingBishopVsKing_returnsTrue(){
		board.loadBoard("00000000000k000b000000000000000000000000000000000K00000000000000000000");
		// https://lichess.org/editor/5k2/1b6/8/8/8/3K4/8/8_b_-_-_0_1

		boolean result = drawChecker.isDeadPosition(board);

		Assert.assertTrue(result);
	}

	@Test
	public void isDeadPosition_GivenKingVsKingKnight_returnsTrue(){
		board.loadBoard("00000000000k000N000000000000000000000000000000000K00000000000000000000");
		// https://lichess.org/editor/5k2/1N6/8/8/8/3K4/8/8_b_-_-_0_1

		boolean result = drawChecker.isDeadPosition(board);

		Assert.assertTrue(result);
	}

	@Test
	public void isDeadPosition_GivenKingVsKingBishop_returnsTrue(){
		board.loadBoard("00000000000k000B000000000000000000000000000000000K00000000000000000000");
		// https://lichess.org/editor/5k2/1B6/8/8/8/3K4/8/8_b_-_-_0_1

		boolean result = drawChecker.isDeadPosition(board);

		Assert.assertTrue(result);
	}

	@Test
	public void isDeadPosition_GivenWhiteMoreThan2Pieces_returnsFalse(){
		board.loadBoard("00000000000k000BB00000000000000000000000000000000K00000000000000000000");
		// https://lichess.org/editor/5k2/1BB5/8/8/8/3K4/8/8_b_-_-_0_1

		boolean result = drawChecker.isDeadPosition(board);

		Assert.assertFalse(result);
	}

	@Test
	public void isDeadPosition_GivenBlackMoreThan2Pieces_returnsFalse(){
		board.loadBoard("00000000000k000bb00000000000000000000000000000000K00000000000000000000");
		// https://lichess.org/editor/5k2/1bb5/8/8/8/3K4/8/8_b_-_-_0_1

		boolean result = drawChecker.isDeadPosition(board);

		Assert.assertFalse(result);
	}

	@Test
	public void isDeadPosition_GivenKingBishopVsKingBishopSameColors_returnsTrue(){
		board.loadBoard("00000000000k000b0B0000000000000000000000000000000K00000000000000000000");
		// https://lichess.org/editor/5k2/1b1B4/8/8/8/3K4/8/8_b_-_-_0_1

		boolean result = drawChecker.isDeadPosition(board);

		Assert.assertTrue(result);
	}

	@Test
	public void isDeadPosition_GivenKingBishopVsKingBishopDifferentColors_returnsFalse(){
		board.loadBoard("00000000000k000bB00000000000000000000000000000000K00000000000000000000");
		// https://lichess.org/editor/5k2/1bB5/8/8/8/3K4/8/8_b_-_-_0_1

		boolean result = drawChecker.isDeadPosition(board);

		Assert.assertTrue(result);
	}

}