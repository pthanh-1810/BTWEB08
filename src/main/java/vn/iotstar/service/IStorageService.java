package vn.iotstar.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface IStorageService {
    void init();  // 👈 phải có để implement

    String getStorageFilename(MultipartFile file, String id);

    void store(MultipartFile file, String filename);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void delete(String filename);
}
