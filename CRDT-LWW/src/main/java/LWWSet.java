package main.java;

import java.sql.Timestamp;
import java.util.Map;

/**
 * Last-Writer-Wins Element Set class that handle CRDT data structure.
 * 
 * @author wzhang
 *
 * @param <T>
 */
public class LWWSet<T> {
	private Map<T, Timestamp> addSet;
	private Map<T, Timestamp> removeSet;
}
