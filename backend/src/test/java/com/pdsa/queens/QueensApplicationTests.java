package com.pdsa.queens;

import com.pdsa.backend.queens.algorithm.SequentialQueensSolver;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class QueensApplicationTests {

	private final SequentialQueensSolver solver = new SequentialQueensSolver();

	// Test 1: Every solution must have exactly 16 queens
	@Test
	void eachSolutionHas16Queens() {
		List<int[]> solutions = solver.findAllSolutions();
		for (int[] solution : solutions) {
			assertEquals(16, solution.length);
		}
	}

	// Test 2: No solution should have two queens in the same row
	@Test
	void noTwoQueensInSameRow() {
		List<int[]> solutions = solver.findAllSolutions();
		for (int[] sol : solutions) {
			for (int c1 = 0; c1 < 16; c1++) {
				for (int c2 = c1 + 1; c2 < 16; c2++) {
					assertNotEquals(sol[c1], sol[c2], "Two queens share same row");
				}
			}
		}
	}

	// Test 3: No solution should have two queens on same diagonal
	@Test
	void noTwoQueensOnSameDiagonal() {
		List<int[]> solutions = solver.findAllSolutions();
		for (int[] sol : solutions) {
			for (int c1 = 0; c1 < 16; c1++) {
				for (int c2 = c1 + 1; c2 < 16; c2++) {
					assertNotEquals(
							Math.abs(sol[c1] - sol[c2]),
							Math.abs(c1 - c2),
							"Two queens share same diagonal"
					);
				}
			}
		}
	}

	// Test 4: Solver must find at least one solution
	@Test
	void solverFindsAtLeastOneSolution() {
		List<int[]> solutions = solver.findAllSolutions();
		assertFalse(solutions.isEmpty(), "Should find at least one solution");
	}
}