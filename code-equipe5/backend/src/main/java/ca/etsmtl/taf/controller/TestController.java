package ca.etsmtl.taf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.etsmtl.taf.entity.Test;
import ca.etsmtl.taf.repository.TestRepository;

@RestController
@RequestMapping("/api/test") // préfixe clair
public class TestController {

    private final TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping("/tests")
    public Iterable<Test> findAllTests() {
        return testRepository.findAll();
    }

    // ping simple pour valider le mapping   :::::::p
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/tests")
    public Test create(@RequestBody Test payload) {
        return testRepository.save(payload);
    }
}
