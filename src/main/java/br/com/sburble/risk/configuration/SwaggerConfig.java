package br.com.sburble.risk.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ClientCredentialsGrant;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {
	
	
	public static final String RISK = "Order risk analysis";

	@Value("${springfox.documentation.swagger.v2.home}")
	private String home;

	@Autowired
	private Docket dock;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		dock.apiInfo(apiInfo());
		registry.addResourceHandler(home + "/**").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).host("localhost:8080").consumes(Set.of(MediaType.APPLICATION_JSON_VALUE))
				.produces(Set.of(MediaType.APPLICATION_JSON_VALUE)).protocols(Set.of("https")).select()
				.apis(RequestHandlerSelectors.basePackage("br.com.sburble.risk.controller")).paths(PathSelectors.any())
				.build().pathMapping("/").securitySchemes(Collections.singletonList(oauth()))
				.useDefaultResponseMessages(false).apiInfo(apiInfo());

	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Risk API Rest").description("Risk API documentation").version("1.0")
				.contact(new Contact("Developer", null, "contact@develop.com")).build();
	}

	@Bean
	List<GrantType> grantTypes() {
		final String tokenUrl = String.format("https://localhost:8080/oauth2/token");
		List<GrantType> grantTypes = new ArrayList<>();
		grantTypes.add(new ClientCredentialsGrant(tokenUrl));
		return grantTypes;
	}

	@Bean
	SecurityScheme oauth() {
		return new OAuthBuilder().name("OAuth2").scopes(scopes()).grantTypes(grantTypes()).build();
	}

	private List<AuthorizationScope> scopes() {
		List<AuthorizationScope> list = new ArrayList<>();
		list.add(new AuthorizationScope("read", "Grants read access"));
		list.add(new AuthorizationScope("write", "Grants write access"));
		return list;
	}

}
