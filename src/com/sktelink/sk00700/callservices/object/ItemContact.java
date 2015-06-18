package com.sktelink.sk00700.callservices.object;

public class ItemContact {
	
	private String oldNumber = "";
	private String newNumber = "";
	private String nameNumber= "";
	
	/*
	 * 
	 */
	
	public String getOldNumber() {
		return oldNumber;
	}
	public ItemContact setOldNumber(String oldNumber) {
		this.oldNumber = oldNumber;
		return this;
	}
	public String getNewNumber() {
		return newNumber;
	}
	public ItemContact setNewNumber(String newNumber) {
		this.newNumber = newNumber;
		return this;
	}
	public String getNameNumber() {
		return nameNumber;
	}
	public ItemContact setNameNumber(String nameNumber) {
		this.nameNumber = nameNumber;
		return this;
	}
	


}
