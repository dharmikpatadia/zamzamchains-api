package com.victorem.zamzamchains.factory.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.victorem.zamzamchains.factory.document.ChainDetail;
import com.victorem.zamzamchains.factory.document.ClientDetail;
import com.victorem.zamzamchains.factory.model.AddClient;
import com.victorem.zamzamchains.factory.repository.ChainDetailRepository;
import com.victorem.zamzamchains.factory.repository.ClientDetailRepository;

@Service
public class DbNameService {

	final static Logger logger = LogManager.getLogger(DbNameService.class);

	@Autowired
	private ClientDetailRepository clientRepository;
	@Autowired
	private ChainDetailRepository chainRepository;

	@Autowired
	private MongoOperations mongo;

	public boolean addClientToDb(AddClient addClient) {
		try {
			ClientDetail clientDetail = new ClientDetail();
			clientDetail.setName(addClient.getClientName());
			String value = addClient.getClientName().replaceAll("\\s", "").toLowerCase();
			clientDetail.setValue(value);
			List<ClientDetail> clientList = clientRepository.findAll();
			boolean duplicate = false;
			for (ClientDetail eachClient : clientList) {
				if (value.equals(eachClient.getValue())) {
					duplicate = true;
					break;
				}
			}
			if (duplicate) {
				logger.info("Duplicate");
				return false;
			} else {
				clientRepository.save(clientDetail);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean removeClientFromDb(AddClient addClient) {
		try {
			ClientDetail clientDetail = new ClientDetail();
			clientDetail.setName(addClient.getClientName());
			String value = addClient.getClientName().replaceAll("\\s", "").toLowerCase();
			clientDetail.setValue(value);
			List<ClientDetail> clientList = clientRepository.findAll();
			boolean duplicate = false;
			Query query = new Query();
			String tableName = "clientDetail";

			for (ClientDetail eachClient : clientList) {
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
				mongo.dropCollection(value + "_Credit");
				mongo.dropCollection(value + "_Debit");
			} catch (Exception e) {
				duplicate = false;
			}
			return duplicate;
		} catch (Exception e) {
			logger.info("Exc " + e);
			return false;
		}
	}
	
	public boolean addChainToDb(AddClient addClient) {
		try {
			ChainDetail chainDetail = new ChainDetail();
			chainDetail.setName(addClient.getClientName());
			String value = addClient.getClientName().replaceAll("\\s", "").toLowerCase();
			chainDetail.setValue(value);
			List<ChainDetail> clientList = chainRepository.findAll();
			boolean duplicate = false;
			for (ChainDetail eachClient : clientList) {
				if (value.equals(eachClient.getValue())) {
					duplicate = true;
					break;
				}
			}
			if (duplicate) {
				logger.info("Duplicate");
				return false;
			} else {
				chainRepository.save(chainDetail);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean removeChainFromDb(AddClient addClient) {
		try {
			ChainDetail chainDetail = new ChainDetail();
			chainDetail.setName(addClient.getClientName());
			String value = addClient.getClientName().replaceAll("\\s", "").toLowerCase();
			chainDetail.setValue(value);
			List<ChainDetail> clientList = chainRepository.findAll();
			boolean duplicate = false;
			Query query = new Query();
			String tableName = "chainDetail";

			for (ChainDetail eachClient : clientList) {
				logger.info("Cleint : " + eachClient);
				if (value.equals(eachClient.getValue())) {
					chainDetail.setId(eachClient.getId());

					query.addCriteria(Criteria.where("_id").is(eachClient.getId()));
					ChainDetail removed = mongo.findAndRemove(query, ChainDetail.class, tableName);
					logger.info("removed " + removed);
					duplicate = true;

				}
			}
			return duplicate;
		} catch (Exception e) {
			logger.info("Exc " + e);
			return false;
		}
	}
}
