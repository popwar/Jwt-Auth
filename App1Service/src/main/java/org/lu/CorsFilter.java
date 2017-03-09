package org.lu;

import lombok.extern.java.Log;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Log
@Component("corsFilter")
public class CorsFilter implements Filter
{

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");

		String allowedHeaders = request.getHeader("Access-Control-Request-Headers");
		if (allowedHeaders != null && allowedHeaders.indexOf("auth-token") != -1)
		{
			response.setHeader("Access-Control-Allow-Headers", "auth-token");
		}

		// Just ACCEPT and REPLY OK if OPTIONS
		if (request.getMethod().equals("OPTIONS"))
		{
			response.setStatus(HttpServletResponse.SC_OK);
			return;
		}
		chain.doFilter(req, res);
		log.info("Server finished setting up cors response");
	}

	public void init(FilterConfig filterConfig)
	{
	}

	public void destroy()
	{
	}

}
