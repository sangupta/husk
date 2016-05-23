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

package com.sangupta.husk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.reflections.Reflections;

import com.sangupta.consoles.ConsoleType;
import com.sangupta.consoles.Consoles;
import com.sangupta.consoles.IConsole;
import com.sangupta.consoles.core.InputKey;
import com.sangupta.consoles.core.KeyTrapHandler;
import com.sangupta.consoles.core.SpecialInputKey;
import com.sangupta.husk.core.DefaultHuskShellContext;
import com.sangupta.husk.core.HuskShellContextAware;
import com.sangupta.husk.manager.HistoryManager;
import com.sangupta.jerry.print.ConsoleTable;

import javassist.Modifier;

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
	protected volatile boolean exitShellRequest = false;
	
	/**
	 * Stores an instance of all commands
	 */
	protected final Map<String, HuskShellCommand> COMMAND_MAP = new ConcurrentHashMap<String, HuskShellCommand>();
	
	/**
	 * 
	 */
	protected final HuskShellOptions shellOptions = new HuskShellOptions();
	
	/**
	 * The thread in which the command is executed
	 */
	protected Thread commandExecutorThread = null;
	
	/**
	 * Default constructor
	 */
	public HuskShell() {
		this(0, 0);
	}
	
	/**
	 * Create a new {@link HuskShell} with the given {@link ConsoleType}.
	 * 
	 * @param consoleType
	 */
	public HuskShell(final ConsoleType consoleType) {
		this(consoleType, 0, 0);
	}
	
	/**
	 * 
	 * Create an instance of the {@link HuskShell}.
	 * 
	 */
	public HuskShell(final int rows, final int columns) {
		this(ConsoleType.UI, rows, columns);
	}
	
	/**
	 * Create {@link HuskShell} instance with given parameters
	 * 
	 * 
	 */
	public HuskShell(final ConsoleType consoleType, final int rows, final int columns) {
		this.console = Consoles.getConsole(consoleType, rows, columns);
		
		// initialize command mapper
		HuskCommandMapper mapper = new HuskCommandMapper(COMMAND_MAP);
		
		// initialize the context
		this.shellContext = new DefaultHuskShellContext();
		
		DefaultHuskShellContext context = ((DefaultHuskShellContext) this.shellContext);
		context.setConsole(this.console);
		context.setCommandMapper(mapper);
		
		// add the shutdown hook
		this.console.addShutdownHook(new Runnable() {
			
			@Override
			public void run() {
				// clean up the console
				stop();
				
				// interrupt this thread
				exitShellRequest = true;
			}
		});
		
		this.console.switchStreams();
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
	 * Return the {@link IConsole} instance associated with this
	 * shell.
	 * 
	 * @return
	 */
	public IConsole getConsole() {
		return this.console;
	}

	/**
	 * Start the shell.
	 * 
	 */
	public void start() {
		addSpecialKeyHooks();
		
		do {
			String command = readCommandString();
			
			// check for shell exit
			if(this.exitShellRequest) {
				break;
			}
			
			// check for command history persistence
			HistoryManager.maintainCommandHistory(this.shellOptions, this.console.getConsoleID(), command);
			
			// extract the command and the arguments in a separate line
			String[] tokens = command.split("\\s");
			String[] arguments;
			if(tokens.length > 1) {
				command = tokens[0];
				arguments = Arrays.copyOfRange(tokens, 1, tokens.length);
			} else {
				arguments = new String[] { };
			}

			// check for shell exit
			checkForShellExitCommand(command);
			if(this.exitShellRequest) {
				break;
			}

			// check for help
			boolean helpShown = showHelpIfNeeded(command);
			if(helpShown) {
				continue;
			}
			
			// try and see which command we are trying to invoke
			boolean commandHandled = handleCommand(command, arguments);
			if(!commandHandled) {
				this.console.println("Unknown command!");
				this.console.print('\n');
			}
		} while(true);
	}

	/**
	 * Check if the command passed was a request to terminate the shell
	 * 
	 * @param command
	 *            the command name entered by the user
	 */
	private void checkForShellExitCommand(String command) {
		for(String exitName : this.exitCommandNames) {
			if(command.equalsIgnoreCase(exitName)) {
				this.exitShellRequest = true;
				return;
			}
		}
	}

	/**
	 * Handle the command if it is in our list of available commands.
	 * 
	 * @param command
	 *            the command to be invoked
	 * 
	 * @param arguments
	 *            the arguments to be passed
	 * 
	 * @return <code>true</code> if the command was handled, <code>false</code>
	 *         otherwise
	 */
	private boolean handleCommand(String command, String[] arguments) {
		if(!COMMAND_MAP.containsKey(command)) {
			return false;
		}
		
		HuskShellCommand shellCommand = COMMAND_MAP.get(command);
		
		if(shellCommand instanceof HuskShellContextAware) {
			((HuskShellContextAware) shellCommand).setShellContext(this.shellContext);
		}

		launchCommand(shellCommand, arguments);
		
		// output a single new line char
		this.console.print('\n');
		return true;
	}

	/**
	 * Show help to the user if needed.
	 * 
	 * @param command
	 *            the command name that was passed
	 * 
	 * @return <code>true</code> if help was shown, <code>false</code> otherwise
	 */
	private boolean showHelpIfNeeded(String command) {
		for(String helpName : this.helpCommandNames) {
			if(this.caseSensitiveCommands) {
				if(command.equals(helpName)) {
					showShellHelp();
					return true;
				}
			} else {
				if(command.equalsIgnoreCase(helpName)) {
					showShellHelp();
					return true;
				}
			}
		}
		
		return false;
	}

	/**
	 * Read the command string from the console as user input.
	 * 
	 * @return the command string entered.
	 * 
	 */
	private String readCommandString() {
		String command = null;
		
		while(command == null || command.equals("")) {
			if(this.promptProvider != null) {
				this.console.print(this.promptProvider.getPrompt());
			} else if(this.prompt != null) {
				this.console.print(this.prompt);
			}
			
			command = this.console.readLine().trim();
			
			if(this.exitShellRequest) {
				break;
			}
		}
		
		return command;
	}

	/**
	 * Launch the command in a separate thread and join it.
	 * 
	 * @param shellCommand
	 *            the shell command instance to be launched, cannot be
	 *            <code>null</code>
	 * 
	 * @param arguments
	 *            the arguments to be passed to the command
	 */
	private void launchCommand(final HuskShellCommand shellCommand, final String[] arguments) {
		commandExecutorThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					shellCommand.execute(arguments);
				} catch(Throwable t) {
					t.printStackTrace();
				}
			}
			
		});
		commandExecutorThread.setName("HuskShell-Command-Executor");
		commandExecutorThread.start();
		
		try {
			commandExecutorThread.join();
		} catch(InterruptedException e) {
			commandExecutorThread.interrupt();
		}
	}

	/**
	 * Add special key hooks like up arrow key and down arrow key.
	 * 
	 */
	private void addSpecialKeyHooks() {
		// add Ctrl+C hook
		this.console.addPriorityKeyTrap(new InputKey('c', false, true), new KeyTrapHandler() {
			
			@Override
			public boolean handleKeyInvocation(InputKey key) {
				System.out.println("^C");
				if(commandExecutorThread == null) {
					return false;
				}
				
				if(commandExecutorThread.isAlive()) {
					commandExecutorThread.interrupt();
				}
				
				return false;
			}
			
		});

		// for up arrow
		this.console.addKeyTrap(new InputKey(SpecialInputKey.UpArrow), new KeyTrapHandler() {
			
			@Override
			public boolean handleKeyInvocation(InputKey key) {
				String command = HistoryManager.previous(console.getConsoleID());
				if(command == null) {
					return false;
				}
				
				System.out.println(command);
				return false;
			}
		});
	}

	/**
	 * Display the shell help message. This help message basically list down all
	 * commands that are available to the shell, with their basic description. Displaying
	 * detailed help messages is the responsibility of the individual command.
	 * 
	 */
	public void showShellHelp() {
		if(COMMAND_MAP == null || COMMAND_MAP.isEmpty()) {
			return;
		}

		// sort the command names
		List<String> names = new ArrayList<String>(COMMAND_MAP.keySet());
		Collections.sort(names);
		
		ConsoleTable table = new ConsoleTable();
		table.addHeaderRow("Command", "Description");
		
		// show the list
		for(String name : names) {
			HuskShellCommand command = COMMAND_MAP.get(name);
			String helpLine = command.getHelpLine();

			table.addRow(name, helpLine);
		}
		
		this.console.print(table.toString());
	}

	/**
	 * Set the window title for this shell, if possible. This will not reset 
	 * the window title if <code>null</code> is passed as a parameter.
	 * 
	 */
	public void setTitle(String title) {
		if(title == null) {
			return;
		}
		
		this.console.setWindowTitle(title);
	}

	/**
	 * Dispose of this shell instance. This will initiate the shutdown of the attached
	 * console instance and will release all resources as attached to the instance.
	 * 
	 */
	public void stop() {
		// close the console
		this.console.shutdown();
	}
	
	/**
	 * Add a new {@link HuskShellCommand} dynamically to the runnning shell
	 * instance.
	 * 
	 * @param command
	 */
	public boolean addCommand(HuskShellCommand command) {
		if(command == null) {
			return false;
		}
		
		return addCommandInternal(command);
	}
	
	/**
	 * The method helps load all commands that can be found inside the given package name.
	 * An object class is considered implementing a command if it implements the {@link HuskShellCommand}
	 * interface.
	 * 
	 * @param packageName
	 */
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
				
				addCommandInternal(command);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean addCommandInternal(HuskShellCommand command) {
		if(command == null) {
			return false;
		}
		
		String name = command.getName();
		if(name == null || name.trim().isEmpty()) {
			// skip this command
			// TODO: log as an error
			return false;
		}
		
		// TODO: log clash
		if(!COMMAND_MAP.containsKey(name.trim())) {
			COMMAND_MAP.put(name.trim(), command);
		}
		
		// check for command aliases
		String[] alias = command.getNameAlias();
		if(alias != null && alias.length > 0) {
			for(String newName : alias) {
				if(newName != null) {
					// TODO: log clash
					if(!COMMAND_MAP.containsKey(newName.trim())) {
						COMMAND_MAP.put(newName.trim(), command);
					}
				}
			}
		}
		
		return true;
	}
	
}