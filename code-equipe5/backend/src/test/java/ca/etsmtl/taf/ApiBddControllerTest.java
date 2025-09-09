package ca.etsmtl.taf;

import ca.etsmtl.taf.service.ApiBddService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiBddControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiBddControllerTest.class);
	
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ApiBddService apiBddService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testWorkflow() throws Exception {

        // Step 0: Delete all results
        mockMvc.perform(delete("/api/results/jmeter"))
                .andExpect(status().isNoContent());

        // Step 1: Add new test data
        String jsonRequest = "{ \"module\": \"jmeter\", \"results\": ["
        + "{ \"timeStamp\": \"1728676067633\", \"date\": \"2024-10-11 21:47:47\", \"elapsed\": \"73\", \"label\": \"HTTP Request\", \"responseCode\": \"200\", \"responseMessage\": \"OK\", \"threadName\": \"Thread Group 1-1\", \"dataType\": \"text\", \"success\": \"true\", \"failureMessage\": \"\", \"bytes\": \"481\", \"sentBytes\": \"115\", \"grpThreads\": \"1\", \"allThreads\": \"1\", \"URL\": \"http://httpbin.io/get\", \"Latency\": \"73\", \"IdleTime\": \"0\", \"Connect\": \"42\" },"
        + "{ \"timeStamp\": \"1728676068134\", \"date\": \"2024-10-11 21:47:48\", \"elapsed\": \"52\", \"label\": \"HTTP Request\", \"responseCode\": \"200\", \"responseMessage\": \"OK\", \"threadName\": \"Thread Group 1-2\", \"dataType\": \"text\", \"success\": \"true\", \"failureMessage\": \"\", \"bytes\": \"481\", \"sentBytes\": \"115\", \"grpThreads\": \"1\", \"allThreads\": \"1\", \"URL\": \"http://httpbin.io/get\", \"Latency\": \"52\", \"IdleTime\": \"0\", \"Connect\": \"23\" },"
        + "{ \"timeStamp\": \"1728676068633\", \"date\": \"2024-10-11 21:47:48\", \"elapsed\": \"55\", \"label\": \"HTTP Request\", \"responseCode\": \"200\", \"responseMessage\": \"OK\", \"threadName\": \"Thread Group 1-3\", \"dataType\": \"text\", \"success\": \"true\", \"failureMessage\": \"\", \"bytes\": \"481\", \"sentBytes\": \"115\", \"grpThreads\": \"1\", \"allThreads\": \"1\", \"URL\": \"http://httpbin.io/get\", \"Latency\": \"55\", \"IdleTime\": \"0\", \"Connect\": \"26\" },"
        + "{ \"timeStamp\": \"1728676069130\", \"date\": \"2024-10-11 21:47:49\", \"elapsed\": \"56\", \"label\": \"HTTP Request\", \"responseCode\": \"200\", \"responseMessage\": \"OK\", \"threadName\": \"Thread Group 1-4\", \"dataType\": \"text\", \"success\": \"true\", \"failureMessage\": \"\", \"bytes\": \"481\", \"sentBytes\": \"115\", \"grpThreads\": \"1\", \"allThreads\": \"1\", \"URL\": \"http://httpbin.io/get\", \"Latency\": \"56\", \"IdleTime\": \"0\", \"Connect\": \"27\" },"
        + "{ \"timeStamp\": \"1728676069641\", \"date\": \"2024-10-11 21:47:49\", \"elapsed\": \"50\", \"label\": \"HTTP Request\", \"responseCode\": \"200\", \"responseMessage\": \"OK\", \"threadName\": \"Thread Group 1-5\", \"dataType\": \"text\", \"success\": \"true\", \"failureMessage\": \"\", \"bytes\": \"481\", \"sentBytes\": \"115\", \"grpThreads\": \"1\", \"allThreads\": \"1\", \"URL\": \"http://httpbin.io/get\", \"Latency\": \"50\", \"IdleTime\": \"0\", \"Connect\": \"19\" },"
        + "{ \"timeStamp\": \"1728676070131\", \"date\": \"2024-10-11 21:47:50\", \"elapsed\": \"49\", \"label\": \"HTTP Request\", \"responseCode\": \"200\", \"responseMessage\": \"OK\", \"threadName\": \"Thread Group 1-6\", \"dataType\": \"text\", \"success\": \"true\", \"failureMessage\": \"\", \"bytes\": \"481\", \"sentBytes\": \"115\", \"grpThreads\": \"1\", \"allThreads\": \"1\", \"URL\": \"http://httpbin.io/get\", \"Latency\": \"49\", \"IdleTime\": \"0\", \"Connect\": \"20\" },"
        + "{ \"timeStamp\": \"1728676070630\", \"date\": \"2024-10-11 21:47:50\", \"elapsed\": \"52\", \"label\": \"HTTP Request\", \"responseCode\": \"200\", \"responseMessage\": \"OK\", \"threadName\": \"Thread Group 1-7\", \"dataType\": \"text\", \"success\": \"true\", \"failureMessage\": \"\", \"bytes\": \"481\", \"sentBytes\": \"115\", \"grpThreads\": \"1\", \"allThreads\": \"1\", \"URL\": \"http://httpbin.io/get\", \"Latency\": \"52\", \"IdleTime\": \"0\", \"Connect\": \"24\" },"
        + "{ \"timeStamp\": \"1728676071130\", \"date\": \"2024-10-11 21:47:51\", \"elapsed\": \"54\", \"label\": \"HTTP Request\", \"responseCode\": \"200\", \"responseMessage\": \"OK\", \"threadName\": \"Thread Group 1-8\", \"dataType\": \"text\", \"success\": \"true\", \"failureMessage\": \"\", \"bytes\": \"481\", \"sentBytes\": \"115\", \"grpThreads\": \"1\", \"allThreads\": \"1\", \"URL\": \"http://httpbin.io/get\", \"Latency\": \"54\", \"IdleTime\": \"0\", \"Connect\": \"25\" },"
        + "{ \"timeStamp\": \"1728676071632\", \"date\": \"2024-10-11 21:47:51\", \"elapsed\": \"54\", \"label\": \"HTTP Request\", \"responseCode\": \"200\", \"responseMessage\": \"OK\", \"threadName\": \"Thread Group 1-9\", \"dataType\": \"text\", \"success\": \"true\", \"failureMessage\": \"\", \"bytes\": \"481\", \"sentBytes\": \"115\", \"grpThreads\": \"1\", \"allThreads\": \"1\", \"URL\": \"http://httpbin.io/get\", \"Latency\": \"54\", \"IdleTime\": \"0\", \"Connect\": \"25\" },"
        + "{ \"timeStamp\": \"1728676072133\", \"date\": \"2024-10-11 21:47:52\", \"elapsed\": \"55\", \"label\": \"HTTP Request\", \"responseCode\": \"200\", \"responseMessage\": \"OK\", \"threadName\": \"Thread Group 1-10\", \"dataType\": \"text\", \"success\": \"true\", \"failureMessage\": \"\", \"bytes\": \"481\", \"sentBytes\": \"115\", \"grpThreads\": \"1\", \"allThreads\": \"1\", \"URL\": \"http://httpbin.io/get\", \"Latency\": \"55\", \"IdleTime\": \"0\", \"Connect\": \"26\" }"
        + "] }";
		
		logger.info("Sending POST request with JSON payload: {}", jsonRequest);

        mockMvc.perform(post("/api/results/jmeter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        logger.info("POST request completed, checking if data was added...");

        // Step 2: Retrieve all results and check if data was added
        mockMvc.perform(get("/api/results/jmeter"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(10))
                .andDo(result -> logger.info("GET request for all results: {}", result.getResponse().getContentAsString()));
    }
}

