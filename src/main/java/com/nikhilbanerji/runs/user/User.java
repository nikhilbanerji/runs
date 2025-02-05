package com.nikhilbanerji.runs.user;

import jakarta.validation.constraints.Email;

public record User(
        Integer id,
        String name,
        String username,
        @Email
        String email,
        Address address,
        String phone,
        String website,
        Company company
) {
}
