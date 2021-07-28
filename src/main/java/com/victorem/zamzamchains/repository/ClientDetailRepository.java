package com.victorem.zamzamchains.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.victorem.zamzamchains.document.ClientDetail;

public interface ClientDetailRepository extends MongoRepository<ClientDetail, Integer> {

}
