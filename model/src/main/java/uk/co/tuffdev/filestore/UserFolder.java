package uk.co.tuffdev.filestore;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class UserFolder {

    @Id
    private String id;

    private String folderName;

    private Set<UserFile> files;

    private Set<UserFolder> subdirectories;

    private String userId;

    private Boolean userRootDir;

    private LocalDateTime created;

}
