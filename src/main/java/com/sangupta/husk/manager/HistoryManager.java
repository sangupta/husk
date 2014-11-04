package com.sangupta.husk.manager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.sangupta.husk.HuskShellOptions;
import com.sangupta.husk.history.CommandHistory;
import com.sangupta.husk.history.MemoryCommandHistory;

/**
 * Manages history items as and when they come in.
 * 
 * @author sangupta
 *
 */
public class HistoryManager {
	
	private static final ConcurrentMap<String, CommandHistory> histories = new ConcurrentHashMap<String, CommandHistory>();
	
	private static final MemoryCommandHistory MERGED = new MemoryCommandHistory();
	
	static {
		histories.put("merged", MERGED);
	}

	/**
	 * Maintain command history based on the current options.
	 * 
	 * @param command
	 */
	public static void maintainCommandHistory(HuskShellOptions shellOptions, String consoleID, String command) {
		if(!shellOptions.maintainCommandHistory) {
			return;
		}
		
		// add the command to required History object
		CommandHistory history = histories.get(consoleID);
		if(history == null) {
			history = new MemoryCommandHistory();
			CommandHistory olderHistory = histories.putIfAbsent(consoleID, history);
			if(olderHistory != null) {
				history = olderHistory;
			}
		}
		
		history.add(command);
		
		// add to the merged list
		if(!shellOptions.mergeCommandHistory) {
			return;
		}
		
		MERGED.add(command);
	}

	public static String previous(String consoleID) {
		if(consoleID == null) {
			return null;
		}
		
		CommandHistory history = histories.get(consoleID);
		if(history == null) {
			return null;
		}

		int index = history.previous();
		return history.get(index);
	}

}
