package ru.havlong.dnnback;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.havlong.dnnback.repositories.NetworkRepository;

@SpringBootApplication
public class DnnBackApplication {

	private HttpServiceProxyFactory serviceProxyFactory = null;
	@Value("http://${app.dnn_hostname}:${app.dnn_port}/")
	private String urlString;

	public static void main(String[] args) {
		SpringApplication.run(DnnBackApplication.class, args);
	}

	void initProxyFactory() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(urlString));
		RestTemplateAdapter adapter = RestTemplateAdapter.create(restTemplate);
		serviceProxyFactory = HttpServiceProxyFactory.builderFor(adapter).build();
	}

	@Bean
	NetworkRepository networkTemplate() {
		if (Objects.isNull(serviceProxyFactory))
			initProxyFactory();
		return serviceProxyFactory.createClient(NetworkRepository.class);
	}
}
