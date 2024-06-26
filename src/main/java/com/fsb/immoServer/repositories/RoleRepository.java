package com.fsb.immoServer.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.fsb.immoServer.models.ERole;
import com.fsb.immoServer.models.Role;

public interface RoleRepository extends MongoRepository<Role, String>{
    Optional<Role> findByName(ERole name);
}
