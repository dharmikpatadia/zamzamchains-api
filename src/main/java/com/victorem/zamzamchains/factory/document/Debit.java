package com.victorem.zamzamchains.factory.document;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Debit {

	@Id
	private int _id;
	@Field
	private String goldInfo;
	@Field
	private double grossWeight;
	@Field
	private double purityTouch;
	@Field
	private double fineWeight;
	@Field
	private double totalGrossWeight;
	@Field
	private double totalFineWeight;
	@Field
	private double balance;
	@Field
	private Date date;
	public int getId() {
		return _id;
	}
	public void setId(int _id) {
		this._id = _id;
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
	public double getTotalGrossWeight() {
		return totalGrossWeight;
	}
	public void setTotalGrossWeight(double totalGrossWeight) {
		this.totalGrossWeight = totalGrossWeight;
	}
	public double getTotalFineWeight() {
		return totalFineWeight;
	}
	public void setTotalFineWeight(double totalFineWeight) {
		this.totalFineWeight = totalFineWeight;
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
