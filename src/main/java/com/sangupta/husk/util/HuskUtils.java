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

package com.sangupta.husk.util;

/**
 * Contains generic utility methods to reduce dependency on other frameworks
 * like Apache Commons.
 * 
 * @author sangupta
 *
 */
public class HuskUtils {

	public static String rightPad(String str, int max, char pad) {
		StringBuilder builder = new StringBuilder(str);
		builder.setLength(max);
		for(int index = str.length(); index < max; index++) {
			builder.setCharAt(index, pad);
		}
		
		return builder.toString();
	}

}