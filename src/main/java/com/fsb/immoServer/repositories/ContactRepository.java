package com.fsb.immoServer.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.fsb.immoServer.models.Contact;

public interface ContactRepository extends MongoRepository<Contact,String>{

}
