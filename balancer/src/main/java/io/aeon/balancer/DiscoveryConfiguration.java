package io.aeon.balancer;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

record DiscoveryConfiguration() {
	
	@Bean
	@Primary
	ServiceInstanceListSupplier serviceInstanceListSupplier() {
		return new OcrParserInstanceListSuppler();
	}
}