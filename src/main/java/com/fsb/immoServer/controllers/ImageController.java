package com.fsb.immoServer.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ImageController {

    public static String FOLDER_PATH = "C:\\Users\\Amna\\Desktop\\ProjetWeb\\images\\";

    public static String uploadImageToFileSystem(MultipartFile file, String annonceId) throws IOException {
        String imageName = annonceId + "_" + file.getOriginalFilename();
        String filePath = FOLDER_PATH + imageName;

        System.err.println("============ filePath = " + filePath);

        try {
            file.transferTo(new File(filePath));
            return imageName;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] getImageFromFileSystem(String path) {
        try {
            byte[] imageBytes = Files.readAllBytes(new File(path).toPath());
            return imageBytes;
        } catch (IOException e) {
            return null;
        }
    }

    @PostMapping("/image/upload")
    public ResponseEntity<String> uploadImageToFileSystemApi(@RequestParam("image") MultipartFile file)
            throws IOException {
        String filePath = FOLDER_PATH + file.getOriginalFilename();

        try {
            file.transferTo(new File(filePath));

            return ResponseEntity.ok().body("file uploaded successfully : " + filePath);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not upload the file: ");
        }
    }

    @GetMapping("/image/{name}")
    public ResponseEntity<byte[]> getImageFromFileSystemApi(@PathVariable String name) {
        String path = FOLDER_PATH + name;
        System.out.println("path = " + path);
        try {
            byte[] imageBytes = Files.readAllBytes(new File(path).toPath());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(imageBytes);
            // return ResponseEntity.ok().body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Could not read the file: " + path).getBytes());
        }
    }

    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }
        try {
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4 * 1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            outputStream.close();
        } catch (Exception ignored) {
        }
        return outputStream.toByteArray();
    }

}
