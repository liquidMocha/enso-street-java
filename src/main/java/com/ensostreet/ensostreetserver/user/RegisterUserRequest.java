package com.ensostreet.ensostreetserver.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Builder
public class RegisterUserRequest {
    String email;
    String profileName;
    @Size(min = 8)
    String password;
}
