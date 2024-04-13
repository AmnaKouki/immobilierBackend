package com.fsb.immoServer.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fsb.immoServer.models.Annonce;
import com.fsb.immoServer.repositories.AnnonceRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/annonces")
@RestController
public class AnnonceController {

    @Autowired
    AnnonceRepository annonceRepository;

    @GetMapping("")
    public List<Annonce> getAllAnnonces() {
        return this.annonceRepository.findAll();
    }

    @PostMapping("/add")
    public Annonce addAnnonce(@RequestBody Annonce annonce) {
        return this.annonceRepository.save(annonce);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> getAnnonceById(@PathVariable String id) {
        if (this.annonceRepository.findById(id).isPresent()) {
            return new ResponseEntity<>(this.annonceRepository.findById(id).get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    

    @PostMapping("update/{id}")
    public ResponseEntity<?> updateAnnonce(@PathVariable String id, @RequestBody Annonce newAnnonce) {
        if (this.annonceRepository.findById(id).isPresent()) {
            Annonce annonce = this.annonceRepository.findById(id).get();
            annonce.setType(newAnnonce.getType());
            annonce.setCategorieImmo(newAnnonce.getCategorieImmo());
            annonce.setTitre(newAnnonce.getTitre());
            annonce.setDescription(newAnnonce.getDescription());
            annonce.setPrix(newAnnonce.getPrix());
            annonce.setSurface(newAnnonce.getSurface());
            annonce.setNbPieces(newAnnonce.getNbPieces());
            annonce.setCaracteristiques(newAnnonce.getCaracteristiques());
            annonce.setPhotos(newAnnonce.getPhotos());
            annonce.setRegion(newAnnonce.getRegion());
            annonce.setAdresse(newAnnonce.getAdresse());
            annonce.setContact(newAnnonce.getContact());
            annonce.setDateCreation(newAnnonce.getDateCreation());

            this.annonceRepository.save(annonce);

            return new ResponseEntity<>(annonce, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer une annonce par son Id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAnnonceByID(@PathVariable String id) {
        this.annonceRepository.deleteById(id);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }

    @PostMapping("add/image/{id}")
    public ResponseEntity<?> addImageToAnnonce(@PathVariable String id, @RequestParam("image") MultipartFile file) throws IOException {
        if (this.annonceRepository.findById(id).isPresent()) {
            Annonce annonce = this.annonceRepository.findById(id).get();
            String imagePath = ImageController.uploadImageToFileSystem(file, id);
            List<String> newPhotoList = annonce.getPhotos();
            newPhotoList.addLast(imagePath);
    
           annonce.setPhotos(newPhotoList );
            System.out.println(annonce.getPhotos());

            this.annonceRepository.save(annonce);

            return new ResponseEntity<>(annonce, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }








    @GetMapping("/image/{id}")
    public List<byte[]> getByteImageList(@PathVariable String id) throws IOException {
        Optional<Annonce> optionalAnnonce = this.annonceRepository.findById(id);

        if (optionalAnnonce.isEmpty()) {
            return null;
        }
        List<String> imgPathList = optionalAnnonce.get().getPhotos();
        List<byte[]> result = new ArrayList<>();
        for (String imgPath : imgPathList) {
            result.add(ImageController.getImageFromFileSystem(imgPath));
        }
        return result;

    }
    @GetMapping("/findJson/{id}")
    public ResponseEntity<?> getAnnonceByIdFormatJson(@PathVariable String id) {
        if (this.annonceRepository.findById(id).isPresent()) {
            Annonce anoonce = this.annonceRepository.findById(id).get();
            JSONObject annonceJson = getAnnonceWithImageFormatJson(anoonce);
            return new ResponseEntity<>(annonceJson, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    private JSONObject getAnnonceWithImageFormatJson(Annonce annonce) {
        JSONObject jsonObject = new JSONObject();
        List<byte[]> images;
        try {
            images = this.getByteImageList(annonce.getId());

            jsonObject.put("type", annonce.getType());
            jsonObject.put("categorieImmo", annonce.getCategorieImmo());
            jsonObject.put("titre", annonce.getTitre());
            jsonObject.put("description", annonce.getDescription());
            jsonObject.put("prix", annonce.getPrix());
            jsonObject.put("surface", annonce.getSurface());
            jsonObject.put("prix", annonce.getPrix());
            jsonObject.put("nbPieces", annonce.getNbPieces());
            jsonObject.put("caracteristiques", annonce.getCaracteristiques());
            jsonObject.put("region", annonce.getRegion());
            jsonObject.put("contact", annonce.getContact());
            jsonObject.put("dateCreation", annonce.getDateCreation());
            jsonObject.put("photos", images);

            return jsonObject;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
