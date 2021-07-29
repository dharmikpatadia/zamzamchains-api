package com.victorem.zamzamchains.factory.repository;

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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.victorem.zamzamchains.factory.api.support.AddBalanceSupport;
import com.victorem.zamzamchains.factory.api.support.GetLastEntrySupport;
import com.victorem.zamzamchains.factory.api.support.PrintSupport;
import com.victorem.zamzamchains.factory.document.Credit;
import com.victorem.zamzamchains.factory.document.Debit;
import com.victorem.zamzamchains.factory.model.LastEntry;

@Repository
public class MonthEndRepository {

	final static Logger logger = LogManager.getLogger(MonthEndRepository.class);

	final static DecimalFormat df = new DecimalFormat("###.###");

	final static DecimalFormat touchDf = new DecimalFormat("###.##");

	@Autowired
	private MongoOperations mongo;

	public LastEntry getLastEntry(GetLastEntrySupport getLastSupport) {
		try {

			String tableNameCredit = getLastSupport.getClientName() + "_Credit";
			String tableNameDebit = getLastSupport.getClientName() + "_Debit";

			Query query = new Query();
			query.limit(1);
			query.with(Sort.by(Sort.Direction.DESC, "_id"));

			List<Credit> listCredit = mongo.find(query, Credit.class, tableNameCredit);
			Credit lastEntryCredit = listCredit.get(0);

			List<Debit> listDebit = mongo.find(query, Debit.class, tableNameDebit);
			Debit lastEntryDebit = listDebit.get(0);
			
			LastEntry lastEntry=new LastEntry();
			
			lastEntry.setCreditTotalFineWeight(lastEntryCredit.getTotalFineWeight());
			lastEntry.setDebitTotalFineWeight(lastEntryDebit.getTotalFineWeight());

			return lastEntry;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Credit> getCreditPrint(PrintSupport printSupport) {

		try {
			String tableNameCredit = printSupport.getClientName() + "_Credit";
			Query query = new Query();
			Date startDate = null;
			Date toDate = null;
			logger.info("Credit Records for " + printSupport.getClientName() + " From : " + printSupport.getFromDate()
					+ " To :" + printSupport.getToDate());
			try {
				startDate = new SimpleDateFormat("dd/MM/yyyy").parse(printSupport.getFromDate());
				toDate = new SimpleDateFormat("dd/MM/yyyy").parse(printSupport.getToDate());
			} catch (ParseException e) {
				logger.info("Error - " + e.getMessage());
				e.printStackTrace();
			}
			query.addCriteria(Criteria.where("date").gte(startDate).lte(toDate));
			List<Credit> listCredit = mongo.find(query, Credit.class, tableNameCredit);
			return listCredit;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Debit> getDebitPrint(PrintSupport printSupport) {

		try {
			String tableNameDebit = printSupport.getClientName() + "_Debit";
			Query query = new Query();
			Date startDate = null;
			Date toDate = null;
			logger.info("Debit Records for " + printSupport.getClientName() + " From : " + printSupport.getFromDate()
					+ " To :" + printSupport.getToDate());
			try {
				startDate = new SimpleDateFormat("dd/MM/yyyy").parse(printSupport.getFromDate());
				toDate = new SimpleDateFormat("dd/MM/yyyy").parse(printSupport.getToDate());
			} catch (ParseException e) {
				logger.info("Error - " + e.getMessage());
				e.printStackTrace();
			}
			query.addCriteria(Criteria.where("date").gte(startDate).lte(toDate));
			List<Debit> listDebit = mongo.find(query, Debit.class, tableNameDebit);
			return listDebit;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean balanceEntry(AddBalanceSupport balanceSupport) {
		try {
			Debit debit = new Debit();
			Credit credit = new Credit();
			credit.setChainName("balance");
			debit.setGoldInfo("balance");
			Date date = null;
			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(balanceSupport.getDate());
			} catch (ParseException e) {
				logger.info("Error - " + e.getMessage());
				e.printStackTrace();
			}
			debit.setDate(date);
			credit.setDate(date);
			debit.setFineWeight(0);
			credit.setFineWeight(0);
			debit.setGrossWeight(0);
			credit.setGrossWeight(0);
			debit.setPurityTouch(0);
			credit.setTouch(0);
			credit.setYourTouch(0);

			String tableNameCredit = balanceSupport.getClientName() + "_Credit";
			String tableNameDebit = balanceSupport.getClientName() + "_Debit";

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

			double balance = lastEntryDebit.getTotalFineWeight() - lastEntryCredit.getTotalFineWeight();

			debit.setTotalFineWeight(
					balance < 0 ? Double.parseDouble(df.format(0)) : Double.parseDouble(df.format(balance)));
			credit.setTotalFineWeight(
					balance < 0 ? -Double.parseDouble(df.format(balance)) : Double.parseDouble(df.format(0)));
			debit.setId(lastEntryDebit.getId() + 1);
			credit.setId(lastEntryCredit.getId()+1);
			debit.setBalance(Double.parseDouble(df.format(balance)));
			credit.setBalance(Double.parseDouble(df.format(balance)));
			mongo.insert(debit, tableNameDebit);
			mongo.insert(credit,tableNameCredit);

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
