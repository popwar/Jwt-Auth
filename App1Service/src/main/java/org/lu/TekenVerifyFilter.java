package org.lu;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.java.Log;

@Log
@Component("tokenVerifyFilter")
public class TekenVerifyFilter implements Filter
{

//	@Autowired
//	private RestTemplate restTemplate;
	
	final private RestTemplate restTemplate = new RestTemplate();

	private static final String VALIDATE_TOKEN_URL = "http://localhost:8088/auth/validateToken";
	// private static final String VALIDATE_TOKEN_URL =
	// "http://AUTH-SERVICE/auth/validateToken";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String forwardURL = new StringBuffer(httpRequest.getScheme()).append("://").append(httpRequest.getServerName())
				.append(":").append(httpRequest.getServerPort()).append(httpRequest.getRequestURI()).toString();

		Map<String, String[]> parameters = httpRequest.getParameterMap();
		String thing = null;
		if (parameters.get("thing") != null)
		{
			thing = parameters.get("thing")[0];
		}

		String token = httpRequest.getHeader("auth-token");
		if (thing != null || (token != null && token != ""))
		{
			TokenRequest requestToken = new TokenRequest();
			if (thing != null)
			{
				requestToken.setToken(thing);
			}
			else if (token != null)
			{
				requestToken.setToken(token);
			}
			ResponseEntity<ReturnTokenResponse> ob = this.restTemplate.postForEntity(VALIDATE_TOKEN_URL, requestToken,
					ReturnTokenResponse.class);
			if (ob != null && ob.getBody()!=null)
			{
//				ReturnTokenResponse tokenResponse = ob.getBody();
//				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//				if (authentication == null || !authentication.isAuthenticated()
//						|| !((User) authentication.getPrincipal()).getUsername()
//								.equalsIgnoreCase(tokenResponse.getUsername()))
//				{
//					// generate spring security
//					httpRequest.getSession().setAttribute("SPRING_SECURITY_CONTEXT", this.setSpringAuthentication(
//							tokenResponse.getUsername(), Arrays.asList(tokenResponse.getRoles())));
//
//				}

				chain.doFilter(request, response);
				log.info("Validation finished and forward the request");
			}
			else
			{
				// https://jsonplaceholder.typicode.com/posts
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
				log.info("Finished with access denied");
				return;
			}
		}
		else
		{
			httpResponse.sendRedirect("http://localhost:8088/auth/toLogin?forwardURL=" + forwardURL);
			log.info("Redirect to login page");
			return;
		}

	}

	@Override
	public void destroy()
	{

	}

	private SecurityContext setSpringAuthentication(String username, List<String> roles)
	{
		// set login user to spring security context, currently just set userId.
		User user = new User(username, roles);
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
				user.getGrantedAuthorities());
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(auth);
		return securityContext;
	}

}
