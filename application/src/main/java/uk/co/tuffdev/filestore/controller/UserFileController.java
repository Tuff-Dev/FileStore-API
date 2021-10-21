package uk.co.tuffdev.filestore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.tuffdev.filestore.FileDirectoryService;
import uk.co.tuffdev.filestore.UserFolder;
import uk.co.tuffdev.filestore.auth.CurrentUser;
import uk.co.tuffdev.filestore.auth.UserPrincipal;

@RestController
@RequestMapping("/user-files")
public class UserFileController {

    private final FileDirectoryService fileDirectoryService;

    public UserFileController(FileDirectoryService fileDirectoryService) {
        this.fileDirectoryService = fileDirectoryService;
    }

    @GetMapping
    public ResponseEntity<UserFolder> getUserFileStructure(@CurrentUser UserPrincipal user) {
        return ResponseEntity.ok(fileDirectoryService.getUserFiles(user.getId()));
    }

}
