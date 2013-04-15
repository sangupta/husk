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

/**
 * Marker interface for objects that need to be used as the Shell
 * Context.
 * 
 * @author sangupta
 *
 */
public interface HuskShellContext {
	
	/**
	 * Return the current directory in which the commands should operate.
	 * 
	 * @return
	 */
	public File getCurrentDirectory();

	/**
	 * Change the current directory to the one provided.
	 * 
	 * @param file
	 */
	public void changeCurrentDirectory(File file);
	
}
