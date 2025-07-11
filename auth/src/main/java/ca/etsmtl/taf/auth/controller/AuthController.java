package ca.etsmtl.taf.auth.controller;

import ca.etsmtl.taf.auth.payload.response.JwtResponse;
import ca.etsmtl.taf.auth.services.JwtService;
import ca.etsmtl.taf.auth.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.etsmtl.taf.auth.payload.request.SignupRequest;
import ca.etsmtl.taf.auth.payload.response.MessageResponse;
import ca.etsmtl.taf.auth.payload.request.LoginRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @GetMapping("/home")
  public String greeting() {
    return "Hello from Auth Microservice!";
  }

  @Autowired
  private JwtService jwtService;

  @Autowired
  private UserService userService;

  @PostMapping("/signin")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {
    final JwtResponse response = this.jwtService.createJwtToken(authenticationRequest);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
    if (this.userService.existsByUsername(request.getUsername())) {
      return ResponseEntity.badRequest().body("Username is already taken.");
    }

    if (this.userService.existsByEmail(request.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    this.userService.save(request);

    return ResponseEntity.ok(new MessageResponse("Inscription Réussie.!"));
  }
}
