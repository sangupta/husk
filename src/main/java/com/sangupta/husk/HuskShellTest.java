package com.sangupta.husk;

import com.sangupta.husk.core.PromptProvider;

public class HuskShellTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the shell instance
		HuskShell huskShell = new HuskShell();
		
		// prepare for launch
		huskShell.initialize();
		
		PromptProvider promptProvider = new PromptProvider() {
			
			@Override
			public String getPrompt() {
				return System.currentTimeMillis() + " Husk> ";
			}
		};
		
		huskShell.setPromptProvider(promptProvider);
		huskShell.setExitCommandNames("exit");
		huskShell.setHelpCommandNames("help");
		huskShell.loadExternalCommands("com.sangupta.andruil.commands");
		
		System.out.println("a\nb");
		
		// start the shell instance
		try {
			huskShell.start();
		} catch(Throwable t) {
			t.printStackTrace();
		}
		
		// stop and destroy all resources associated with the shell
		huskShell.stop();
	}

}
