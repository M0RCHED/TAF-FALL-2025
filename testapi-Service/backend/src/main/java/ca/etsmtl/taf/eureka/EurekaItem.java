package ca.etsmtl.taf.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EurekaItem {

   @Autowired
   private DiscoveryClient discoveryClient;

	public void test() throws Exception {
		List<String> list = discoveryClient.getServices();

		list.forEach(item -> {
			System.out.println("Service: " + item);
			List<ServiceInstance> instances = this.discoveryClient.getInstances(item);
			instances.forEach(instance -> {
				System.out.println("URI: " + instance.getUri());
				System.out.println("HOST: " + instance.getHost());
				System.out.println("PORT: " + instance.getPort());
				System.out.println("INSTANCE ID: " + instance.getInstanceId());
				System.out.println("SERVICE ID: " + instance.getServiceId());
			});
		});
	}
}
