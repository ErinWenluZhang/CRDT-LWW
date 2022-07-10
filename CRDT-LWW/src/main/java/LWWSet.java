package main.java;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	 * Verify if an element exists in LWWSet.
	 *
	 * @param element The element to be verified.
	 * @return boolean true if the element exists.
	 */
	public boolean Exists(T element) {
		var addTime = addSet.get(element);
		var removeTime = removeSet.get(element);

		if (addTime == null) {
			return false;
		}
		if (removeTime == null) {
			return true;
		}

		return addTime.after(removeTime);
	}

	/**
	 * Generate a collection with all existing elements
	 *
	 * @return Returns a List with all elements exists.
	 */
	public List<T> Get() {
		return addSet.keySet().stream().filter(e -> Exists(e)).collect(Collectors.toList());
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
