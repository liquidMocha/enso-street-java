package com.ensostreet.ensostreetserver.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginRequest {
    String email;
    String password;
}
