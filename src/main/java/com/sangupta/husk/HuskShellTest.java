package com.sangupta.husk;

public class HuskShellTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HuskShell huskShell = new HuskShell();
		huskShell.initialize();
		
		huskShell.loadExternalCommands("com.sangupta.andruil.commands");
		
		huskShell.start();
		
		huskShell.stop();
	}

}
