package com.sangupta.husk;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javassist.Modifier;

import org.reflections.Reflections;

import com.sangupta.consoles.ConsoleType;
import com.sangupta.consoles.Consoles;
import com.sangupta.consoles.IConsole;
import com.sangupta.husk.core.HuskShellContextAware;
import com.sangupta.husk.util.HuskUtils;

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
	 * Stores an instance of all commands
	 */
	protected final Map<String, HuskShellCommand> COMMAND_MAP = new ConcurrentHashMap<String, HuskShellCommand>();
	
	/**
	 * Create an instance of the {@link HuskShell}.
	 * 
	 */
	public HuskShell() {
		this.console = Consoles.getConsole(ConsoleType.UI);
		
		// now initialize the System.in and System.out streams as well
		System.setIn(this.console.getInputStream());
		System.setOut(new PrintStream(this.console.getOutputStream()));
		System.setErr(System.out);
	}
	
	/**
	 * Initialize the shell and load all commands that can
	 * be found in the classpath.
	 * 
	 */
	public void initialize() {
		loadCommandsFromPackage("com.sangupta.husk.commands");
	}

	/**
	 * Load commands from an additional package with the given 
	 * qualified name prefix.
	 * 
	 * @param packageName
	 */
	public void loadExternalCommands(String packageName) {
		if(packageName == null || packageName.trim().isEmpty()) {
			throw new IllegalArgumentException("Package name from which commands are to be loaded cannot be null/empty");
		}
		
		loadCommandsFromPackage(packageName);
	}

	/**
	 * Start the shell.
	 * 
	 */
	public void start() {
		do {
			String command = null;
			
			while(command == null || command.equals("")) {
				
				if(this.promptProvider != null) {
					this.console.print(this.promptProvider.getPrompt());
				} else if(this.prompt != null) {
					this.console.print(this.prompt);
				}
				
				command = this.console.readLine().trim();
			}
			
			// extract the command and the arguments in a separate line
			String[] tokens = command.split("\\s");
			String[] arguments;
			if(tokens.length > 1) {
				command = tokens[0];
				arguments = Arrays.copyOfRange(tokens, 1, tokens.length);
			} else {
				arguments = new String[] { };
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
			
			boolean helpShown = false;
			for(String helpName : this.helpCommandNames) {
				if(this.caseSensitiveCommands) {
					if(command.equals(helpName)) {
						showShellHelp();
						helpShown = true;
						continue;
					}
				} else {
					if(command.equalsIgnoreCase(helpName)) {
						showShellHelp();
						helpShown = true;
						continue;
					}
				}
			}
			
			if(helpShown) {
				continue;
			}
			
			// try and see which command we are trying to invoke
			if(COMMAND_MAP.containsKey(command)) {
				HuskShellCommand shellCommand = COMMAND_MAP.get(command);
				
				if(shellCommand instanceof HuskShellContextAware) {
					((HuskShellContextAware) shellCommand).setShellContext(this.shellContext);
				}

				try {
					shellCommand.execute(arguments);
				} catch(Throwable t) {
					t.printStackTrace();
				}
				
				continue;
			}
			
			System.out.println("Unknown command!");
		} while(true);
	}
	
	/**
	 * Display the shell help message.
	 * 
	 */
	public void showShellHelp() {
		if(COMMAND_MAP == null || COMMAND_MAP.isEmpty()) {
			return;
		}
		
		// find max tool name length
		int max = 0;
		Set<String> commandNames = COMMAND_MAP.keySet();
		for(String name : commandNames) {
			if(max < name.length()) {
				max = name.length();
			}
		}
		
		max += 3;

		for(String name : commandNames) {
			HuskShellCommand command = COMMAND_MAP.get(name);
			String helpLine = command.getHelpLine();

			System.out.print(HuskUtils.rightPad(name, max, ' '));
			System.out.println(helpLine);
		}
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
	
	protected void loadCommandsFromPackage(String packageName) {
		Reflections reflections = new Reflections(packageName);
		Set<Class<? extends HuskShellCommand>> commands = reflections.getSubTypesOf(HuskShellCommand.class);
		
		if(commands == null) {
			return;
		}
		
		// TODO: store commands in a way that we don't have to initialize them
		// again and again
		for(Class<? extends HuskShellCommand> clazz : commands) {
			HuskShellCommand command;
			try {
				if(Modifier.isAbstract(clazz.getModifiers())) {
					// no need to instantiate abstract classes
					continue;
				}
				
				command = clazz.newInstance();
				
				if(command != null) {
					String name = command.getName();
					if(name == null || name.trim().isEmpty()) {
						// skip this command
						// TODO: log as an error
						continue;
					}
					
					COMMAND_MAP.put(name.trim(), command);
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
}
