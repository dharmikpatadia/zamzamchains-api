package com.victorem.zamzamchains.factory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.victorem.zamzamchains.factory.document.ClientDetail;

public interface ClientDetailRepository extends MongoRepository<ClientDetail, Integer> {

}
