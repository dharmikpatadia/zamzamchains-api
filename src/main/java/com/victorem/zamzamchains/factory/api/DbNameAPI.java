package com.victorem.zamzamchains.factory.api;

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

import com.victorem.zamzamchains.factory.document.ClientDetail;
import com.victorem.zamzamchains.factory.model.AddClient;
import com.victorem.zamzamchains.factory.repository.ClientDetailRepository;
import com.victorem.zamzamchains.factory.service.DbNameService;

@RestController
@RequestMapping("/api/v1.0/factory")
public class DbNameAPI {

	final static Logger logger = LogManager.getLogger(DbNameAPI.class);

	@Autowired
	private ClientDetailRepository repository;
	
	@Autowired
	private DbNameService dbNameService;

	@PostMapping("/getClients")
	public List<ClientDetail> getUsers( HttpServletRequest request) {
		logger.info("Get Clients IP : " + request.getRemoteAddr());
		return repository.findAll();
	}
	
	@PostMapping("/addClient")
	public ResponseEntity<String> addUser(@RequestBody AddClient addClient,HttpServletRequest request) {
		logger.info("Add Clients IP : " + request.getRemoteAddr()+" : "+addClient.getClientName());
		boolean flag = dbNameService.addClientToDb(addClient);
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
