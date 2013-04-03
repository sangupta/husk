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
