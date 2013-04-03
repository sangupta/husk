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

import com.sangupta.husk.core.PromptProvider;

/**
 * A simple class to test {@link HuskShell}.
 * 
 * @author sangupta
 *
 */
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
