package uk.co.tuffdev.filestore;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;


public interface StorageService {

    void init();

    void save(MultipartFile multipartFile);
//    void save(MultipartFile multipartFile, String path, User user);

    Resource loadAsResource(String fileName);

    Path load(String filename);

}
