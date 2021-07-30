package com.victorem.zamzamchains.retail.document;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Chain {
	@Id
	private int _id;
	@Field
	private String chainName;
	@Field
	private double touch;
	@Field
	private double grossWeight;
	@Field
	private double yourTouch;
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
	public int getId() {
		return this._id;
	}
	public void setId(int _id) {
		this._id = _id;
	}
	
}
