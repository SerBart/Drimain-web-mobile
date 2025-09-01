package drimer.drimain.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Service for managing physical file storage.
 * Handles upload, download, and deletion of attachment files.
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${app.file.upload-dir:./uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /**
     * Store a file and return the stored filename.
     */
    public String storeFile(MultipartFile file) throws IOException {
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String storedFilename = UUID.randomUUID().toString() + extension;
        
        // Copy file to the target location
        Path targetLocation = this.fileStorageLocation.resolve(storedFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        return storedFilename;
    }

    /**
     * Load a file as a Path object.
     */
    public Path loadFile(String filename) {
        return fileStorageLocation.resolve(filename).normalize();
    }

    /**
     * Delete a file from storage.
     */
    public boolean deleteFile(String filename) {
        try {
            Path filePath = loadFile(filename);
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Calculate SHA-256 checksum of a file.
     */
    public String calculateChecksum(MultipartFile file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = file.getBytes();
            byte[] hash = digest.digest(bytes);
            
            // Convert to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            return null;
        }
    }

    /**
     * Check if a file exists in storage.
     */
    public boolean fileExists(String filename) {
        Path filePath = loadFile(filename);
        return Files.exists(filePath) && Files.isRegularFile(filePath);
    }
}