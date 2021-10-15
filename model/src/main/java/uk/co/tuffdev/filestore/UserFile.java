package uk.co.tuffdev.filestore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserFile {

    private String internalFileReference;

    private String fileName;

    private String userId;

    private LocalDateTime created;

}
