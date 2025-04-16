package sfu.informationsecurity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private int b1Level = 1;

    @Column(nullable = false)
    private int b2Level = 1;

    @Column(nullable = false)
    private int b3Level = 1;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(unique = true, nullable = false)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public int getB1Level() {
        return b1Level;
    }

    public void setB1Level(int b1Level) {
        this.b1Level = b1Level;
    }

    public int getB3Level() {
        return b3Level;
    }

    public void setB3Level(int b3Level) {
        this.b3Level = b3Level;
    }

    public int getB2Level() {
        return b2Level;
    }

    public void setB2Level(int b2Level) {
        this.b2Level = b2Level;
    }

    private int clickGain = 1;

    private int autoGain = 1;

    public int getClickGain() {
        return clickGain;
    }

    public void setClickGain(int clickGain) {
        this.clickGain = clickGain;
    }

    public int getAutoGain() {
        return autoGain;
    }

    public void setAutoGain(int autoGain) {
        this.autoGain = autoGain;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
