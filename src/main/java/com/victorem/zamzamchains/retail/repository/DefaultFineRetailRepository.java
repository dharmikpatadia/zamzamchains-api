package com.victorem.zamzamchains.retail.repository;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.victorem.zamzamchains.retail.api.support.CreditDebitResponse;
import com.victorem.zamzamchains.retail.api.support.FineSupport;
import com.victorem.zamzamchains.retail.document.Chain;
import com.victorem.zamzamchains.retail.document.Fine;

@Repository
public class DefaultFineRetailRepository {

	final static Logger logger = LogManager.getLogger(DefaultFineRetailRepository.class);

	final static DecimalFormat df = new DecimalFormat("###.###");

	final static DecimalFormat touchDf = new DecimalFormat("###.##");

	@Autowired
	private MongoOperations mongo;	

	public CreditDebitResponse addFineRecord(FineSupport debitSupport) {
		try {
			Fine fine = new Fine();
			fine.setGoldInfo(debitSupport.getGoldInfo());
			Date date = null;
			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(debitSupport.getDate());
			} catch (ParseException e) {
				logger.info("Error - " + e.getMessage());
				e.printStackTrace();
			}
			fine.setDate(date);
			double grossweight = Double.parseDouble(df.format(debitSupport.getGrossWeight()));
			double purityTouch = Double.parseDouble(touchDf.format(debitSupport.getPurityTouch()));
			fine.setGrossWeight(grossweight);
			fine.setPurityTouch(purityTouch);

			double fineweight = Double.parseDouble(df.format(grossweight * purityTouch / 100));
			fine.setFineWeight(fineweight);

			String tableNameChain = debitSupport.getClientName() + "_Chain_Retail";
			String tableNameFine = debitSupport.getClientName() + "_Fine_Retail";

			Query query = new Query();
			query.limit(1);
			query.with(Sort.by(Sort.Direction.DESC, "_id"));

			List<Chain> listCredit = mongo.find(query, Chain.class, tableNameChain);
			Chain lastEntryCredit = new Chain();
			if (listCredit.size() <= 0) {
				lastEntryCredit.setTotalFineWeight(0);
				lastEntryCredit.setId(0);
			} else {
				lastEntryCredit = listCredit.get(0);
			}

			List<Fine> listDebit = mongo.find(query, Fine.class, tableNameFine);
			Fine lastEntryDebit = new Fine();

			if (listDebit.size() <= 0) {
				lastEntryDebit.setTotalFineWeight(0);
				lastEntryDebit.setId(0);
			} else {
				lastEntryDebit = listDebit.get(0);
			}

			double balance = lastEntryCredit.getTotalFineWeight() -lastEntryDebit.getTotalFineWeight() - fineweight;

			fine.setTotalFineWeight(Double.parseDouble(df.format(lastEntryDebit.getTotalFineWeight() + fineweight)));
			
			fine.setTotalGrossWeight(Double.parseDouble(df.format(lastEntryDebit.getTotalGrossWeight() + grossweight)));

			fine.setId(lastEntryDebit.getId() + 1);

			fine.setBalance(Double.parseDouble(df.format(balance)));

			mongo.insert(fine, tableNameFine);

			return new CreditDebitResponse(fineweight);
		} catch (Exception e) {
			logger.info("Error : " + e);
			return null;
		}
	}

}
