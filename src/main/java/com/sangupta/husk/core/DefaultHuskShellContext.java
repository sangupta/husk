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

package com.sangupta.husk.core;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author sangupta
 *
 */
public class DefaultHuskShellContext implements HuskShellContext {

	private File currentDirectory;
	
	public DefaultHuskShellContext() {
		this.currentDirectory = new File(".").getAbsoluteFile().getParentFile();
	}

	@Override
	public File getCurrentDirectory() {
		return currentDirectory;
	}

	@Override
	public void changeCurrentDirectory(File file) {
		if(file != null && file.exists() && file.isDirectory()) {
			String path;
			try {
				path = file.getCanonicalPath();
			} catch(IOException e) {
				path = file.getAbsolutePath();
			}
			
			this.currentDirectory = new File(path);
		}
	}
	
}
