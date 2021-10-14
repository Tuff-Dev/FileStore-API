package uk.co.tuffdev.filestore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.tuffdev.filestore.auth.CurrentUser;
import uk.co.tuffdev.filestore.auth.UserPrincipal;
import uk.co.tuffdev.filestore.auth.exception.ResourceNotFoundException;
import uk.co.tuffdev.filestore.User;
import uk.co.tuffdev.filestore.UserRepository;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
