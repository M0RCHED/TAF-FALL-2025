package ca.etsmtl.taf.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "users")
public class User {

  @Id
  private String id;

  @NotBlank
  @Size(max = 50)
  private String fullName;

  @Indexed(unique = true)
  @NotBlank
  @Size(max = 20)
  private String username;

  @Indexed(unique = true)
  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @DBRef
  private Set<Role> roles = new HashSet<>();

  public User() {}

  public User(String fullName, String username, String email, String password) {
    this.fullName = fullName;
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public String getFullName() {
    return fullName;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
}

