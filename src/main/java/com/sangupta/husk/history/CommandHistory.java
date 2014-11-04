package com.sangupta.husk.history;

/**
 * Contract for all command history implementations.
 * 
 * @author sangupta
 *
 */
public interface CommandHistory {
	
	public int size();
	
	public boolean isEmpty();
	
	public int index();
	
	public void clear();
	
	public boolean add(String command);
	
	public String get(int index);
	
	public boolean remove(int index);
	
	public boolean removeFirst();
	
	public boolean removeLast();

	public String current();
	
	public int previous();
	
	public int next();
	
	public boolean moveToFirst();
	
	public boolean moveToLast();
	
	public boolean moveTo(int index);
	
}
