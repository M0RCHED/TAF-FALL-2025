package ca.etsmtl.taf.service;

import ca.etsmtl.taf.dto.TestCaseDTO;
import ca.etsmtl.taf.dto.TestPlanDTO;
import ca.etsmtl.taf.dto.TestSuiteDTO;
import ca.etsmtl.taf.entity.TestCase;
import ca.etsmtl.taf.entity.TestPlan;
import ca.etsmtl.taf.entity.TestSuite;
import ca.etsmtl.taf.repository.testapi.TestCaseRepository;
import ca.etsmtl.taf.repository.testapi.TestPlanRepository;
import ca.etsmtl.taf.repository.testapi.TestSuiteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TestPlanService {
    private final TestPlanRepository testPlanRepository;
    private final TestSuiteRepository testSuiteRepository;
    private final TestCaseRepository testCaseRepository;
    private final ModelMapper modelMapper;

    public TestPlanService(TestPlanRepository testPlanRepository,
                           TestSuiteRepository testSuiteRepository,
                           TestCaseRepository testCaseRepository,
                           ModelMapper modelMapper) {
        this.testPlanRepository = testPlanRepository;
        this.testSuiteRepository = testSuiteRepository;
        this.testCaseRepository = testCaseRepository;
        this.modelMapper = modelMapper;
    }

    public Optional<?> getElementById(String type, String id) {
        switch (type) {
            case TestPlan.API_NAME:
                return testPlanRepository.findById(id);
            case TestSuite.API_NAME:
                return testSuiteRepository.findById(id);
            case TestCase.API_NAME:
                return testCaseRepository.findById(id);
            default:
                return Optional.empty();
        }
    }

    public Object createElement(String type, Object data) {
        switch (type) {
            case TestPlan.API_NAME:
                TestPlanDTO planDTO = modelMapper.map(data, TestPlanDTO.class);
                return createTestPlan(planDTO);

            case TestSuite.API_NAME:
                TestSuiteDTO suiteDTO = modelMapper.map(data, TestSuiteDTO.class);
                return createTestSuite(suiteDTO);

            case TestCase.API_NAME:
                TestCaseDTO caseDTO = modelMapper.map(data, TestCaseDTO.class);
                return createTestCase(caseDTO);

            default:
                throw new RuntimeException("Unknown type : " + type);
        }
    }

    public Object updateElement(String type, String id, Object updatedData) {
        switch (type) {
            case TestPlan.API_NAME:
                return updateEntity(testPlanRepository, id, updatedData, TestPlan.class);
            case TestSuite.API_NAME:
                return updateEntity(testSuiteRepository, id, updatedData, TestSuite.class);
            case TestCase.API_NAME:
                return updateEntity(testCaseRepository, id, updatedData, TestCase.class);
            default:
                throw new RuntimeException("Unknown type : " + type);
        }
    }

    private <T> T updateEntity(CrudRepository<T, String> repository, String id, Object updatedData, Class<T> entityType) {
        return repository.findById(id).map(existingEntity -> {
            modelMapper.map(updatedData, existingEntity);
            return repository.save(existingEntity);
        }).orElseThrow(() -> new RuntimeException("Element not found with id : " + id));
    }

    public List<TestPlan> getTestPlansWithDetails() {
        List<TestPlan> testPlans = testPlanRepository.findAll();
        for (TestPlan plan : testPlans) {
            List<TestSuite> suites = testSuiteRepository.findAllById(plan.testSuiteIds);
            for (TestSuite suite : suites) {
                suite.testCases = testCaseRepository.findAllById(suite.testCaseIds);
            }
            plan.testSuites = suites;
        }

        return testPlans;
    }

    public TestPlan createTestPlan(TestPlanDTO dto) {
        TestPlan testPlan = modelMapper.map(dto, TestPlan.class);
        testPlan.setId(null); // Force MongoDB to generate ID
        return testPlanRepository.save(testPlan);
    }

    public TestSuite createTestSuite(TestSuiteDTO dto) {
        TestSuite testSuite = modelMapper.map(dto, TestSuite.class);
        testSuite.setId(null); // Force MongoDB to generate ID
        testSuite = testSuiteRepository.save(testSuite);

        Optional<TestPlan> optionalPlan = testPlanRepository.findById(dto.testPlanId);
        if (optionalPlan.isPresent()) {
            TestPlan testPlan = optionalPlan.get();
            testPlan.testSuiteIds.add(testSuite.getId());
            testPlanRepository.save(testPlan);
        } else {
            throw new RuntimeException("TestPlan not found with id: " + dto.testPlanId);
        }

        return testSuite;
    }

    public TestCase createTestCase(TestCaseDTO dto) {
        TestCase testCase = modelMapper.map(dto, TestCase.class);
        testCase.setId(null); // Force MongoDB to generate ID
        testCase = testCaseRepository.save(testCase);

        Optional<TestSuite> optionalSuite = testSuiteRepository.findById(dto.testSuiteId);
        if (optionalSuite.isPresent()) {
            TestSuite testSuite = optionalSuite.get();
            testSuite.testCaseIds.add(testCase.getId());
            testSuiteRepository.save(testSuite);
        } else {
            throw new RuntimeException("TestSuite not found with id: " + dto.testSuiteId);
        }

        return testCase;
    }

    public void deleteElement(String type, String id) {
        switch (type) {
            case TestPlan.API_NAME:
                deleteTestPlan(id);
                break;
            case TestSuite.API_NAME:
                deleteTestSuite(id);
                break;
            case TestCase.API_NAME:
                deleteTestCase(id);
                break;
            default:
                throw new RuntimeException("Unknown type : " + type);
        }
    }

    private void deleteTestPlan(String planId) {
        Optional<TestPlan> optionalPlan = testPlanRepository.findById(planId);
        if (optionalPlan.isPresent()) {
            TestPlan plan = optionalPlan.get();
            if (plan.testSuiteIds != null) {
                for (String suiteId : plan.testSuiteIds) {
                    deleteTestSuite(suiteId);
                }
            }
            testPlanRepository.deleteById(planId);
        }
    }

    private void deleteTestSuite(String suiteId) {
        Optional<TestSuite> optionalSuite = testSuiteRepository.findById(suiteId);
        if (optionalSuite.isPresent()) {
            TestSuite suite = optionalSuite.get();
            if (suite.testCaseIds != null) {
                for (String caseId : suite.testCaseIds) {
                    deleteTestCase(caseId);
                }
            }

            Optional<TestPlan> parentPlan = testPlanRepository.findById(suite.testPlanId);
            if (parentPlan.isPresent()) {
                parentPlan.get().testSuiteIds.remove(suiteId);
                testPlanRepository.save(parentPlan.get());
            }
            testSuiteRepository.deleteById(suiteId);
        }
    }

    private void deleteTestCase(String caseId) {
        Optional<TestCase> optionalCase = testCaseRepository.findById(caseId);
        if (optionalCase.isPresent()) {
            TestCase testCase = optionalCase.get();
            Optional<TestSuite> parentSuite = testSuiteRepository.findById(testCase.testSuiteId);
            if (parentSuite.isPresent()) {
                parentSuite.get().testCaseIds.remove(caseId);
                testSuiteRepository.save(parentSuite.get());
            }
            testCaseRepository.deleteById(caseId);
        }
    }

}
