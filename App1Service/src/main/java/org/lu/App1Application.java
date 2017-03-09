package org.lu;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

//@EnableDiscoveryClient
@SpringBootApplication
public class App1Application
{
	public static void main(String[] args)
	{
		SpringApplication.run(App1Application.class, args);
	}

//	@Bean
//	public FilterRegistrationBean corsFilterRegistration()
//	{
//		FilterRegistrationBean registration = new FilterRegistrationBean();
//		registration.setFilter((corsFilter()));
//		registration.addUrlPatterns("/*");
//		registration.setName("filter1");
//		registration.setOrder(1);
//		return registration;
//	}
//
//	@Bean
//	public FilterRegistrationBean tokenVerifyFilterRegistration()
//	{
//
//		FilterRegistrationBean registration = new FilterRegistrationBean();
//		registration.setFilter((tokenVerifyFilter()));
//		registration.addUrlPatterns("/*");
//		registration.setName("filter2");
//		registration.setOrder(2);
//		return registration;
//	}

//	@Bean(name = "tokenVerifyFilter")
//	public Filter tokenVerifyFilter()
//	{
//		return new TekenVerifyFilter();
//	}
//
//	@Bean(name = "corsFilter")
//	public Filter corsFilter()
//	{
//		return new CorsFilter();
//	}

	@LoadBalanced
	@Bean
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
	}

}

@RefreshScope
@RestController
class MyController
{
	@Value("${message:Hello default}")
	private String message;

	@LoadBalanced
	@Autowired
	private RestTemplate restTemplate;

	// @CrossOrigin
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(HttpServletResponse httpServletResponse)
	{
		return this.message;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletResponse httpServletResponse)
	{
		return "index";
	}

	@RequestMapping(value = "/call", method = RequestMethod.GET)
	public String callOtherService()
	{
		return restTemplate.getForEntity("http://AUTH-SERVICE/auth/home", String.class).getBody();
	}
}
