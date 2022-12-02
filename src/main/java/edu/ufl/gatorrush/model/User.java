package edu.ufl.gatorrush.model;

import net.minidev.json.annotate.JsonIgnore;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents a User
 */
@Entity
@Table(name = "UserEntity")
public class User implements UserDetails {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private static final Pattern USERNAME = Pattern.compile("^[a-z0-9]{4,15}$");
    private static final Pattern EMAIL = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");

    /**
     * Unique Identifier
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * Login Username
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Email
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Password
     */
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    /**
     * Status of account
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * List of completed Levels
     */
    @ManyToOne
    private Level level;

    /**
     * List of attempted problems
     */
    @OneToMany(mappedBy = "user")
    private final List<Attempt> attempts = new ArrayList<>();

    /**
     * Highest challenge mode score
     */
    private Integer score = 0;

    protected User() {}

    /**
     * Constructor
     * @param username Username
     * @param email Email
     * @param password Password
     */
    public User(String username, String email, String password) throws Exception {
        setUsername(username);
        setEmail(email);
        setPassword(password);
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setUsername(String username) throws Exception {
        if (USERNAME.matcher(username.toLowerCase()).matches()) {
            this.username = username;
        } else {
            throw new Exception("Invalid username format.");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        email = email.toLowerCase();
        if (EMAIL.matcher(email).matches()) {
            this.email = email;
        } else {
            throw new Exception("Invalid email format.");
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public List<Attempt> getAttempts() {
        return attempts;
    }

    public Level getLevel() {
        return level;
    }

    public User setLevel(Level level) {
        this.level = level;
        return this;
    }

    public Integer getScore() {
        return score;
    }

    public User setScore(Integer score) {
        this.score = score;
        return this;
    }

    /**
     * Sets the score to the provided score if higher than the current score
     * @param score New score
     * @return The potentially modified object
     */
    public User setHighScore(Integer score) {
        this.score = Math.max(this.score, score);
        return this;
    }
}
