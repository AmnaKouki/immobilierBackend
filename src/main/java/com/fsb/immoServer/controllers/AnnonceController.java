package com.fsb.immoServer.controllers;

import java.io.File;
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

import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import org.springframework.core.io.InputStreamResource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequestMapping("/api/annonces")
@RestController
public class AnnonceController {

    @Autowired
    AnnonceRepository annonceRepository;

    String UPLOAD_PATH = "C:\\Users\\Amna\\Desktop\\ProjetWeb\\images\\";

    @GetMapping("")
    public List<Annonce> getAllAnnonces() {
        return this.annonceRepository.findAll();
    }

    @PostMapping("/add")
    public Annonce addAnnonce(@RequestBody Annonce annonce) {
        return this.annonceRepository.save(annonce);
    }

    @GetMapping("/{id}")
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
            // annonce.setDateCreation(newAnnonce.getDateCreation());

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
        // return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
        // return a json
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Deleted successfully");
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    public String uploadImageToFileSystem(MultipartFile file, String annonceId) throws IOException {
        String imageName = annonceId + "_" + file.getOriginalFilename();
        String filePath = this.UPLOAD_PATH + imageName;

        System.err.println("============ filePath = " + filePath);

        try {
            file.transferTo(new File(filePath));
            return imageName;
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/uploadImage/{id}")
    public ResponseEntity<?> addImageToAnnonce(@PathVariable String id, @RequestParam("image") MultipartFile file)
            throws IOException {
        if (this.annonceRepository.findById(id).isPresent()) {
            Annonce annonce = this.annonceRepository.findById(id).get();
            String imagePath = ImageController.uploadImageToFileSystem(file, id);

            List<String> newPhotoList = annonce.getPhotos();
            newPhotoList.addLast(imagePath);

            annonce.setPhotos(newPhotoList);
            System.out.println(annonce.getPhotos());

            this.annonceRepository.save(annonce);

            return new ResponseEntity<>(annonce, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // @GetMapping("/getImages/{id}")
    // public List<byte[]> getByteImageList(@PathVariable String id) throws
    // IOException {
    // Optional<Annonce> optionalAnnonce = this.annonceRepository.findById(id);

    // if (optionalAnnonce.isEmpty()) {
    // return null;
    // }
    // List<String> imgPathList = optionalAnnonce.get().getPhotos();
    // List<byte[]> result = new ArrayList<>();
    // for (String imgPath : imgPathList) {
    // result.add(ImageController.getImageFromFileSystem(imgPath));
    // }
    // return result;

    // }

    // @GetMapping("/findJson/{id}")
    // public ResponseEntity<?> getAnnonceByIdFormatJson(@PathVariable String id) {
    // if (this.annonceRepository.findById(id).isPresent()) {
    // Annonce annonce = this.annonceRepository.findById(id).get();
    // JSONObject annonceJson = getAnnonceWithImageFormatJson(annonce);
    // return new ResponseEntity<>(annonceJson, HttpStatus.OK);
    // } else {
    // return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    // }
    // }

    // private JSONObject getAnnonceWithImageFormatJson(Annonce annonce) {
    // JSONObject jsonObject = new JSONObject();
    // List<byte[]> images;
    // try {
    // images = this.getByteImageList(annonce.getId());

    // jsonObject.put("type", annonce.getType());
    // jsonObject.put("categorieImmo", annonce.getCategorieImmo());
    // jsonObject.put("titre", annonce.getTitre());
    // jsonObject.put("description", annonce.getDescription());
    // jsonObject.put("prix", annonce.getPrix());
    // jsonObject.put("surface", annonce.getSurface());
    // jsonObject.put("prix", annonce.getPrix());
    // jsonObject.put("nbPieces", annonce.getNbPieces());
    // jsonObject.put("caracteristiques", annonce.getCaracteristiques());
    // jsonObject.put("region", annonce.getRegion());
    // jsonObject.put("contact", annonce.getContact());
    // // jsonObject.put("dateCreation", annonce.getDateCreation());
    // jsonObject.put("photos", images);

    // return jsonObject;

    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // return null;
    // }

    @GetMapping("/download/{filename}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(this.UPLOAD_PATH + filename);
            String mimeType = Files.probeContentType(filePath);
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(filePath));

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(resource);
        } catch (Exception e) {
            // handle exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // remove file from an
    @DeleteMapping("/{id}/image/delete/{filename}")
    public ResponseEntity<?> deleteImageFromAnnonce(@PathVariable String id, @PathVariable String filename) {
        if (this.annonceRepository.findById(id).isPresent()) {
            Annonce annonce = this.annonceRepository.findById(id).get();
            List<String> newPhotoList = annonce.getPhotos();
            newPhotoList.remove(filename);

            annonce.setPhotos(newPhotoList);
            this.annonceRepository.save(annonce);

            return new ResponseEntity<>(annonce, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
