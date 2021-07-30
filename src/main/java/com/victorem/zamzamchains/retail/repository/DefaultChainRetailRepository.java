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
import com.victorem.zamzamchains.retail.api.support.ChainSupport;
import com.victorem.zamzamchains.retail.document.Chain;
import com.victorem.zamzamchains.retail.document.Fine;
import com.victorem.zamzamchains.retail.document.RetailClientDetail;

@Repository
public class DefaultChainRetailRepository {

	final static Logger logger = LogManager.getLogger(DefaultChainRetailRepository.class);

	final static DecimalFormat df = new DecimalFormat("###.###");

	final static DecimalFormat touchDf = new DecimalFormat("###.##");

	@Autowired
	private RetailClientDetailRepository repository;

	@Autowired
	private MongoOperations mongo;

	public CreditDebitResponse addChainRecord(ChainSupport creditSupport) {
		try {
			checkExsistingClient(creditSupport);

			Chain chain = new Chain();
			chain.setChainName(creditSupport.getChainName());
			Date date = null;
			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(creditSupport.getDate());
				logger.info("Date Credit : " + date);
			} catch (ParseException e) {
				logger.info("Error - " + e.getMessage());
				e.printStackTrace();
			}
			chain.setDate(date);

			double grossweight = Double.parseDouble(df.format(creditSupport.getGrossWeight()));

			double touch = Double.parseDouble(touchDf.format(creditSupport.getTouch()));

			double yourTouch = Double.parseDouble(touchDf.format(creditSupport.getYourTouch()));

			double fineWeight = Double.parseDouble(df.format(grossweight * (touch + yourTouch) / 100));

			chain.setFineWeight(fineWeight);
			chain.setGrossWeight(grossweight);
			chain.setTouch(touch);
			chain.setYourTouch(yourTouch);

			String tableNameFine = creditSupport.getClientName().replaceAll("\\s", "").toLowerCase() + "_Fine_Retail";
			String tableNameChain = creditSupport.getClientName().replaceAll("\\s", "").toLowerCase() + "_Chain_Retail";

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

			double balance = lastEntryCredit.getTotalFineWeight() - lastEntryDebit.getTotalFineWeight() + fineWeight;

			chain.setTotalFineWeight(Double.parseDouble(df.format(lastEntryCredit.getTotalFineWeight() + fineWeight)));
			chain.setTotalGrossWeight(
					Double.parseDouble(df.format(lastEntryCredit.getTotalGrossWeight() + grossweight)));
			chain.setId(lastEntryCredit.getId() + 1);
			chain.setBalance(Double.parseDouble(df.format(balance)));
			mongo.insert(chain, tableNameChain);

			return new CreditDebitResponse(fineWeight);
		} catch (Exception e) {
			logger.info(e);
			return null;
		}
	}

	private void checkExsistingClient(ChainSupport creditSupport) {
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
