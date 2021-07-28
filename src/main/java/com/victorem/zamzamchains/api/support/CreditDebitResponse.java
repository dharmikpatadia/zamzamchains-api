package com.victorem.zamzamchains.api.support;

public class CreditDebitResponse {

	private Double fineweight;

	public CreditDebitResponse(double fineweight) {
		this.fineweight=fineweight;
	}
	
	public Double getFineweight() {
		return fineweight;
	}

	public void setFineweight(Double fineweight) {
		this.fineweight = fineweight;
	}
}
