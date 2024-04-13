package com.fsb.immoServer.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.fsb.immoServer.models.Annonce;

public interface AnnonceRepository extends MongoRepository<Annonce,String>{
    // public List<Annonce> findAll();
    
}
