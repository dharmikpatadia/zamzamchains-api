package com.victorem.zamzamchains.api;

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

import com.victorem.zamzamchains.api.support.GetCreditTransactionResponse;
import com.victorem.zamzamchains.api.support.GetDebitTransactionResponse;
import com.victorem.zamzamchains.api.support.GetMonthsTransactionResponse;
import com.victorem.zamzamchains.api.support.GetMonthsTransactionSupport;
import com.victorem.zamzamchains.api.support.GetTransactionSupport;
import com.victorem.zamzamchains.service.GetTransactionService;

@RestController
@RequestMapping("/api/v1.0")
public class GetTransactionAPI {
	
	final static Logger logger = LogManager.getLogger(GetTransactionAPI.class);
	
	@Autowired
	private GetTransactionService getTransactionService;
	
	@PostMapping("/getTodaysCredit")
	public ResponseEntity<List<GetCreditTransactionResponse>> getTodaysCreditRecords(@RequestBody GetTransactionSupport getTransactionSupport,
			HttpServletRequest request) {
		logger.info("Print Credit IP : " + request.getRemoteAddr());
		List<GetCreditTransactionResponse> listCredit = null;
		try {
			listCredit = getTransactionService.getTodaysCreditRecords(getTransactionSupport);
			return new ResponseEntity<>(listCredit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(listCredit, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/getTodaysDebit")
	public ResponseEntity<List<GetDebitTransactionResponse>> getTodaysDebitRecords(@RequestBody GetTransactionSupport getTransactionSupport,
			HttpServletRequest request) {
		logger.info("Print Credit IP : " + request.getRemoteAddr());
		List<GetDebitTransactionResponse> listDebit = null;
		try {
			listDebit = getTransactionService.getTodaysDebitRecords(getTransactionSupport);
			return new ResponseEntity<>(listDebit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(listDebit, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/getMonthsTransaction")
	public ResponseEntity<List<GetMonthsTransactionResponse>> getMonthsTransactionRecords(@RequestBody GetMonthsTransactionSupport getTransactionSupport,
			HttpServletRequest request) {
		logger.info("Print Credit IP : " + request.getRemoteAddr());
		List<GetMonthsTransactionResponse> listCredit = null;
		try {
			listCredit = getTransactionService.getMonthsTransactionRecords(getTransactionSupport);
			return new ResponseEntity<>(listCredit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(listCredit, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
