package com.victorem.zamzamchains.retail.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.victorem.zamzamchains.retail.document.RetailClientDetail;

public interface RetailClientDetailRepository extends MongoRepository<RetailClientDetail, Integer> {

}
