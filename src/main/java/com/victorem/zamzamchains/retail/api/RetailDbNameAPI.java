package com.victorem.zamzamchains.retail.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.victorem.zamzamchains.retail.api.support.NewChainSupport;
import com.victorem.zamzamchains.retail.document.RetailClientDetail;
import com.victorem.zamzamchains.retail.model.AddClient;
import com.victorem.zamzamchains.retail.repository.ChainNameRepository;
import com.victorem.zamzamchains.retail.repository.RetailClientDetailRepository;
import com.victorem.zamzamchains.retail.service.RetailDbNameService;

@RestController
@RequestMapping("/api/v1.0")
public class RetailDbNameAPI {

	final static Logger logger = LogManager.getLogger(RetailDbNameAPI.class);

	@Autowired
	private RetailClientDetailRepository repository;
	
	@Autowired
	private RetailDbNameService dbNameService;

	@PostMapping("/getClients")
	public List<RetailClientDetail> getUsers( HttpServletRequest request) {
		logger.info("Get Clients IP : " + request.getRemoteAddr());
		return repository.findAll();
	}
	
	@PostMapping("/addClient")
	public ResponseEntity<String> addUser(@RequestBody NewChainSupport chainName,HttpServletRequest request) {
		logger.info("Add Clients IP : " + request.getRemoteAddr()+" : "+chainName.getChainName());
		boolean flag = dbNameService.addChainToDb(chainName);
		if (flag) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PostMapping("/deleteClient")
	public ResponseEntity<String> removeUser(@RequestBody AddClient addClient) {
		logger.info("Delete Client "+addClient.getClientName());
		boolean flag = dbNameService.removeClientFromDb(addClient);
		if (flag) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
