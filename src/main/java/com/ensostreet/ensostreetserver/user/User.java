package com.ensostreet.ensostreetserver.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", schema = "public")
@Getter
public class User {
    public User(String email, String profileName, String password) {
        this.password = password;
        this.email = email;
        this.profileName = profileName;
        this.createdOn = Instant.now();
    }

    @Id
    @GeneratedValue
    UUID id;

    String password;
    String email;
    Instant createdOn;
    int failedLoginAttempts;
    String profileName;
    String firstName;
    String lastName;
    String phone;
    String stripeConnectAccountId;

}
