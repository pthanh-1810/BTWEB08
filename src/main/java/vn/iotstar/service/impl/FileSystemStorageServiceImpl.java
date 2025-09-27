package vn.iotstar.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.config.StorageProperties;
import vn.iotstar.exception.StorageException;
import vn.iotstar.exception.StorageFileNotFoundException;
import vn.iotstar.service.IStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
public class FileSystemStorageServiceImpl implements IStorageService {

    private final Path rootLocation;

    public FileSystemStorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public String getStorageFilename(MultipartFile file, String id) {
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        return id + "." + (ext != null ? ext : "png");
    }

    @Override
    public void store(MultipartFile file, String filename) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(filename))
                    .normalize().toAbsolutePath();
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path file = load(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageException("Could not delete file: " + filename, e);
        }
    }
}
