package ca.etsmtl.taf.controller;

import ca.etsmtl.taf.entity.TestCase;
import ca.etsmtl.taf.entity.TestPlan;
import ca.etsmtl.taf.entity.TestSuite;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/models")
public class ModelController {
    @GetMapping
    public List<Map<String, String>> getAvailableModels() {
        return List.of(
                Map.of("type", TestPlan.API_NAME, "label", "Plan de Test"),
                Map.of("type", TestSuite.API_NAME, "label", "Suite de Test"),
                Map.of("type", TestCase.API_NAME, "label", "Cas de Test")
        );
    }
}
