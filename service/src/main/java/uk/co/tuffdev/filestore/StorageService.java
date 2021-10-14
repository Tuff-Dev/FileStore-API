package uk.co.tuffdev.filestore;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void save(MultipartFile multipartFile);
//    void save(MultipartFile multipartFile, String path, User user);

}
