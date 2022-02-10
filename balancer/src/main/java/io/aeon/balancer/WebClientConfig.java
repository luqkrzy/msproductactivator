package io.aeon.balancer;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@LoadBalancerClient(name = "ocr-parser", configuration = DiscoveryConfiguration.class)
class WebClientConfig {
	
	@LoadBalanced
	@Bean
	WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}
}
