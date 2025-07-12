package ca.etsmtl.taf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import ca.etsmtl.taf.eureka.EurekaItem;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.List;
import java.util.Map;

@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaAuditing
public class TestAutomationFrameworkApplication implements CommandLineRunner {

    @Autowired
	private EurekaItem eurekaItem;

	public void run(String... args) throws Exception {
		System.out.println("LIST INSTANCES");

		//this.eurekaItem.test();
	}

	public static void main(String[] args) {
		SpringApplication.run(TestAutomationFrameworkApplication.class, args);
	}

}
