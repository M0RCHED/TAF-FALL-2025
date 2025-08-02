package ca.etsmtl.taf.auth.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//@Data
public class JwtResponse {
  private String token;
  private String refresh;
  private String type = "Bearer";
  private String id;
  private String fullName;
  private String username;
  private String email;
  private List<String> roles;

  public JwtResponse(String accessToken, String refresh, String id, String fullName, String username, String email, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.fullName = fullName;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.refresh = refresh;
  }
}
