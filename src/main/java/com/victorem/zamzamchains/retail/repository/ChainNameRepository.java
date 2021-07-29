package com.victorem.zamzamchains.retail.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.victorem.zamzamchains.retail.document.ChainName;

public interface ChainNameRepository extends MongoRepository<ChainName, Integer>{

}
