package uk.co.tuffdev.filestore;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Getter
@Setter
public class UserFolder {

    @Id
    private String folderId;

    private String folderName;

    private Set<UserFile> files;

}
