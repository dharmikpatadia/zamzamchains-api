package com.victorem.zamzamchains.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.victorem.zamzamchains.api.support.GetCreditTransactionResponse;
import com.victorem.zamzamchains.api.support.GetDebitTransactionResponse;
import com.victorem.zamzamchains.api.support.GetMonthsTransactionResponse;
import com.victorem.zamzamchains.api.support.GetMonthsTransactionSupport;
import com.victorem.zamzamchains.api.support.GetTransactionSupport;
import com.victorem.zamzamchains.document.ClientDetail;
import com.victorem.zamzamchains.document.Credit;
import com.victorem.zamzamchains.document.Debit;
import com.victorem.zamzamchains.repository.ClientDetailRepository;

@Service
public class GetTransactionService {

	final static Logger logger = LogManager.getLogger(GetTransactionService.class);

	@Autowired
	private ClientDetailRepository repository;

	@Autowired
	private MongoOperations mongo;

	@Autowired
	private ClientDetailRepository clientDetailRepository;

	public List<GetMonthsTransactionResponse> getMonthsTransactionRecords(
			GetMonthsTransactionSupport getTransactionSupport) {

		try {
			List<ClientDetail> clientDetailList = clientDetailRepository.findAll();

			List<GetMonthsTransactionResponse> todaysList = new ArrayList<>();
			Date fromDate = null;
			Date toDate = null;

			try {
				fromDate = new SimpleDateFormat("dd/MM/yyyy").parse(getTransactionSupport.getFromDate());
				toDate = new SimpleDateFormat("dd/MM/yyyy").parse(getTransactionSupport.getToDate());
			} catch (ParseException e) {
				logger.info("Error - " + e.getMessage());
				e.printStackTrace();
			}
			for (ClientDetail eachClient : clientDetailList) {
				String tableNameCredit = eachClient.getValue() + "_Credit";
				String tableNameDebit = eachClient.getValue() + "_Debit";
				Query query = new Query();
				query.addCriteria(Criteria.where("date").gte(fromDate).lte(toDate));
				try {
					List<Credit> listCredit = mongo.find(query, Credit.class, tableNameCredit);
					List<Debit> listDebit = mongo.find(query, Debit.class, tableNameDebit);
					todaysList.add(buildMonthsResponse(listCredit, listDebit, eachClient.getName()));
				} catch (Exception e) {
					logger.info("Null : " + e);
				}
			}

			return todaysList;
		} catch (Exception e) {
			logger.info("error " + e);
			throw e;
		}
	}

	GetMonthsTransactionResponse buildMonthsResponse(List<Credit> listCredit, List<Debit> listDebit,
			String clientName) {

		GetMonthsTransactionResponse responseList = new GetMonthsTransactionResponse();
		double totalCreditGrossWeight = 0;
		double totalCreditFineWeight = 0;
		double totalDebitGrossWeight = 0;
		double totalDebitFineWeight = 0;
		
		for (Credit i : listCredit) {
			if (!i.getChainName().equals("balance")) {
				totalCreditGrossWeight += i.getGrossWeight();
				totalCreditFineWeight += i.getFineWeight();
			}
			
		}
		
		for (Debit i : listDebit) {
			if (!i.getGoldInfo().equals("balance")) {
				totalDebitGrossWeight += i.getGrossWeight();
				totalDebitFineWeight += i.getFineWeight();
			}
			
		}
		responseList.setClientName(clientName);
		responseList.setTotalCreditFineWeight(totalCreditFineWeight);
		responseList.setTotalCreditGrossWeight(totalCreditGrossWeight);
		responseList.setTotalDebitFineWeight(totalDebitFineWeight);
		responseList.setTotalDebitGrossWeight(totalDebitGrossWeight);
		return responseList;
	}

	public List<GetCreditTransactionResponse> getTodaysCreditRecords(GetTransactionSupport getTransactionSupport) {

		try {
			List<ClientDetail> clientDetailList = clientDetailRepository.findAll();

			List<GetCreditTransactionResponse> todaysList = new ArrayList<>();
			Date date = null;

			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(getTransactionSupport.getDate());
			} catch (ParseException e) {
				logger.info("Error - " + e.getMessage());
				e.printStackTrace();
			}
			for (ClientDetail eachClient : clientDetailList) {
				String tableNameCredit = eachClient.getValue() + "_Credit";
				Query query = new Query();
				query.addCriteria(Criteria.where("date").gte(date).lte(date));
				try {
					List<Credit> listCredit = mongo.find(query, Credit.class, tableNameCredit);
					todaysList.addAll(buildCreditResponse(listCredit, eachClient.getName()));
				} catch (Exception e) {
					logger.info("Null : " + e);
				}
			}

			return todaysList;
		} catch (Exception e) {
			logger.info("error " + e);
			throw e;
		}
	}

	List<GetCreditTransactionResponse> buildCreditResponse(List<Credit> listCredit, String clientName) {

		List<GetCreditTransactionResponse> responseList = new ArrayList<>();
		for (Credit i : listCredit) {
			GetCreditTransactionResponse response = new GetCreditTransactionResponse();
			response.setBalance(i.getBalance());
			response.setChainName(i.getChainName());
			response.setClientName(clientName);
			response.setDate(i.getDate());
			response.setFineWeight(i.getFineWeight());
			response.setGrossWeight(i.getGrossWeight());
			response.setTouch(i.getTouch());
			response.setYourTouch(i.getYourTouch());
			responseList.add(response);
		}
		return responseList;
	}

	public List<GetDebitTransactionResponse> getTodaysDebitRecords(GetTransactionSupport getTransactionSupport) {

		try {
			List<ClientDetail> clientDetailList = clientDetailRepository.findAll();

			List<GetDebitTransactionResponse> todaysList = new ArrayList<>();
			Date date = null;

			try {
				date = new SimpleDateFormat("dd/MM/yyyy").parse(getTransactionSupport.getDate());
			} catch (ParseException e) {
				logger.info("Error - " + e.getMessage());
				e.printStackTrace();
			}
			for (ClientDetail eachClient : clientDetailList) {
				String tableNameDebit = eachClient.getValue() + "_Debit";
				Query query = new Query();
				query.addCriteria(Criteria.where("date").gte(date).lte(date));
				try {
					List<Debit> listDebit = mongo.find(query, Debit.class, tableNameDebit);
					todaysList.addAll(buildDebitResponse(listDebit, eachClient.getName()));
				} catch (Exception e) {
					logger.info("Null : " + e);
				}
			}

			return todaysList;
		} catch (Exception e) {
			logger.info("error " + e);
			throw e;
		}
	}

	List<GetDebitTransactionResponse> buildDebitResponse(List<Debit> listCredit, String clientName) {

		List<GetDebitTransactionResponse> responseList = new ArrayList<>();
		for (Debit i : listCredit) {
			GetDebitTransactionResponse response = new GetDebitTransactionResponse();
			response.setBalance(i.getBalance());
			response.setClientName(clientName);
			response.setDate(i.getDate());
			response.setGoldInfo(i.getGoldInfo());
			response.setFineWeight(i.getFineWeight());
			response.setGrossWeight(i.getGrossWeight());
			response.setPurityTouch(i.getPurityTouch());
			responseList.add(response);
		}
		return responseList;
	}

}
