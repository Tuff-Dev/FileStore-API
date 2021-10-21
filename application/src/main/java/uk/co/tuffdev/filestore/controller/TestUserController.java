package uk.co.tuffdev.filestore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.tuffdev.filestore.User;
import uk.co.tuffdev.filestore.UserRepository;
import uk.co.tuffdev.filestore.auth.CurrentUser;
import uk.co.tuffdev.filestore.auth.UserPrincipal;
import uk.co.tuffdev.filestore.auth.exception.ResourceNotFoundException;

@RestController
public class TestUserController {

    private final UserRepository userRepository;

    public TestUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/me")
    public User userDetails(@CurrentUser UserPrincipal user) {
        return userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("User.", "id", user.getId()));
    }

}
