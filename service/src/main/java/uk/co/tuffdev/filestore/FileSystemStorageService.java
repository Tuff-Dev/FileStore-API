package uk.co.tuffdev.filestore;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.co.tuffdev.filestore.config.StorageProperties;
import uk.co.tuffdev.filestore.exception.StorageException;
import uk.co.tuffdev.filestore.exception.StorageFileNotFoundException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
    public void save(MultipartFile file, String userId, String targetFileName) {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file");
        }

        Path destinationPath = createPath(this.rootLocation, userId);

        Path destinationFile = destinationPath.resolve(targetFileName).normalize().toAbsolutePath();

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file.");
        }

    }

    @Override
    public Path load(String userId, String filename) {
        return rootLocation.resolve(userId + "/" + filename);
    }

    @Override
    public Resource loadAsResource(String userId, String fileId) {
        try {
            Path file = load(userId, fileId);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not find file: " + fileId);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + fileId, e);
        }
    }

    private Path createPath(Path rootLocation, String userId) {
        Path destinationPath = rootLocation.resolve(
                Paths.get(userId)
        ).normalize().toAbsolutePath();

        Path relative = this.rootLocation.toAbsolutePath().relativize(destinationPath);

        if(!relative.startsWith(userId)) {
            throw new StorageException("Can only store files inside users own directory.");
        }

        try {
            Files.createDirectories(destinationPath);
        } catch (IOException e) {
            throw new StorageException("Filed to create directory structure.", e);
        }

        return destinationPath;
    }
}
