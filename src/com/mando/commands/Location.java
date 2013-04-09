package com.mando.commands;

import java.util.Hashtable;

public class Location extends CommandAbstract {


	public Location(String receivedSMS, String commandstring,
			String settingstring) {
		super(receivedSMS, commandstring, settingstring);
		// TODO Auto-generated constructor stub
	}

	@Override
	boolean isCommand() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	void execute() {
		
	}

	@Override
	Hashtable<String, String> getSettingString() {
		// TODO Auto-generated method stub
		return null;
	}

}
