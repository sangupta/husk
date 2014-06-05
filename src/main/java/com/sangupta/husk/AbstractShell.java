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

import com.sangupta.husk.core.HuskShellContext;
import com.sangupta.husk.core.HuskShellContextAware;
import com.sangupta.husk.core.PromptProvider;

/**
 * An abstract implementation of the {@link IShell} interface that takes out
 * the very basic features of a shell and adds them. This basically makes creating
 * totally personalised shells by just extending this class.
 *  
 * @author sangupta
 *
 */
public abstract class AbstractShell implements IShell {
	
	/**
	 * The prompt that needs to be displayed
	 */
	protected String prompt = "Husk >";
	
	/**
	 * The prompt provider to use, if any. If a prompt provider is provided
	 * then it used, else, the given prompt is shown.
	 * 
	 */
	protected PromptProvider promptProvider;
	
	/**
	 * Character to use to mask screen when reading passwords.
	 * 
	 */
	protected char maskCharacter;
	
	/**
	 * Names of commands to use to exit the shell. The array is initialized
	 * to the default name consisting of <strong>exit</strong>.
	 * 
	 */
	protected String[] exitCommandNames = { "exit" };
	
	/**
	 * Names of commands to use to print short help for the shell. The array 
	 * is initialized to the default name consisting of <strong>help</strong>.
	 */
	protected String[] helpCommandNames = { "help" };
	
	/**
	 * Shell context to use to be passed to various commands that implement
	 * the {@link HuskShellContextAware} interface.
	 */
	protected HuskShellContext shellContext;
	
	/**
	 * Set the new value of the prompt.
	 * 
	 */
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	/**
	 * Set the new masking character.
	 * 
	 */
	public void setMaskCharacter(char mask) {
		this.maskCharacter = mask;
	}
	
	/**
	 * Set a new exit command name, ignoring previous names.
	 * 
	 */
	public void setExitCommandNames(String exitName) {
		if(exitName == null) {
			return;
		}
		
		this.exitCommandNames = new String[] { exitName };
	}

	/**
	 * Set new exit command names, ignoring previous ones.
	 * 
	 */
	public void setExitCommandNames(String[] exitNames) {
		if(exitNames == null) {
			return;
		}
		
		this.exitCommandNames = exitNames;
	}
	
	/**
	 * Set new help command names, ignoring previous ones.
	 * 
	 */
	public void setHelpCommandNames(String helpName) {
		if(helpName == null) {
			return;
		}
		
		this.helpCommandNames = new String[] { helpName };
	}

	/**
	 * Set new help command names, ignoring previous ones.
	 * 
	 */
	public void setHelpCommandNames(String[] helpNames) {
		if(helpNames == null) {
			return;
		}
		
		this.helpCommandNames = helpNames;
	}

	/**
	 * Set a new {@link PromptProvider}.
	 * 
	 * @param promptProvider
	 */
	public void setPromptProvider(PromptProvider promptProvider) {
		this.promptProvider = promptProvider;
	}
	
	/**
	 * Return the current shell context.
	 * 
	 * @return
	 */
	public HuskShellContext getCurrentShellContext() {
		return this.shellContext;
	}
	
	/**
	 * Set a new {@link HuskShellContext}.
	 * 
	 */
	@Override
	public void setHuskShellContext(HuskShellContext shellContext) {
		if(shellContext == null) {
			throw new IllegalArgumentException("ShellContext cannot be null");
		}
		
		this.shellContext = shellContext;
	}

}
