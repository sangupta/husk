package com.sangupta.husk;

import com.sangupta.husk.core.HuskShellContext;
import com.sangupta.husk.core.PromptProvider;

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
