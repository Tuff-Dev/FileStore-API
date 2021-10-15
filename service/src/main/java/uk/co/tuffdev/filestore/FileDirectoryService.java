package uk.co.tuffdev.filestore;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileDirectoryService {

    UserFolder getUserFiles(String userId);

    void uploadFile(MultipartFile multipartFile, String userId, String path);

    Resource getFile(String fileName, String path, String userId) throws ResourceNotFoundException;
//    void createFolder(String folderPath, String folderName);

}
