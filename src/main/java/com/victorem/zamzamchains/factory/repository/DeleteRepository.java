package com.victorem.zamzamchains.factory.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.victorem.zamzamchains.factory.api.support.DeleteMultipleSupport;
import com.victorem.zamzamchains.factory.api.support.DeleteSingleSupport;
import com.victorem.zamzamchains.factory.document.Credit;
import com.victorem.zamzamchains.factory.document.Debit;

@Repository
public class DeleteRepository {

	final static Logger logger = LogManager.getLogger(DeleteRepository.class);

	@Autowired
	private MongoOperations mongo;

	public boolean deleteSingleRecord(DeleteSingleSupport deleteSupport) {

		Query query = new Query();
		String tableName = deleteSupport.getClientName() + "_" + deleteSupport.getType();
		query.addCriteria(Criteria.where("_id").is(deleteSupport.getId()));
		if (deleteSupport.getType().equals("Credit")) {
			Credit DeletedRecord = mongo.findAndRemove(query, Credit.class, tableName);
			if (DeletedRecord.getId() == deleteSupport.getId()) {
				return true;
			} else {
				return false;
			}
		} else {
			Debit DeletedRecord = mongo.findAndRemove(query, Debit.class, tableName);
			if (DeletedRecord.getId() == deleteSupport.getId()) {
				return true;
			} else {
				return false;
			}
		}

	}

	public boolean deleteMultipleRecord(DeleteMultipleSupport multiDeleteSupport) {
		try {
			String tableName = multiDeleteSupport.getClientName() + "_" + multiDeleteSupport.getType();
			Query query = new Query();
			Date startDate = null;
			Date toDate = null;
			logger.info("Multiple Delete Records for " + multiDeleteSupport.getClientName() + " From : "
					+ multiDeleteSupport.getFromDate() + " To :" + multiDeleteSupport.getEndDate());
			try {
				startDate = new SimpleDateFormat("dd/MM/yyyy").parse(multiDeleteSupport.getFromDate());
				toDate = new SimpleDateFormat("dd/MM/yyyy").parse(multiDeleteSupport.getEndDate());
			} catch (ParseException e) {
				logger.info("Error - " + e.getMessage());
				e.printStackTrace();
			}
			
			if (multiDeleteSupport.getType().equals("Credit")) {
				query.addCriteria(Criteria.where("date").gte(startDate).lte(toDate).andOperator(
						Criteria.where("chainName").ne("balance").andOperator(Criteria.where("chainName").ne("initial"))));
				mongo.findAllAndRemove(query, Credit.class, tableName);
			} else {
				query.addCriteria(Criteria.where("date").gte(startDate).lte(toDate).andOperator(
						Criteria.where("goldInfo").ne("balance").andOperator(Criteria.where("goldInfo").ne("initial"))));
				mongo.findAllAndRemove(query, Debit.class, tableName);
			}
			
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
