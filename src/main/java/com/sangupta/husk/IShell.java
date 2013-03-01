package com.sangupta.husk;

/**
 * Contract for all shell implementations.
 * 
 * @author sangupta
 *
 */
public interface IShell {
	
	public void setPrompt(String prompt);
	
	public void setTitle(String title);
	
	public void setMaskCharacter(char mask);
	
	public void setExitCommandNames(String exitName);
	
	public void setExitCommandNames(String[] exitNames);
	
	public void setHelpCommandNames(String helpName);
	
	public void setHelpCommandNames(String[] helpNames);

}
