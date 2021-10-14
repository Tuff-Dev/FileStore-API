package uk.co.tuffdev.filestore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class User {

    @Id
    private String id;

    private String name;

    @Email
    private String email;

    private String imageUrl;

    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    private AuthProvider provider;

    private String providerId;

}
