package com.sangupta.husk.history;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link CommandHistory} that stores everything in memory.
 * 
 * @author sangupta
 *
 */
public class MemoryCommandHistory implements CommandHistory {
	
	private final List<String> commands = new ArrayList<String>();
	
	private volatile int currentIndex;

	@Override
	public int size() {
		return this.commands.size();
	}

	@Override
	public boolean isEmpty() {
		return this.commands.isEmpty();
	}

	@Override
	public int index() {
		return this.currentIndex;
	}

	@Override
	public void clear() {
		this.commands.clear();
		this.currentIndex = 0;
	}

	@Override
	public boolean add(String command) {
		if(command == null || command.length() == 0) {
			return false;
		}
		
		this.commands.add(command);
		this.currentIndex = this.commands.size() - 1; 
		return true;
	}

	@Override
	public String get(int index) {
		return this.commands.get(index);
	}

	@Override
	public boolean remove(int index) {
		this.commands.remove(index);
		if(index < this.currentIndex) {
			this.currentIndex--;
		}
		
		return true;
	}

	@Override
	public boolean removeFirst() {
		this.commands.remove(0);
		this.currentIndex--;
		return true;
	}

	@Override
	public boolean removeLast() {
		this.commands.remove(this.commands.size() - 1);
		this.currentIndex--;
		return true;
	}

	@Override
	public String current() {
		return null;
	}

	@Override
	public int previous() {
		return (--this.currentIndex); 
	}

	@Override
	public int next() {
		this.currentIndex++;
		if(this.currentIndex >= this.commands.size()) {
			this.currentIndex = this.commands.size() - 1;
		}
		
		return this.currentIndex;
	}

	@Override
	public boolean moveToFirst() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveToLast() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean moveTo(int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
