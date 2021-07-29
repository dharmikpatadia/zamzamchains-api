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

import com.victorem.zamzamchains.retail.api.support.DeleteMultipleSupport;
import com.victorem.zamzamchains.retail.api.support.DeleteSingleSupport;
import com.victorem.zamzamchains.retail.repository.DeleteRepository;


@RestController
@RequestMapping("/api/v1.0")
public class DeleteAPI {

	final static Logger logger = LogManager.getLogger(DeleteAPI.class);

	@Autowired
	private DeleteRepository repository;
	
	@PostMapping("/deleteSingle")
	public ResponseEntity<String> removeRecord(@RequestBody DeleteSingleSupport deleteSupport, HttpServletRequest request) {
		logger.info("Delete Single IP : " + request.getRemoteAddr());
		boolean flag= repository.deleteSingleRecord(deleteSupport);
		if (flag) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/deleteMulti")
	public ResponseEntity<String> removeRecord(@RequestBody DeleteMultipleSupport deleteSupport, HttpServletRequest request) {
		logger.info("Delete Multiple IP : " + request.getRemoteAddr());
		boolean flag= repository.deleteMultipleRecord(deleteSupport);
		if (flag) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
