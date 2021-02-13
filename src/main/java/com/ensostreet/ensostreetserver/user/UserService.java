package com.ensostreet.ensostreetserver.user;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    void registerUser(
            String email,
            String name,
            String password
    ) {
        boolean userExists = userRepository.existsByEmail(email);

        if (!userExists) {
            userRepository.save(new User(email, name, BCrypt.hashpw(password, BCrypt.gensalt())));
        } else {
            throw new UserAlreadyExists();
        }
    }
}
