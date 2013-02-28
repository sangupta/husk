package com.sangupta.husk;

/**
 * Contract for all commands that need to participate in the
 * shell.
 * 
 * @author sangupta
 *
 */
public interface HuskShellCommand {

	/**
	 * Return the name of the command.
	 * 
	 * @return
	 */
	public String getName();
	
	/**
	 * Return the quick line help for this command.
	 * 
	 * @return
	 */
	public String getHelpLine();
	
	/**
	 * Execute the shell command for the given command-line
	 * arguments.
	 * 
	 * @param arguments
	 */
	public void execute(String[] arguments);
	
}
