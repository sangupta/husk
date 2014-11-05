package com.sangupta.husk;

import java.util.Map;

import com.sangupta.jerry.util.AssertUtils;

public class HuskCommandMapper {
	
	private Map<String, HuskShellCommand> commands;

	public HuskCommandMapper(Map<String, HuskShellCommand> commands) {
		this.commands = commands;
	}
	
	public HuskShellCommand obtain(String name) {
		if(AssertUtils.isEmpty(name)) {
			return null;
		}
		
		return commands.get(name);
	}

}
