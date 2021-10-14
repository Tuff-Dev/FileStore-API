package uk.co.tuffdev.filestore;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.co.tuffdev.filestore.config.StorageProperties;
import uk.co.tuffdev.filestore.exception.StorageException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service("fileSystemStorageService")
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    public FileSystemStorageService(StorageProperties storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getLocation());
    }

    @PostConstruct
    public void setup() {
        this.init();
    }

    @Override
    public void init() {
        // Create base directory
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Failed to initialise storage.");
        }
    }

    @Override
    public void save(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file");
        }

        Path destinationFile = this.rootLocation.resolve(
                Paths.get(createFileName(file.getOriginalFilename()))
        ).normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new StorageException("Cannot store file outside defined base directory.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file.");
        }

    }

    private String createFileName(String existingName) {
        // TODO: Append uniqueness

        if (existingName != null) {
            return existingName;
        }
        // Generate random name
        return "test";
    }
}
