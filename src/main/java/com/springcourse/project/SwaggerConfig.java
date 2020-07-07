package com.springcourse.project;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.hateoas.client.LinkDiscoverer;
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	Contact contact = new Contact(
			"Vaibhav Mani",
			"",
			"vaibvinesh@gmail.com");
	
	ApiInfo info = new ApiInfo("User Management Web Service Documentaion"
			, "Documents the various endpoints in this project", 
			"1.0", "", contact, "Apache 2.0", "http://www.apache.org/licences/LICENSE-2.0",
			new ArrayList<>());
	@Bean
	public Docket apiDocket() {
		Docket docket =  new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(info)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.springcourse.project"))
				.paths(PathSelectors.any())
				.build();
		
		return docket;
	}

	@Bean
	public LinkDiscoverers discovers() {
		
		List<LinkDiscoverer> plugins = new ArrayList<>();
		plugins.add(new CollectionJsonLinkDiscoverer());
		return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
	}
}
