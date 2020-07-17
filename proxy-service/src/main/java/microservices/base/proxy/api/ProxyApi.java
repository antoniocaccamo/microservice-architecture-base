package microservices.base.proxy.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Configuration
public class ProxyApi {

    @Autowired
    private ZuulProperties zuulProperties;


	@Primary
	@Bean
	public SwaggerResourcesProvider swaggerResourcesProvider() {

		return () -> {
			return
			zuulProperties.getRoutes().values().stream()
					.map(this::route2resource)
					.collect(Collectors.toList())
					;

		};
	}

	private SwaggerResource route2resource(ZuulProperties.ZuulRoute zuulRoute) {

		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(zuulRoute.getServiceId());
		swaggerResource.setLocation(String.format("/%s/v2/api-docs", zuulRoute.getId()));
		swaggerResource.setSwaggerVersion("1.0.0");
		return swaggerResource;
	}


}