package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.Test;

import main.java.LWWSet;

public class LWWAddRemoveTest {

	@Test
	public void testLWWSetAddWithTimeArgument() {
		var lwwSet = new LWWSet<String>();

		// time3 before time1 and time1 before time2
		var time1 = new Date();
		var time2 = new Date();
		var time3 = new Date(time1.getTime() - 1);

		// tests
		assertTrue(lwwSet.getAddSet().isEmpty());
		assertEquals("Apple is entered as a new entry", new Timestamp(time1.getTime()), lwwSet.Add("Apple", time1));
		assertEquals("Banana is entered as a new entry", new Timestamp(time2.getTime()), lwwSet.Add("Banana", time2));
		assertEquals("Apple is updated with latest time stamp time2", new Timestamp(time2.getTime()),
				lwwSet.Add("Apple", time2));
		assertEquals("Apple is NOT updated due to time3 is before stored time stamp", new Timestamp(time2.getTime()),
				lwwSet.Add("Apple", time3));

		assertEquals("There are 2 entries", 2, lwwSet.getAddSet().size());
	}

	@Test
	public void testLWWSetAdd() {
		var lwwSet = new LWWSet<String>();

		// tests
		assertTrue(lwwSet.getAddSet().isEmpty());
		Timestamp time1 = lwwSet.Add("Apple");
		Timestamp time2 = lwwSet.Add("Apple");
		assertFalse("Apple latest time was updated", time1.after(time2));
	}

	@Test
	public void testLWWSetRemoveWithTimeArgument() {
		var lwwSet = new LWWSet<String>();

		// time3 before time1 and time1 before time2
		var time1 = new Date();
		var time2 = new Date();
		var time3 = new Date(time1.getTime() - 1);

		// tests
		assertTrue(lwwSet.getRemoveSet().isEmpty());
		assertEquals("Apple is entered as a new entry", new Timestamp(time1.getTime()), lwwSet.Remove("Apple", time1));
		assertEquals("Banana is entered as a new entry", new Timestamp(time2.getTime()),
				lwwSet.Remove("Banana", time2));
		assertEquals("Apple is updated with latest time stamp time2", new Timestamp(time2.getTime()),
				lwwSet.Remove("Apple", time2));
		assertEquals("Apple is NOT updated due to time3 is before stored time stamp", new Timestamp(time2.getTime()),
				lwwSet.Remove("Apple", time3));

		assertEquals("There are 2 entries", 2, lwwSet.getRemoveSet().size());
	}

	@Test
	public void testLWWSetRemove() {
		var lwwSet = new LWWSet<String>();

		// tests
		assertTrue(lwwSet.getRemoveSet().isEmpty());
		Timestamp time1 = lwwSet.Remove("Apple");
		Timestamp time2 = lwwSet.Remove("Apple");
		assertFalse("Apple latest time was updated", time1.after(time2));
	}
}
