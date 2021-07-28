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

import com.victorem.zamzamchains.api.support.GetTransactionResponse;
import com.victorem.zamzamchains.api.support.GetTransactionSupport;
import com.victorem.zamzamchains.document.ClientDetail;
import com.victorem.zamzamchains.document.Credit;
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

	@SuppressWarnings("null")
	public List<GetTransactionResponse> getTodaysCreditRecords(GetTransactionSupport getTransactionSupport) {

		try {
			List<ClientDetail> clientDetailList = clientDetailRepository.findAll();

			List<GetTransactionResponse> todaysList = new ArrayList<>();
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
				List<Credit> listCredit = mongo.find(query, Credit.class, tableNameCredit);
				todaysList.addAll(buildResponse(listCredit,eachClient.getName()));
			}

			return todaysList;
		} catch (Exception e) {
			logger.info("error " + e);
			throw e;
		}
	}

	List<GetTransactionResponse> buildResponse(List<Credit> listCredit,String clientName) {

		List<GetTransactionResponse> responseList=new ArrayList<>();
		for(Credit i: listCredit) {
			GetTransactionResponse response=new GetTransactionResponse();
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

}
