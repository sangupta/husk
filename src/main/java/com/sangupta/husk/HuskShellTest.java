package com.sangupta.husk;

public class HuskShellTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the shell instance
		HuskShell huskShell = new HuskShell();
		
		// prepare for launch
		huskShell.initialize();
		huskShell.setPrompt("Husk> ");
		huskShell.setExitCommandNames("exit");
		huskShell.setHelpCommandNames("help");
		huskShell.loadExternalCommands("com.sangupta.andruil.commands");
		
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
