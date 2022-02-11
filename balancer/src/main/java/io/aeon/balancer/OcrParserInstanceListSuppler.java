package io.aeon.balancer;

import io.aeon.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

class OcrParserInstanceListSuppler implements ServiceInstanceListSupplier {
	
	@Value("${client.name}")
	private String serviceId;
	
	private static final HttpStatus SERVICE_UNAVAILABLE = HttpStatus.SERVICE_UNAVAILABLE;
	
	@Autowired
	WebClient webClient;
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	ResponseEntity<Void> checkEurekaAvailability() {
		return webClient.get()
						.retrieve()
						.toBodilessEntity()
						.onErrorReturn(new ResponseEntity<>(SERVICE_UNAVAILABLE))
						.block();
	}
	
	@Override
	public String getServiceId() {
		return serviceId;
	}
	
	@Override
	public Flux<List<ServiceInstance>> get() {
		ResponseEntity<Void> response = checkEurekaAvailability();
		if (response.getStatusCode().equals(SERVICE_UNAVAILABLE)) {
			throw new ApiException("Unable to connect to service discovery", SERVICE_UNAVAILABLE);
		}
		List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
		if (instances.size() == 0) {
			throw new ApiException(String.format("Unable to fetch %s instances", serviceId), SERVICE_UNAVAILABLE);
		}
		return Flux.just(instances);
	}
}
