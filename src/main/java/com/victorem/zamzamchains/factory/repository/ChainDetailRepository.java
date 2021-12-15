package com.victorem.zamzamchains.factory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.victorem.zamzamchains.factory.document.ChainDetail;

public interface ChainDetailRepository extends MongoRepository<ChainDetail, Integer> {

}
