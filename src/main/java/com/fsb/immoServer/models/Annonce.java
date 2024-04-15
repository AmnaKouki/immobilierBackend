package com.fsb.immoServer.models;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "annonces")
public class Annonce {
    @Id
    // @Field("_id") // to use the auto-generated id from MongoDB
    private String id;

    private String type;
    private String categorieImmo;
    private String titre;
    private String description;
    private int surface;
    private int prix;
    private int nbPieces;
    private List<String> caracteristiques;
    private List<String> photos;
    private String region;
    private String adresse;
    private Contact contact;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
