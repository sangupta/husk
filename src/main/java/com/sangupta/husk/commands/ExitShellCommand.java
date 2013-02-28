package com.sangupta.husk.commands;

import com.sangupta.husk.HuskShellCommand;

public class ExitShellCommand implements HuskShellCommand {

	public String getName() {
		return "exit";
	}

	public String getHelpLine() {
		return "Exit the shell";
	}

	public void execute(String[] arguments) {
		
	}

}
