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
	
	protected String prompt;
	
	protected PromptProvider promptProvider;
	
	protected char maskCharacter;
	
	protected String[] exitCommandNames = { "exit" };
	
	protected String[] helpCommandNames = { "help" };
	
	protected HuskShellContext shellContext;
	
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public void setMaskCharacter(char mask) {
		this.maskCharacter = mask;
	}
	
	public void setExitCommandNames(String exitName) {
		if(exitName == null) {
			return;
		}
		
		this.exitCommandNames = new String[] { exitName };
	}

	public void setExitCommandNames(String[] exitNames) {
		if(exitNames == null) {
			return;
		}
		
		this.exitCommandNames = exitNames;
	}
	
	public void setHelpCommandNames(String helpName) {
		if(helpName == null) {
			return;
		}
		
		this.helpCommandNames = new String[] { helpName };
	}

	public void setHelpCommandNames(String[] helpNames) {
		if(helpNames == null) {
			return;
		}
		
		this.helpCommandNames = helpNames;
	}

	public void setPromptProvider(PromptProvider promptProvider) {
		this.promptProvider = promptProvider;
	}
	
	@Override
	public void setHuskShellContext(HuskShellContext shellContext) {
		this.shellContext = shellContext;
	}

}
