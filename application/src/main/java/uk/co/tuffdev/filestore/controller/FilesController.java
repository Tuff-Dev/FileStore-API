package uk.co.tuffdev.filestore.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.co.tuffdev.filestore.FileDirectoryService;
import uk.co.tuffdev.filestore.ResourceNotFoundException;
import uk.co.tuffdev.filestore.StorageService;
import uk.co.tuffdev.filestore.auth.CurrentUser;
import uk.co.tuffdev.filestore.auth.UserPrincipal;
import uk.co.tuffdev.filestore.exception.StorageFileNotFoundException;

@Controller
@RequestMapping("/files")
public class FilesController {

    private final StorageService storageService;
    private final FileDirectoryService fileDirectoryService;

    public FilesController(StorageService storageService, FileDirectoryService fileDirectoryService) {
        this.storageService = storageService;
        this.fileDirectoryService = fileDirectoryService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile file, @RequestParam("path") String path, @CurrentUser UserPrincipal user) {
        fileDirectoryService.uploadFile(file, user.getId(), path);
        return ResponseEntity.accepted().body(null);
    }

    @GetMapping()
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@RequestParam("file") String filename, @RequestParam("path") String path, @CurrentUser UserPrincipal user) throws ResourceNotFoundException {

//        Resource file = storageService.loadAsResource(user.getId(), filename);
        Resource file = fileDirectoryService.getFile(filename, path, user.getId());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"")
                .body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageException(StorageFileNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

}
