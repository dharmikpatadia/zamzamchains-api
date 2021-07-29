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

import com.victorem.zamzamchains.retail.api.support.AddBalanceSupport;
import com.victorem.zamzamchains.retail.api.support.GetLastEntrySupport;
import com.victorem.zamzamchains.retail.api.support.PrintSupport;
import com.victorem.zamzamchains.retail.document.Credit;
import com.victorem.zamzamchains.retail.document.Debit;
import com.victorem.zamzamchains.retail.model.LastEntry;
import com.victorem.zamzamchains.retail.repository.RetailMonthEndRepository;

@RestController
@RequestMapping("/api/v1.0/retail")
public class RetailMonthEndAPI {

	final static Logger logger = LogManager.getLogger(RetailMonthEndAPI.class);

	@Autowired
	private RetailMonthEndRepository repository;

	@PostMapping("/checkBalance")
	public ResponseEntity<LastEntry> getBalance(@RequestBody GetLastEntrySupport printSupport,
			HttpServletRequest request) {
		logger.info("Get Last Entry IP : " + request.getRemoteAddr());
		LastEntry response = null;
		try {
			response = repository.getLastEntry(printSupport);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/printCredit")
	public ResponseEntity<List<Credit>> getCreditRecords(@RequestBody PrintSupport printSupport,
			HttpServletRequest request) {
		logger.info("Print Credit IP : " + request.getRemoteAddr());
		List<Credit> listCredit = null;
		try {
			listCredit = repository.getCreditPrint(printSupport);
			return new ResponseEntity<>(listCredit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(listCredit, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/printDebit")
	public ResponseEntity<List<Debit>> getDebitRecords(@RequestBody PrintSupport printSupport,
			HttpServletRequest request) {
		logger.info("Print Debit IP : " + request.getRemoteAddr());
		List<Debit> listDebit = null;
		try {
			listDebit = repository.getDebitPrint(printSupport);
			return new ResponseEntity<>(listDebit, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(listDebit, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/addBalance")
	public ResponseEntity<String> adBalanceRecord(@RequestBody AddBalanceSupport addBalanceSupport,
			HttpServletRequest request) {
		logger.info("ADD Balance IP : " + request.getRemoteAddr());
		boolean flag = repository.balanceEntry(addBalanceSupport);
		if (flag) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
