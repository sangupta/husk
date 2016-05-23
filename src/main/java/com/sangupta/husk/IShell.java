/**
 * husk - cli shell framework
 * Copyright (c) 2013-2016, Sandeep Gupta
 * 
 * http://sangupta/projects/husk
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

import com.sangupta.consoles.IConsole;
import com.sangupta.husk.core.HuskShellContext;

/**
 * Contract for all shell implementations.
 * 
 * @author sangupta
 *
 */
public interface IShell {
	
	public void setPrompt(String prompt);
	
	public void setTitle(String title);
	
	public void setMaskCharacter(char mask);
	
	public void setExitCommandNames(String exitName);
	
	public void setExitCommandNames(String[] exitNames);
	
	public void setHelpCommandNames(String helpName);
	
	public void setHelpCommandNames(String[] helpNames);

	public void setHuskShellContext(HuskShellContext shellContext);

	public IConsole getConsole();
}