package com.victorem.zamzamchains.retail.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.victorem.zamzamchains.factory.document.ClientDetail;
import com.victorem.zamzamchains.retail.api.support.NewChainSupport;
import com.victorem.zamzamchains.retail.document.ChainName;
import com.victorem.zamzamchains.retail.document.RetailClientDetail;
import com.victorem.zamzamchains.retail.model.AddClient;
import com.victorem.zamzamchains.retail.repository.ChainNameRepository;
import com.victorem.zamzamchains.retail.repository.RetailClientDetailRepository;

@Service
public class DbNameService {

	final static Logger logger = LogManager.getLogger(DbNameService.class);

	@Autowired
	private RetailClientDetailRepository repository;
	
	@Autowired
	private ChainNameRepository chainNameRepository;

	@Autowired
	private MongoOperations mongo;
	
	public boolean addChainToDb(NewChainSupport chain) {
		try {
			ChainName newChain = new ChainName();
			
			String value = chain.getChainName().replaceAll("\\s", "").toLowerCase();
			newChain.setChainName(value);
			List<ChainName> chainList = chainNameRepository.findAll();
			boolean duplicate = false;
			for (ChainName eachClient : chainList) {
				if (value.equals(eachClient.getChainName())) {
					duplicate = true;
					break;
				}
			}
			if (duplicate) {
				logger.info("Duplicate");
				return false;
			} else {
				chainNameRepository.save(newChain);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean removeClientFromDb(AddClient addClient) {
		try {
			RetailClientDetail clientDetail = new RetailClientDetail();
			clientDetail.setName(addClient.getClientName());
			String value = addClient.getClientName().replaceAll("\\s", "").toLowerCase();
			clientDetail.setValue(value);
			List<RetailClientDetail> clientList = repository.findAll();
			boolean duplicate = false;
			Query query = new Query();
			String tableName = "retailClientDetail";

			for (RetailClientDetail eachClient : clientList) {
				logger.info("Cleint : " + eachClient);
				if (value.equals(eachClient.getValue())) {
					clientDetail.setId(eachClient.getId());

					query.addCriteria(Criteria.where("_id").is(eachClient.getId()));
					ClientDetail removed = mongo.findAndRemove(query, ClientDetail.class, tableName);
					logger.info("removed " + removed);
					duplicate = true;

					logger.info("Deele " + clientDetail);
				}
			}
			try {
				mongo.dropCollection(value + "_Credit_Retail");
				mongo.dropCollection(value + "_Debit_Retail");
			} catch (Exception e) {
				duplicate = false;
			}
			return duplicate;
		} catch (Exception e) {
			logger.info("Exc " + e);
			return false;
		}
	}
}
