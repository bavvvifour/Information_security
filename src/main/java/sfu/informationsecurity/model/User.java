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

    private int clickMultiplier = 1; // Х2 тапа в нажатие
    private int autoGainBonus = 0;   // +5 в секунду
    private int autoMultiplier = 1;  // Х2 авто-тап

    public int getClickMultiplier() {
        return clickMultiplier;
    }

    public void setClickMultiplier(int clickMultiplier) {
        this.clickMultiplier = clickMultiplier;
    }

    public int getAutoMultiplier() {
        return autoMultiplier;
    }

    public void setAutoMultiplier(int autoMultiplier) {
        this.autoMultiplier = autoMultiplier;
    }

    public int getAutoGainBonus() {
        return autoGainBonus;
    }

    public void setAutoGainBonus(int autoGainBonus) {
        this.autoGainBonus = autoGainBonus;
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
        return true;
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
