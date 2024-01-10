package com.example.metainfo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.metainfo.model.MetaData;

public interface MetaDataRepository extends MongoRepository<MetaData, String>{
	

}
