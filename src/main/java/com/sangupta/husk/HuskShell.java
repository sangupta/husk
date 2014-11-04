/**
 *
 * Husk - Helps build command line shells
 * Copyright (c) 2013, Sandeep Gupta
 * 
 * http://www.sangupta/projects/husk
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

import javassist.Modifier;

import org.reflections.Reflections;

import com.sangupta.consoles.ConsoleType;
import com.sangupta.consoles.Consoles;
import com.sangupta.consoles.IConsole;
import com.sangupta.consoles.core.InputKey;
import com.sangupta.consoles.core.KeyTrapHandler;
import com.sangupta.consoles.core.SpecialInputKey;
import com.sangupta.husk.core.DefaultHuskShellContext;
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
	 * Default constructor
	 */
	public HuskShell() {
		this(0, 0);
	}
	
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
	
	public HuskShell(final ConsoleType consoleType, final int rows, final int columns) {
		this.console = Consoles.getConsole(consoleType, rows, columns);
		this.shellContext = new DefaultHuskShellContext();
		((DefaultHuskShellContext) this.shellContext).setConsole(this.console);
		
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

				launchCommand(shellCommand, arguments);
				
				// output a single new line char
				this.console.print('\n');
				
				continue;
			}
			
			this.console.println("Unknown command!");
			this.console.print('\n');
		} while(true);
	}

	/**
	 * Launch the command in a separate thread and join it.
	 * 
	 * @param shellCommand
	 * @param arguments
	 */
	private void launchCommand(final HuskShellCommand shellCommand, final String[] arguments) {
		Thread childThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					shellCommand.execute(arguments);
				} catch(Throwable t) {
					t.printStackTrace();
				}
			}
			
		});
		
		childThread.start();
		
		try {
			childThread.join();
		} catch(InterruptedException e) {
			childThread.interrupt();
		}
		
	}

	/**
	 * Add special key hooks like up arrow key and down arrow key.
	 * 
	 */
	private void addSpecialKeyHooks() {
		this.console.addKeyTrap(new InputKey(SpecialInputKey.UpArrow), new KeyTrapHandler() {
			
			@Override
			public boolean handleKeyInvocation(InputKey key) {
				System.out.println("Handled");
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
		
		// find max tool name length
		int max = 0;
		Set<String> commandNames = COMMAND_MAP.keySet();
		for(String name : commandNames) {
			if(max < name.length()) {
				max = name.length();
			}
		}
		
		max += 3;
		
		// sort the command names
		List<String> names = new ArrayList<String>(commandNames);
		Collections.sort(names);
		
		// show the list
		for(String name : names) {
			HuskShellCommand command = COMMAND_MAP.get(name);
			String helpLine = command.getHelpLine();

			this.console.print(HuskUtils.rightPad(name, max, ' '));
			this.console.println(helpLine);
		}
		
		this.console.print('\n');
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
				
				if(command != null) {
					String name = command.getName();
					if(name == null || name.trim().isEmpty()) {
						// skip this command
						// TODO: log as an error
						continue;
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
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
}
