package uk.co.tuffdev.filestore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserFile {

    @Id
    private String fileId;

    private String fileName;

    private String path;

    private String userId;

    private LocalDate created;

}
