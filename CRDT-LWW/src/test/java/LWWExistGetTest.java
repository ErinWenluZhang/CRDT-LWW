package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import main.java.LWWSet;

public class LWWExistGetTest {

	private LWWSet<String> lwwSet;

	@Before
	public void setUp() {
		lwwSet = new LWWSet<String>();
		lwwSet.Add("Alan");
		lwwSet.Add("Emily");
		lwwSet.Add("Hilton");
		lwwSet.Add("Patrice");
		lwwSet.Remove("Emily");
	}

	@Test
	public void testExists() {
		assertTrue("Alan was added and never removed", lwwSet.Exists("Alan"));
		assertFalse("Emily was added and removed after", lwwSet.Exists("Emily"));
		assertTrue("Hilton was added and never removed", lwwSet.Exists("Hilton"));
		assertTrue("Patrice was added and never removed", lwwSet.Exists("Patrice"));
		assertFalse("Wenlu was never added", lwwSet.Exists("Wenlu"));
	}

	@Test
	public void testGet() {

		var existElements = lwwSet.Get();
		assertEquals("There are 3 elements in Set", 3, existElements.size());
		assertEquals("[Alan, Hilton, Patrice]",
				existElements.stream().sorted().collect(Collectors.toList()).toString());
	}

	@Test
	public void testWithConcurrency() throws InterruptedException {
		int numberOfThreads = 10;
		ExecutorService service = Executors.newFixedThreadPool(10);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		for (int i = 0; i < numberOfThreads; i++) {
			service.submit(() -> {
				assertTrue("Alan was added and never removed", lwwSet.Exists("Alan"));
				assertFalse("Emily was added and removed after", lwwSet.Exists("Emily"));
				assertTrue("Hilton was added and never removed", lwwSet.Exists("Hilton"));
				assertTrue("Patrice was added and never removed", lwwSet.Exists("Patrice"));
				assertFalse("Wenlu was never added", lwwSet.Exists("Wenlu"));

				var existElements = lwwSet.Get();
				assertEquals("There are 3 elements in Set", 3, existElements.size());
				assertEquals("[Alan, Hilton, Patrice]",
						existElements.stream().sorted().collect(Collectors.toList()).toString());

				latch.countDown();
			});
		}
		latch.await();
	}
}
