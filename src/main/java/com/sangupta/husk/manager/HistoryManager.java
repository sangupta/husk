/**
 * husk - cli shell framework
 * Copyright (c) 2013-2016, Sandeep Gupta
 * 
 * http://sangupta/projects/husk
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

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
		if(index < 0) {
			return null;
		}
		
		return history.get(index);
	}

}