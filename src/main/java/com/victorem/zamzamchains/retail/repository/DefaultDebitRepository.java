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
import com.victorem.zamzamchains.retail.api.support.DebitSupport;
import com.victorem.zamzamchains.retail.document.Credit;
import com.victorem.zamzamchains.retail.document.Debit;

@Repository
public class DefaultDebitRepository {

	final static Logger logger = LogManager.getLogger(DefaultDebitRepository.class);

	final static DecimalFormat df = new DecimalFormat("###.###");

	final static DecimalFormat touchDf = new DecimalFormat("###.##");

	@Autowired
	private MongoOperations mongo;	

	public CreditDebitResponse addDebitRecord(DebitSupport debitSupport) {
		try {
			Debit debit = new Debit();
			debit.setGoldInfo(debitSupport.getGoldInfo());
			Date date = null;
			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(debitSupport.getDate());
			} catch (ParseException e) {
				logger.info("Error - " + e.getMessage());
				e.printStackTrace();
			}
			debit.setDate(date);
			double grossweight = Double.parseDouble(df.format(debitSupport.getGrossWeight()));
			double purityTouch = Double.parseDouble(touchDf.format(debitSupport.getPurityTouch()));
			debit.setGrossWeight(grossweight);
			debit.setPurityTouch(purityTouch);

			double fineweight = Double.parseDouble(df.format(grossweight * purityTouch / 100));
			debit.setFineWeight(fineweight);

			String tableNameCredit = debitSupport.getClientName() + "_Credit_Retail";
			String tableNameDebit = debitSupport.getClientName() + "_Debit_Retail";

			Query query = new Query();
			query.limit(1);
			query.with(Sort.by(Sort.Direction.DESC, "_id"));

			List<Credit> listCredit = mongo.find(query, Credit.class, tableNameCredit);
			Credit lastEntryCredit = new Credit();
			if (listCredit.size() <= 0) {
				lastEntryCredit.setTotalFineWeight(0);
				lastEntryCredit.setId(0);
			} else {
				lastEntryCredit = listCredit.get(0);
			}

			List<Debit> listDebit = mongo.find(query, Debit.class, tableNameDebit);
			Debit lastEntryDebit = new Debit();

			if (listDebit.size() <= 0) {
				lastEntryDebit.setTotalFineWeight(0);
				lastEntryDebit.setId(0);
			} else {
				lastEntryDebit = listDebit.get(0);
			}

			double balance = lastEntryDebit.getTotalFineWeight() - lastEntryCredit.getTotalFineWeight() + fineweight;

			debit.setTotalFineWeight(Double.parseDouble(df.format(lastEntryDebit.getTotalFineWeight() + fineweight)));
			
			debit.setTotalGrossWeight(Double.parseDouble(df.format(lastEntryDebit.getTotalGrossWeight() + grossweight)));

			debit.setId(lastEntryDebit.getId() + 1);

			debit.setBalance(Double.parseDouble(df.format(balance)));

			mongo.insert(debit, tableNameDebit);

			return new CreditDebitResponse(fineweight);
		} catch (Exception e) {
			logger.info("Error : " + e);
			return null;
		}
	}

}
