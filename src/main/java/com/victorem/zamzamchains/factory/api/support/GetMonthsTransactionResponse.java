package com.victorem.zamzamchains.factory.api.support;

public class GetMonthsTransactionResponse {
	
	private String clientName;
	private double totalCreditGrossWeight;
	private double totalDebitGrossWeight;
	private double totalCreditFineWeight;
	private double totalDebitFineWeight;
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public double getTotalCreditGrossWeight() {
		return totalCreditGrossWeight;
	}
	public void setTotalCreditGrossWeight(double totalCreditGrossWeight) {
		this.totalCreditGrossWeight = totalCreditGrossWeight;
	}
	public double getTotalDebitGrossWeight() {
		return totalDebitGrossWeight;
	}
	public void setTotalDebitGrossWeight(double totalDebitGrossWeight) {
		this.totalDebitGrossWeight = totalDebitGrossWeight;
	}
	public double getTotalCreditFineWeight() {
		return totalCreditFineWeight;
	}
	public void setTotalCreditFineWeight(double totalCreditFineWeight) {
		this.totalCreditFineWeight = totalCreditFineWeight;
	}
	public double getTotalDebitFineWeight() {
		return totalDebitFineWeight;
	}
	public void setTotalDebitFineWeight(double totalDebitFineWeight) {
		this.totalDebitFineWeight = totalDebitFineWeight;
	}
	
	

}
