package com.victorem.zamzamchains.factory.api;

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

import com.victorem.zamzamchains.factory.api.support.CreditDebitResponse;
import com.victorem.zamzamchains.factory.api.support.DebitSupport;
import com.victorem.zamzamchains.factory.repository.DefaultDebitRepository;

@RestController
@RequestMapping("/api/v1.0/factory")
public class DebitAPI {

	final static Logger logger = LogManager.getLogger(DbNameAPI.class);
	
	@Autowired
	private DefaultDebitRepository repository;

	@PostMapping("/debit")
	public ResponseEntity<CreditDebitResponse> addRecord(@RequestBody DebitSupport debitSupport,
			HttpServletRequest request) {
		logger.info("ADD Debit IP : " + request.getRemoteAddr());
		CreditDebitResponse response = null;
		try {
			response = repository.addDebitRecord(debitSupport);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
