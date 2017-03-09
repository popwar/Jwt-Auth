package org.lu;

import javax.annotation.Resource;
import javax.servlet.Filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Resource(name = "corsFilter")
	private Filter corsFilter;

	@Resource(name = "tokenVerifyFilter")
	private Filter tokenVerifyFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated().and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().formLogin().disable();

		http.addFilterBefore(corsFilter, SecurityContextPersistenceFilter.class);
		http.addFilterBefore(tokenVerifyFilter, SecurityContextPersistenceFilter.class);
	}
}
