package org.jpa.ticketmanagerbackend.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jpa.ticketmanagerbackend.model.common.BaseEntity;
import org.jpa.ticketmanagerbackend.utilities.EncryptionUtility;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.*;

/**
 * Entité représentant un utilisateur.
 * Chaque utilisateur a un nom d'utilisateur unique, un mot de passe et une adresse e-mail.
 */
@Entity
@Table(name = "user_account", indexes = {
        @Index(name = "i_user_account_email", columnList = "email", unique = true),
        @Index(name = "i_user_account_username", columnList = "username", unique = true),
        //@Index(name = "i_user_account_employee_id", columnList = "employee_id")
})
@NamedQueries({ @NamedQuery(name = "allUserAccountQuery", query = "SELECT u FROM UserAccount u"),
        @NamedQuery(name = "allUserAccountCountQuery", query = "SELECT COUNT(*) FROM UserAccount"),
        @NamedQuery(name = "userAccountByUsernameQuery", query = "SELECT u FROM UserAccount u WHERE (u.username = :username)"),
        @NamedQuery(name = "userAccountByEmailQuery", query = "SELECT u FROM UserAccount u WHERE (u.email = :email)"),
        @NamedQuery(name = "userAccountByEmployeeQuery", query = "SELECT u FROM UserAccount u WHERE (u.employee = :employee)"),
        //@NamedQuery(name = "userAccountByEmployeeQuery", query = "SELECT u FROM UserAccount u WHERE (u.roles IN = :employee)")
})
public class UserAccount
        extends BaseEntity {

    /**
     * Nom d'utilisateur de l'utilisateur.
     */
    private String username;

    /**
     * Mot de passe de l'utilisateur.
     */
    private String password;

    /**
     * Adresse e-mail de l'utilisateur.
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     *
     */
    private String apiKey;

    /**
     *
     */
    private String lang;

    /**
     *
     */
    private Collection<GrantedAuthority> authorities;

    private Employee employee;

    private Set<Role> roles = new HashSet<>();

    /**
     * Constructeur par défaut de la classe User.
     */
    public UserAccount() {
    }

    public UserAccount(String username, String password, String email, Employee employee, Set<Role> roles){
        this.username = username;
        this.password = password;
        this.email = email;
        this.employee = employee;
        this.roles = roles;
    }

    /**
     * Obtient le nom d'utilisateur de l'utilisateur.
     *
     * @return Le nom d'utilisateur de l'utilisateur.
     */
    @Column(name = "username", nullable = false)
    public String getUsername() {
        return username;
    }

    /**
     * Définit le nom d'utilisateur de l'utilisateur.
     *
     * @param username Le nouveau nom d'utilisateur de l'utilisateur.
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Obtient le mot de passe de l'utilisateur.
     *
     * @return Le mot de passe de l'utilisateur.
     */
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    /**
     * Définit le mot de passe de l'utilisateur.
     *
     * @param password Le nouveau mot de passe de l'utilisateur.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtient l'adresse e-mail de l'utilisateur.
     *
     * @return L'adresse e-mail de l'utilisateur.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse e-mail de l'utilisateur.
     *
     * @param email La nouvelle adresse e-mail de l'utilisateur.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @OneToOne(targetEntity = Employee.class, fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "user_account_employee",
            joinColumns = { @JoinColumn(name = "user_account_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "employee_id", referencedColumnName = "id") })
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "user_account_role",
            joinColumns = {@JoinColumn(name = "user_account_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Column(name = "apiKey", nullable = false, updatable = false)
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Column(name = "lang", nullable = false)
    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Transient
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = new HashSet<>();
            for (Role role : roles) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
        }
        return authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }


    @Override
    protected void onPrePersist() {
        super.onPrePersist();
        this.lang = "fr";
        this.setActive(true);
        this.password = new BCryptPasswordEncoder().encode(this.password);
        this.apiKey = Objects.requireNonNull(EncryptionUtility.sha256From(UUID.randomUUID().toString())).get();
    }

    @Override
    protected void onPreUpdate() {
        super.onPreUpdate();
        this.setActive(true);
    }
}
