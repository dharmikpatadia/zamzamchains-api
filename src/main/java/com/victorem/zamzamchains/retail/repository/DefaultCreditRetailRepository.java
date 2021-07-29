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

import com.victorem.zamzamchains.factory.document.ClientDetail;
import com.victorem.zamzamchains.retail.api.support.CreditDebitResponse;
import com.victorem.zamzamchains.retail.api.support.CreditSupport;
import com.victorem.zamzamchains.retail.document.Credit;
import com.victorem.zamzamchains.retail.document.Debit;
import com.victorem.zamzamchains.retail.document.RetailClientDetail;

@Repository
public class DefaultCreditRetailRepository {

	final static Logger logger = LogManager.getLogger(DefaultCreditRetailRepository.class);

	final static DecimalFormat df = new DecimalFormat("###.###");

	final static DecimalFormat touchDf = new DecimalFormat("###.##");

	@Autowired
	private RetailClientDetailRepository repository;

	@Autowired
	private MongoOperations mongo;

	public CreditDebitResponse addCreditRecord(CreditSupport creditSupport) {
		try {
			checkExsistingClient(creditSupport);

			Credit credit = new Credit();
			credit.setChainName(creditSupport.getChainName());
			Date date = null;
			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(creditSupport.getDate());
				logger.info("Date Credit : " + date);
			} catch (ParseException e) {
				logger.info("Error - " + e.getMessage());
				e.printStackTrace();
			}
			credit.setDate(date);

			double grossweight = Double.parseDouble(df.format(creditSupport.getGrossWeight()));

			double touch = Double.parseDouble(touchDf.format(creditSupport.getTouch()));

			double yourTouch = Double.parseDouble(touchDf.format(creditSupport.getYourTouch()));

			double fineWeight = Double.parseDouble(df.format(grossweight * (touch + yourTouch) / 100));

			credit.setFineWeight(fineWeight);
			credit.setGrossWeight(grossweight);
			credit.setTouch(touch);
			credit.setYourTouch(yourTouch);

			String tableNameDebit = creditSupport.getClientName() + "_Debit_Retail";
			String tableNameCredit = creditSupport.getClientName() + "_Credit_Retail";

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

			double balance = lastEntryDebit.getTotalFineWeight() - lastEntryCredit.getTotalFineWeight() - fineWeight;

			credit.setTotalFineWeight(Double.parseDouble(df.format(lastEntryCredit.getTotalFineWeight() + fineWeight)));
			credit.setTotalGrossWeight(
					Double.parseDouble(df.format(lastEntryCredit.getTotalGrossWeight() + grossweight)));
			credit.setId(lastEntryCredit.getId() + 1);
			credit.setBalance(Double.parseDouble(df.format(balance)));
			mongo.insert(credit, tableNameCredit);

			return new CreditDebitResponse(fineWeight);
		} catch (Exception e) {
			logger.info(e);
			return null;
		}
	}

	private void checkExsistingClient(CreditSupport creditSupport) {
		String retailClientValue = creditSupport.getClientName().replaceAll("\\s", "").toLowerCase();
		List<RetailClientDetail> retailClientList = repository.findAll();
		boolean duplicate = false;
		for (RetailClientDetail eachClient : retailClientList) {
			if (retailClientValue.equals(eachClient.getValue())) {
				duplicate = true;
				break;
			}
		}

		if (duplicate) {
			logger.info("Duplicate");
		} else {
			RetailClientDetail retailClientDetail = new RetailClientDetail();
			retailClientDetail.setName(creditSupport.getClientName());
			retailClientDetail.setValue(retailClientValue);
			retailClientDetail.setNumber(creditSupport.getNumber());
			repository.save(retailClientDetail);
		}
	}

}
