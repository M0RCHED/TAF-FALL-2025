package ca.etsmtl.taf.controller;

import ca.etsmtl.taf.dto.TestCaseDTO;
import ca.etsmtl.taf.dto.TestPlanDTO;
import ca.etsmtl.taf.dto.TestSuiteDTO;
import ca.etsmtl.taf.entity.TestCase;
import ca.etsmtl.taf.entity.TestPlan;
import ca.etsmtl.taf.entity.TestSuite;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import com.fasterxml.jackson.databind.SerializationFeature;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/templates")
public class TemplateController {

    private final ObjectMapper objectMapper;

    public TemplateController() {
        this.objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @GetMapping("/{className}")
    public Object getTemplate(@PathVariable String className) {
        return switch (className) {
            case TestPlan.API_NAME -> new TestPlanDTO();
            case TestSuite.API_NAME -> new TestSuiteDTO();
            case TestCase.API_NAME -> new TestCaseDTO();
            default -> Map.of("error", "Class not found: " + className);
        };
    }
}
