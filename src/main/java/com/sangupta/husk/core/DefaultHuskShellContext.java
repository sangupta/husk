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

package com.sangupta.husk.core;

import java.io.File;
import java.io.IOException;

import com.sangupta.consoles.IConsole;

/**
 * A default implementation of the {@link HuskShellContext} that allows for
 * rapid roll-out of shells.
 * 
 * @author sangupta
 *
 */
public class DefaultHuskShellContext implements HuskShellContext {
	
	/**
	 * The {@link IConsole} instance we are currently working on
	 * 
	 */
	protected final IConsole console;

	/**
	 * The current directory that the shell is running in.
	 */
	protected File currentDirectory;
	
	/**
	 * Default constructor that sets the current directory to the folder
	 * from which the application is launched in.
	 * 
	 */
	public DefaultHuskShellContext(IConsole console) {
		this.console = console;
		this.currentDirectory = new File(".").getAbsoluteFile().getParentFile();
	}

	/**
	 * Return the current directory.
	 */
	@Override
	public File getCurrentDirectory() {
		return this.currentDirectory;
	}

	/**
	 * Change the current directory to the given folder.
	 * 
	 */
	@Override
	public void changeCurrentDirectory(File file) {
		if(file != null && file.exists() && file.isDirectory()) {
			String path;
			try {
				path = file.getCanonicalPath();
			} catch(IOException e) {
				path = file.getAbsolutePath();
			}
			
			this.currentDirectory = new File(path);
		}
	}

	@Override
	public IConsole getConsole() {
		return this.console;
	}

}
