package com.victorem.zamzamchains.factory.api.support;

import java.util.Date;

public class GetDebitTransactionResponse {
	private String clientName;
	private String goldInfo;
	private double grossWeight;
	private double purityTouch;
	private double fineWeight;
	private double balance;
	private Date date;

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getGoldInfo() {
		return goldInfo;
	}

	public void setGoldInfo(String goldInfo) {
		this.goldInfo = goldInfo;
	}

	public double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public double getPurityTouch() {
		return purityTouch;
	}

	public void setPurityTouch(double purityTouch) {
		this.purityTouch = purityTouch;
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

}
