package fr.ensicaen.pi.gpss.backend.security.user_details;

import fr.ensicaen.pi.gpss.backend.database.entity.user.UserInformationEntity;
import fr.ensicaen.pi.gpss.backend.database.enumerate.Status;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserDetailsImplementation implements UserDetails {
    @Getter
    private final String email;
    private final String password;
    private final Status status;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImplementation(
            String email, String password, Status status,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.email = email;
        this.password = password;
        this.status = status;
        this.authorities = authorities;
    }

    public static UserDetailsImplementation build(UserInformationEntity user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(
                user.getRole().getRoleType().label()
                )
        );

        return new UserDetailsImplementation(
                user.getEmail(), user.getPassword(), user.getRequestStatus(), authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return status != Status.PENDING;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != Status.UNAUTHORIZED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status != Status.UNAUTHORIZED;
    }

    @Override
    public boolean isEnabled() {
        return status == Status.AUTHORIZED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImplementation that = (UserDetailsImplementation) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
