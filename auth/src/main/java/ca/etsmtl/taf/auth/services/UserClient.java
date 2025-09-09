package ca.etsmtl.taf.auth.services;

import ca.etsmtl.taf.auth.entity.User;
import ca.etsmtl.taf.auth.payload.request.SignupRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import ca.etsmtl.taf.auth.payload.request.LoginRequest;

@FeignClient(name = "user")
public interface UserClient {
    @PostMapping("/create")
    //@Headers("Content-Type: application/json")
    User create(@RequestBody SignupRequest request);

    @PostMapping("get")
    //@Headers("Content-Type: application/json")
    User get(@RequestBody LoginRequest request);
}
