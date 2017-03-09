package org.lu;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@EnableDiscoveryClient
@SpringBootApplication
public class AuthenticationApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(AuthenticationApplication.class, args);
	}
}

@Controller
class AuthenticationController extends WebMvcConfigurerAdapter
{
	@Autowired
	private TokenUtils tokenUtils;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	@ResponseBody
	public String home()
	{
		return "home";
	}

	@RequestMapping(value = "/validateToken", method = RequestMethod.POST)
	@ResponseBody
	public ReturnTokenResponse authenticateToken(@RequestBody TokenRequest token)
	{
		Optional<ReturnTokenResponse> a = tokenUtils.validateToken(token.getToken(), "tomcat");
		ReturnTokenResponse response = a.get();
		return response;

	}

	@RequestMapping(value = "/toLogin", method = RequestMethod.GET)
	public String gotoLogin(@RequestParam("forwardURL") String forwardURL, Map<String, Object> model) // forwardURL
	{
		model.put("time", new Date());
		model.put("forwardURL", forwardURL);
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public Response login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse)
			throws UnsupportedEncodingException
	{
		// skip verify user and get role from repository
		String username = loginRequest.getUsername();
		String role = "user";
		String token = tokenUtils.generateToken(username, role);

		Response response = new Response(URLDecoder.decode(loginRequest.getUrl(), "UTF-8"), token);
		return response;
	}

	private class Response
	{
		final private String url;
		final private String token;

		public Response(String url, String token)
		{
			super();
			this.url = url;
			this.token = token;
		}

		public String getUrl()
		{
			return url;
		}

		public String getToken()
		{
			return token;
		}

	}
}
