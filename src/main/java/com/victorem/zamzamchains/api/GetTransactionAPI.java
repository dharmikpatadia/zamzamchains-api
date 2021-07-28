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

import com.victorem.zamzamchains.api.support.GetTransactionResponse;
import com.victorem.zamzamchains.api.support.GetTransactionSupport;
import com.victorem.zamzamchains.document.Credit;
import com.victorem.zamzamchains.service.GetTransactionService;

@RestController
@RequestMapping("/api/v1.0")
public class GetTransactionAPI {
	
	final static Logger logger = LogManager.getLogger(GetTransactionAPI.class);
	
	@Autowired
	private GetTransactionService getTransactionService;
	
	@PostMapping("/getTodaysCredit")
	public ResponseEntity<List<GetTransactionResponse>> getTodaysCreditRecords(@RequestBody GetTransactionSupport getTransactionSupport,
			HttpServletRequest request) {
		logger.info("Print Credit IP : " + request.getRemoteAddr());
		List<GetTransactionResponse> listCredit = null;
		try {
			listCredit = getTransactionService.getTodaysCreditRecords(getTransactionSupport);
			return new ResponseEntity<>(listCredit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(listCredit, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
