package uk.co.tuffdev.filestore;

import org.springframework.web.multipart.MultipartFile;

public interface FileDirectoryService {

    UserFolder getUserFiles(String userId);

    void uploadFile(MultipartFile multipartFile, String userId, String path);

//    void createFolder(String folderPath, String folderName);

}
