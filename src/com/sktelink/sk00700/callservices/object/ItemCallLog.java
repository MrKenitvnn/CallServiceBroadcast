package com.sktelink.sk00700.callservices.object;

/**
 * Created by admin on 6/2/2015.
 */
public class ItemCallLog {


    String callNumber   = "";
    String callDate     = "";
    String callDateEnd  = "";
    String callDuration = "";
    String callType     = "";
    String newNumber	= "";


    ////////////////////////////////////////////////////////////////////////////////
    // TODO getters & setters

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallNumber() {
        return callNumber;
    }


    public ItemCallLog setCallNumber(String callNumber) {
        this.callNumber = callNumber;
        return this;
    }


    public String getCallDate() {
        return callDate;
    }


    public ItemCallLog setCallDate(String callDate) {
        this.callDate = callDate;
        return this;
    }


    public String getCallDateEnd() {
        return callDateEnd;
    }


    public ItemCallLog setCallDateEnd(String callDateEnd) {
        this.callDateEnd = callDateEnd;
        return this;
    }


    public String getCallDuration() {
        return callDuration;
    }


    public ItemCallLog setCallDuration(String callDuration) {
        this.callDuration = callDuration;
        return this;
    }

	public String getNewNumber() {
		return newNumber;
	}

	public ItemCallLog setNewNumber(String pattern) {
		this.newNumber = pattern;
		return this;
	}


    
}
