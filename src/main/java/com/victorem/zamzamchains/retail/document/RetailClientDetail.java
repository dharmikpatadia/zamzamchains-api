package com.victorem.zamzamchains.retail.document;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class RetailClientDetail {
	@Id
	private ObjectId _id;
	@Field
	private String value;
	@Field
	private String name;
	@Field
	private String number;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectId getId() {
		return _id;
	}

	public void setId(ObjectId _id) {
		this._id = _id;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "RetailClientDetail [_id=" + _id + ", value=" + value + ", name=" + name + ", number=" + number + "]";
	}
}
