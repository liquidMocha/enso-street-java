package com.ensostreet.ensostreetserver.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/createUser")
    @ResponseStatus(HttpStatus.CREATED)
    void registerUser(@RequestBody @Valid RegisterUserRequest request) {
        try {
            userService.registerUser(request.email, request.profileName, request.password);
        } catch (UserAlreadyExists exception) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format("User %s already exists", request.email)
            );
        }
    }
}
