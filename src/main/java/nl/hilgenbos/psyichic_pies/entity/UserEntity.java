package nl.hilgenbos.psyichic_pies.entity;

import jakarta.persistence.*;
import nl.hilgenbos.psyichic_pies.security.Role;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue
    private long id;

    String username;
    String password;
    String passwordSeed;

    @ElementCollection (fetch = FetchType.EAGER)
    private List<Role> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSeed() {
        return passwordSeed;
    }

    public void setPasswordSeed(String passwordSeed) {
        this.passwordSeed = passwordSeed;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity user = (UserEntity) o;
        return getId() == user.getId() && 
               Objects.equals(getUsername(), user.getUsername()) && 
               Objects.equals(getPassword(), user.getPassword()) && 
               Objects.equals(getPasswordSeed(), user.getPasswordSeed()) && 
               Objects.equals(getRoles(), user.getRoles());
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(getId());
        result = 31 * result + Objects.hashCode(getUsername());
        result = 31 * result + Objects.hashCode(getPassword());
        result = 31 * result + Objects.hashCode(getPasswordSeed());
        result = 31 * result + Objects.hashCode(getRoles());
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", passwordSeed='" + passwordSeed + '\'' +
                ", roles=" + roles +
                '}';
    }
}
