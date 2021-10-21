package uk.co.tuffdev.filestore;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import uk.co.tuffdev.filestore.config.StorageProperties;
import uk.co.tuffdev.filestore.exception.StorageFileNotFoundException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemStorageServiceTest {

    private final StorageProperties testStorageProperties = new StorageProperties();
    private static final String TEST_USER = "test-user";
    private StorageService storageService;

    @BeforeEach
    public void setup() {
        testStorageProperties.setLocation(".tests");
        storageService = new FileSystemStorageService(testStorageProperties);
    }

    @AfterEach
    public void cleanup() throws IOException {
        // Remove tempt file directory
        Path testPath = Paths.get(".tests/");
        FileUtils.deleteDirectory(testPath.toFile());
    }

    @Test
    public void testFileIsSaved() {
        MockMultipartFile testFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        storageService.save(testFile, TEST_USER, "file1.txt");

        Path result = storageService.load(TEST_USER, "file1.txt");

        assertNotNull(result);
        assertEquals("file1.txt", result.getFileName().toString());
    }

    @Test
    public void testFileCanBeRetrieved() throws IOException {
        MockMultipartFile testFile = new MockMultipartFile("data", "filename2.txt", "text/plain", "some xml".getBytes());
        storageService.save(testFile, TEST_USER, "file2.txt");

        Resource resource = storageService.loadAsResource(TEST_USER, "file2.txt");
        assertNotNull(resource);
        assertTrue(resource.getFile().exists());
        System.out.println(resource.getFile());


    }

    @Test
    public void testFileNotFound() {
        Exception e = assertThrows((StorageFileNotFoundException.class), () -> {
            storageService.loadAsResource(TEST_USER, "some-file.csv");
        });
        assertNotNull(e);
    }


}