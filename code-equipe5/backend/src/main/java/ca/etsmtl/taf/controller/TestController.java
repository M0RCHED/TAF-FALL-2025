package ca.etsmtl.taf.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.etsmtl.taf.entity.Test;
import ca.etsmtl.taf.repository.TestRepository;

@RestController
public class TestController {

    private final TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }
    
    @GetMapping("/tests")
    public Iterable<Test> findAllTests() {
        return this.testRepository.findAll();
    }
}
