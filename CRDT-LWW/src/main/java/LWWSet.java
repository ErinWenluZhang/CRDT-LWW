package main.java;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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

	/**
	 * Construct a new LWWSet and initialize addSet and removeSet.
	 */
	public LWWSet() {
		addSet = Collections.synchronizedMap(new HashMap<>());
		removeSet = Collections.synchronizedMap(new HashMap<>());
	}

	/**
	 * Add an element to LWWSet and record current time stamp.
	 *
	 * @param element The element to be added.
	 * @return Timestamp The latest time stamp
	 */
	public Timestamp Add(T element) {
		return Add(element, new Date());
	}

	/**
	 * Remove an element from LWWSet and record current time stamp.
	 *
	 * @param element The element to be removed.
	 * @return Timestamp The latest time stamp
	 */
	public Timestamp Remove(T element) {
		return Remove(element, new Date());
	}

	/**
	 * Add an element to LWWSet and record the time stamp.
	 *
	 * @param element The element to be added.
	 * @param time    The date time of the add action.
	 * @return Timestamp The latest time stamp
	 */
	public Timestamp Add(T element, Date time) {
		return put(addSet, element, time);
	}

	/**
	 * Remove an element from LWWSet and record the time stamp.
	 *
	 * @param element The element to be removed.
	 * @param time    The date time of the remove action.
	 * @return Timestamp The latest time stamp
	 */
	public Timestamp Remove(T element, Date time) {
		return put(removeSet, element, time);
	}

	/**
	 * Add an element to synchronizedMap with latest time stamp as value.
	 *
	 * @param map     The synchronizedMap to be modified.
	 * @param element The element to be added.
	 * @param time    The date time of the add action.
	 * @return Timestamp The latest time stamp
	 */
	private Timestamp put(Map<T, Timestamp> map, T element, Date time) {
		var lastTimestamp = map.get(element);
		if (lastTimestamp != null && lastTimestamp.after(time)) {
			return lastTimestamp;
		}
		lastTimestamp = new Timestamp(time.getTime());
		map.put(element, lastTimestamp);
		return lastTimestamp;
	}

	/**
	 * Get addSet For testing purpose only
	 * 
	 * @return
	 */
	public Map<T, Timestamp> getAddSet() {
		return this.addSet;
	}

	/**
	 * Get remvoeSet For testing purpose only
	 * 
	 * @return
	 */
	public Map<T, Timestamp> getRemoveSet() {
		return this.removeSet;
	}
}
