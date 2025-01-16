package org.ajaxer.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.ajaxer.service.StarterService.isMinuteMatched;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Shakir Ansari
 * @since 2025-01-11
 */

public class TimeCheckerTest
{
	@Test
	void testSavedMinute0_withCurrentMinuteInFirstQuarter()
	{
		assertTrue(isMinuteMatched(12, 0));  // 0 <= 12 < 15
		assertTrue(isMinuteMatched(0, 0));   // 0 <= 0 < 15
		assertFalse(isMinuteMatched(16, 0)); // 16 >= 15
		assertFalse(isMinuteMatched(26, 0));
		assertFalse(isMinuteMatched(46, 0));
	}

	@Test
	void testSavedMinute15_withCurrentMinuteInSecondQuarter()
	{
		assertTrue(isMinuteMatched(18, 15)); // 15 <= 18 < 30
		assertTrue(isMinuteMatched(29, 15)); // 15 <= 29 < 30
		assertFalse(isMinuteMatched(30, 15)); // 30 >= 30
		assertFalse(isMinuteMatched(13, 15));
		assertFalse(isMinuteMatched(2, 15));
	}

	@Test
	void testSavedMinute30_withCurrentMinuteInThirdQuarter()
	{
		assertTrue(isMinuteMatched(33, 30)); // 30 <= 33 < 45
		assertTrue(isMinuteMatched(44, 30)); // 30 <= 44 < 45
		assertFalse(isMinuteMatched(45, 30)); // 45 >= 45
		assertFalse(isMinuteMatched(2, 30));
		assertFalse(isMinuteMatched(13, 30));
		assertFalse(isMinuteMatched(55, 30));
	}

	@Test
	void testSavedMinute45_withCurrentMinuteInFourthQuarter()
	{
		assertTrue(isMinuteMatched(45, 45)); // 45 <= 45 < 60
		assertTrue(isMinuteMatched(59, 45)); // 45 <= 59 < 60
		assertFalse(isMinuteMatched(44, 45)); // 44 < 45
		assertFalse(isMinuteMatched(2, 45));
		assertFalse(isMinuteMatched(13, 45));
	}

	@Test
	void testDifferentSavedMinuteValues()
	{
		assertTrue(isMinuteMatched(10, 0));   // 0 <= 10 < 15
		assertTrue(isMinuteMatched(20, 15));  // 15 <= 20 < 30
		assertTrue(isMinuteMatched(35, 30));  // 30 <= 35 < 45
		assertTrue(isMinuteMatched(50, 45));  // 45 <= 50 < 60
		assertFalse(isMinuteMatched(30, 0));  // 30 >= 15
		assertFalse(isMinuteMatched(12, 30)); // 12 < 30
		assertFalse(isMinuteMatched(50, 15)); // 50 >= 30
	}

	@Nested
	class QuarterTests
	{
		@Test
		void testGetQuarter__ValidInputs()
		{
			// First Quarter
			assertEquals(0, StarterService.getQuarter(0));
			assertEquals(0, StarterService.getQuarter(14));

			// Second Quarter
			assertEquals(1, StarterService.getQuarter(15));
			assertEquals(1, StarterService.getQuarter(29));

			// Third Quarter
			assertEquals(2, StarterService.getQuarter(30));
			assertEquals(2, StarterService.getQuarter(44));

			// Fourth Quarter
			assertEquals(3, StarterService.getQuarter(45));
			assertEquals(3, StarterService.getQuarter(59));
		}

		@Test
		void testGetQuarter__InvalidInputs()
		{
			// Invalid inputs should default to 0 (First Quarter)
			assertEquals(0, StarterService.getQuarter(-1));
			assertEquals(0, StarterService.getQuarter(60));
			assertEquals(0, StarterService.getQuarter(100));
		}
	}
}
