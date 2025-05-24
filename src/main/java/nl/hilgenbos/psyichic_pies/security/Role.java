package nl.hilgenbos.psyichic_pies.security;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN("ADMIN"),
    USER("USER");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + roleName;
    }

    @Override
    public String toString() {
        return getAuthority();
    }
}
