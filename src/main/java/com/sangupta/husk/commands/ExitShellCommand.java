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

package com.sangupta.husk.commands;

import com.sangupta.husk.HuskShellCommand;

/**
 * Implements the exit command for the shell.
 * 
 * @author sangupta
 *
 */
public class ExitShellCommand implements HuskShellCommand {

	public String getName() {
		return "exit";
	}

	public String getHelpLine() {
		return "Exit the shell";
	}

	public void execute(String[] arguments) {
		
	}

	@Override
	public String[] getNameAlias() {
		return new String[] { "quit" };
	}

}
