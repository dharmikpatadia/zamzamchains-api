package com.victorem.zamzamchains.retail.repository;

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

import com.victorem.zamzamchains.retail.api.support.DeleteMultipleSupport;
import com.victorem.zamzamchains.retail.api.support.DeleteSingleSupport;
import com.victorem.zamzamchains.retail.document.Chain;
import com.victorem.zamzamchains.retail.document.Fine;

@Repository
public class DeleteRetailRepository {

	final static Logger logger = LogManager.getLogger(DeleteRetailRepository.class);

	@Autowired
	private MongoOperations mongo;

	public boolean deleteSingleRecord(DeleteSingleSupport deleteSupport) {

		Query query = new Query();
		String tableName = deleteSupport.getClientName() + "_" + deleteSupport.getType()+"_Retail";
		query.addCriteria(Criteria.where("_id").is(deleteSupport.getId()));
		if (deleteSupport.getType().equals("Chain")) {
			Chain DeletedRecord = mongo.findAndRemove(query, Chain.class, tableName);
			if (DeletedRecord.getId() == deleteSupport.getId()) {
				return true;
			} else {
				return false;
			}
		} else {
			Fine DeletedRecord = mongo.findAndRemove(query, Fine.class, tableName);
			if (DeletedRecord.getId() == deleteSupport.getId()) {
				return true;
			} else {
				return false;
			}
		}

	}

	public boolean deleteMultipleRecord(DeleteMultipleSupport multiDeleteSupport) {
		try {
			String tableName = multiDeleteSupport.getClientName() + "_" + multiDeleteSupport.getType()+"_Retail";
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
			
			if (multiDeleteSupport.getType().equals("Chain")) {
				query.addCriteria(Criteria.where("date").gte(startDate).lte(toDate).andOperator(
						Criteria.where("chainName").ne("balance").andOperator(Criteria.where("chainName").ne("initial"))));
				mongo.findAllAndRemove(query, Chain.class, tableName);
			} else {
				query.addCriteria(Criteria.where("date").gte(startDate).lte(toDate).andOperator(
						Criteria.where("goldInfo").ne("balance").andOperator(Criteria.where("goldInfo").ne("initial"))));
				mongo.findAllAndRemove(query, Fine.class, tableName);
			}
			
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
