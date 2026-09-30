package com.limitcross.facility.web.rest;

import com.limitcross.facility.domain.User;
import com.limitcross.facility.repository.UserRepository;
import com.limitcross.facility.security.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/users/me")
public class UserProfileResource {

    private final UserRepository userRepository;

    public UserProfileResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ProfileResponse getProfile() {
        return toResponse(currentUser());
    }

    @PutMapping
    public ProfileResponse updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        User user = currentUser();
        String[] name = request.fullName().trim().split("\\s+", 2);
        user.setFirstName(name[0]);
        user.setLastName(name.length > 1 ? name[1] : "");
        user.setEmail(request.email().trim().toLowerCase());
        user.setPhone(request.phone() == null ? null : request.phone().trim());
        return toResponse(userRepository.save(user));
    }

    private User currentUser() {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
            new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required"));
        return userRepository.findOneByLogin(login).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private static ProfileResponse toResponse(User user) {
        String fullName = ((user.getFirstName() == null ? "" : user.getFirstName()) + " " +
            (user.getLastName() == null ? "" : user.getLastName())).trim();
        Instant createdAt = user.getCreatedDate();
        return new ProfileResponse(user.getLogin(), user.getPhone(), fullName, user.getEmail(), createdAt, user.getLastModifiedDate());
    }

    public record UpdateProfileRequest(
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Email @Size(min = 5, max = 254) String email,
        @Size(max = 30) String phone
    ) {}

    public record ProfileResponse(String uid, String phone, String fullName, String email, Instant createdAt, Instant lastLoginAt) {}
}