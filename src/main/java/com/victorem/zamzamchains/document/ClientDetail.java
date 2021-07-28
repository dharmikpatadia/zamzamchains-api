package com.victorem.zamzamchains.document;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class ClientDetail {
	@Id
	private ObjectId _id;
	@Field
	private String value;
	@Field
	private String name;

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
	
	@Override
	public String toString() {
		return "ClientDetail [Id=" + _id + ", value=" + value + ", name=" + name + "]";
	}
}
