package com.mando.commands;

import java.util.Hashtable;

public abstract class CommandAbstract {
	private static final int id = -1;
	
	protected String commandString;
	protected String receivedSMS;
	protected Hashtable<String, String> param;
	
	public CommandAbstract(String receivedSMS, String commandstring, String settingstring) {
		this.receivedSMS = receivedSMS;
		this.commandString = commandstring;
	}

	public int getId() {
		return CommandAbstract.id;
	}
	abstract boolean isCommand();
	abstract void execute();
	abstract Hashtable<String, String> getSettingString();
}
