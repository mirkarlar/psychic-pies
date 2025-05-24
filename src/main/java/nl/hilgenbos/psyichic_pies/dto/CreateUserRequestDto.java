package nl.hilgenbos.psyichic_pies.dto;

import nl.hilgenbos.psyichic_pies.security.Role;

import java.util.List;

public record CreateUserRequestDto(String username, String password, List<Role> roles) {
}