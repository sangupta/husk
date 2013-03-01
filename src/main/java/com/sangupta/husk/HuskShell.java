package com.sangupta.husk;

import java.util.Set;

import org.reflections.Reflections;

import com.sangupta.consoles.ConsoleType;
import com.sangupta.consoles.Consoles;
import com.sangupta.consoles.IConsole;

/**
 * Contract for all shell implementations over which powerful
 * command-line tools can be built.
 *  
 * @author sangupta
 *
 */
public class HuskShell extends AbstractShell {
	
	/**
	 * The console implementation to be used.
	 */
	protected final IConsole console;
	
	/**
	 * Defines whether the commands that are read in the shell should
	 * be treated case-sensitive or not.
	 * 
	 */
	protected boolean caseSensitiveCommands = false;
	
	/**
	 * Indicates whether the shell needs to exit.
	 * 
	 */
	protected boolean exitShellRequest = false;
	
	/**
	 * Create an instance of the {@link HuskShell}.
	 * 
	 */
	public HuskShell() {
		this.console = Consoles.getConsole(ConsoleType.UI);
	}
	
	/**
	 * Initialize the shell and load all commands that can
	 * be found in the classpath.
	 * 
	 */
	public void initialize() {
		Reflections reflections = new Reflections("com.sangupta.husk.commands");
		Set<Class<? extends HuskShellCommand>> commands = reflections.getSubTypesOf(HuskShellCommand.class);
		
		if(commands == null) {
			return;
		}
		
		for(Class<? extends HuskShellCommand> command : commands) {
			
		}
	}

	/**
	 * Load commands from an additional package with the given 
	 * qualified name prefix.
	 * 
	 * @param packageName
	 */
	public void loadExternalCommands(String packageName) {
		
	}

	/**
	 * Start the shell.
	 * 
	 */
	public void start() {
		do {
			String command = null;
			while(command == null || command.trim().equals("")) {
				this.console.print(this.prompt);
				command = this.console.readLine();
			}
			
			for(String exitName : this.exitCommandNames) {
				if(command.equalsIgnoreCase(exitName)) {
					this.exitShellRequest = true;
					break;
				}
			}
			
			if(this.exitShellRequest) {
				break;
			}
			
			for(String helpName : this.helpCommandNames) {
				if(this.caseSensitiveCommands) {
					if(command.equals(helpName)) {
						showShellHelp();
						continue;
					}
				} else {
					if(command.equalsIgnoreCase(helpName)) {
						showShellHelp();
						continue;
					}
				}
			}
			
		} while(true);
	}
	
	/**
	 * Display the shell help message.
	 * 
	 */
	public void showShellHelp() {
		
	}

	/**
	 * Set the window title for this shell, if possible.
	 * 
	 */
	public void setTitle(String title) {
		if(title == null) {
			return;
		}
		
		this.console.setWindowTitle(title);
	}

	/**
	 * Dispose of this shell instance.
	 * 
	 */
	public void stop() {
		this.console.shutdown();
	}
	
}
