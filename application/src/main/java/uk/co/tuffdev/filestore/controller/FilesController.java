package uk.co.tuffdev.filestore.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import uk.co.tuffdev.filestore.StorageService;

@Controller
@RequestMapping("/files")
public class FilesController {

    private final StorageService storageService;

    public FilesController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile file) {
        storageService.save(file);
        return ResponseEntity.accepted().body(null);
    }

}
