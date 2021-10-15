package uk.co.tuffdev.filestore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service("fileDirectoryServiceImpl")
public class FileDirectoryServiceImpl implements FileDirectoryService {

    private static final Logger LOGGER = LogManager.getLogger(FileDirectoryServiceImpl.class);

    private final UserFolderRepository userFolderRepository;
    private final StorageService storageService;

    public FileDirectoryServiceImpl(UserFolderRepository userFolderRepository, StorageService storageService) {
        this.userFolderRepository = userFolderRepository;
        this.storageService = storageService;
    }

    @Override
    public UserFolder getUserFiles(String userId) {
        Set<UserFolder> userFolders =  userFolderRepository.getByUserIdAndUserRootDirIsTrue(userId);

        if (userFolders.size() == 0) {
            return createUserBaseFolder(userId);
        }
        if (userFolders.size() > 1) {
            LOGGER.warn("A user should only have 1 root folder.");
        }

        Iterator<UserFolder> iterator = userFolders.iterator();
        return iterator.next();
    }

    private UserFolder createUserBaseFolder(String userId) {
        UserFolder userFolder = new UserFolder();
        userFolder.setUserId(userId);
        userFolder.setUserRootDir(true);
        userFolder.setFiles(new HashSet<>());
        userFolder.setSubdirectories(new HashSet<>());
        userFolder.setCreated(LocalDateTime.now());
        return userFolderRepository.save(userFolder);
    }

    @Override
    public void uploadFile(MultipartFile multipartFile, String userId, String path) {

        UUID newFileId;

        // 1 - Load users file structure
        UserFolder userFolder = getUserFiles(userId);

        // 2 - Navigate to path
        UserFolder workingDir = userFolder;
        String[] folderPath = path.split("/");
        for (String folder: folderPath) {
            Optional<UserFolder> foundDir = workingDir.getSubdirectories().stream().filter(f -> f.getFolderName().equals(folder)).findFirst();
            if (foundDir.isEmpty()) {
                UserFolder newFolder = createFolderInline(folder);
                workingDir.getSubdirectories().add(newFolder);
                workingDir = newFolder;
            } else {
                workingDir = foundDir.get();
            }
        }

        // 3 - Add UserFile record into structure
        UserFile newFile;

        // Check to see if there is already a file with the same name. If so, replace
        String internalFileName;
        boolean isNewFile = true;
        Optional<UserFile> existingFileOptional = workingDir.getFiles().stream().filter(f -> f.getFileName().equals(multipartFile.getOriginalFilename())).findFirst();
        if (existingFileOptional.isPresent()) {
            internalFileName = existingFileOptional.get().getInternalFileReference();
            newFile = existingFileOptional.get();
            isNewFile = false;
        } else {
            // Append extension
            String existingFileName = multipartFile.getOriginalFilename();
            internalFileName = UUID.randomUUID() + existingFileName.substring(existingFileName.lastIndexOf("."));

            newFile = new UserFile();
            newFile.setFileName(multipartFile.getOriginalFilename());
            newFile.setUserId(userId);
            newFile.setInternalFileReference(internalFileName);
        }

        newFile.setCreated(LocalDateTime.now());

        storageService.save(multipartFile, userId, internalFileName);

        if (isNewFile) {
            workingDir.getFiles().add(newFile);
        }

        // 4 - Save structure
        userFolderRepository.save(userFolder);
    }

    private UserFolder createFolderInline(String folderName) {
        UserFolder newFolder = new UserFolder();
        newFolder.setFolderName(folderName);
        newFolder.setFiles(new HashSet<>());
        newFolder.setSubdirectories(new HashSet<>());
        newFolder.setUserRootDir(false);
        newFolder.setCreated(LocalDateTime.now());
        return newFolder;
    }
}
