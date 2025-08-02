package ca.etsmtl.taf.user.payload.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateRequest {
  @NotBlank
  @Size(min = 3, max = 250)
  private String fullName;

  @NotBlank
  @Email
  private String email;
}
