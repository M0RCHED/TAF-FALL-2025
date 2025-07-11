package ca.etsmtl.taf.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SwaggerController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/greeting")
    public String greeting() {
        return "Hello from Example Microservice!";
    }


    @GetMapping("/custom-swagger-config")
    public Map<String, Object> getSwaggerConfig() {
        List<Map<String, String>> urls = discoveryClient.getServices().stream()
                .filter(service -> !service.equalsIgnoreCase("gateway"))
                .map(service -> Map.of("name", service, "url", "/" + service + "/api-docs"))
                .toList();

        return Map.of("urls", urls);
    }
}