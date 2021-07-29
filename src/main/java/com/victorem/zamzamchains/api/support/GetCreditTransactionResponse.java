package com.victorem.zamzamchains.api.support;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class GetCreditTransactionResponse {
	private String clientName;
	private String chainName;
	private double touch;
	private double grossWeight;
	private double yourTouch;
	private double fineWeight;
	private double balance;
	private Date date;
	
	public String getChainName() {
		return chainName;
	}
	public void setChainName(String chainName) {
		this.chainName = chainName;
	}
	public double getTouch() {
		return touch;
	}
	public void setTouch(double touch) {
		this.touch = touch;
	}
	public double getGrossWeight() {
		return grossWeight;
	}
	public void setGrossWeight(double grossWeight) {
		this.grossWeight = grossWeight;
	}
	public double getYourTouch() {
		return yourTouch;
	}
	public void setYourTouch(double yourTouch) {
		this.yourTouch = yourTouch;
	}
	public double getFineWeight() {
		return fineWeight;
	}
	public void setFineWeight(double fineWeight) {
		this.fineWeight = fineWeight;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
}
