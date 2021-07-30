package com.victorem.zamzamchains.retail.api;

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

import com.victorem.zamzamchains.retail.api.support.CreditDebitResponse;
import com.victorem.zamzamchains.retail.api.support.ChainSupport;
import com.victorem.zamzamchains.retail.repository.DefaultChainRetailRepository;

@RestController
@RequestMapping("/api/v1.0/retail")
public class RetailDebitAPI {

	final static Logger logger = LogManager.getLogger(RetailDbNameAPI.class);
	
	@Autowired
	private DefaultChainRetailRepository repository;

	@PostMapping("/debit")
	public ResponseEntity<CreditDebitResponse> addRecord(@RequestBody ChainSupport debitSupport,
			HttpServletRequest request) {
		logger.info("ADD Debit IP : " + request.getRemoteAddr());
		CreditDebitResponse response = null;
		try {
			response = repository.addChainRecord(debitSupport);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
