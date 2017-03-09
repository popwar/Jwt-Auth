package org.lu;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils
{
	@Value("${token.secret}")
	private String secret;

	@Value("${token.expire.period}")
	private int expiration;

	public String getUsernameFromToken(String token)
	{

		Claims claims = this.getClaimsFromToken(token);
		String username = claims.get("audience", String.class);

		return username;
	}

	public String getRoleFromToken(String token)
	{
		Claims claims = this.getClaimsFromToken(token);
		String role = claims.get("role", String.class);
		return role;
	}

	public Date getCreatedDateFromToken(String token)
	{
		Date created;
		try
		{
			final Claims claims = this.getClaimsFromToken(token);
			created = new Date((Long) claims.get("created"));
		}
		catch (Exception e)
		{
			created = null;
		}
		return created;
	}

	public Date getExpirationDateFromToken(String token)
	{
		Date expiration;
		try
		{
			final Claims claims = this.getClaimsFromToken(token);
			expiration = claims.getExpiration();
		}
		catch (Exception e)
		{
			expiration = null;
		}
		return expiration;
	}

	private Claims getClaimsFromToken(String token)
	{
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();

		return claims;
	}

	private Date generateCurrentDate()
	{
		return new Date(System.currentTimeMillis());
	}

	private Date generateExpirationDate()
	{
		return new Date(System.currentTimeMillis() + this.expiration);
	}

	private Boolean isTokenExpired(String token)
	{
		final Date expiration = this.getExpirationDateFromToken(token);
		return expiration.before(this.generateCurrentDate());
	}

	public String generateToken(String userName, String role)
	{
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("audience", userName);
		claims.put("subject", "validate");
		claims.put("created", this.generateCurrentDate());
		claims.put("role", role);
		return this.generateToken(claims);
	}

	private String generateToken(Map<String, Object> claims)
	{
		return Jwts.builder().setClaims(claims).setExpiration(this.generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, this.secret).compact();
	}

	public String refreshToken(String token)
	{
		String refreshedToken;
		try
		{
			final Claims claims = this.getClaimsFromToken(token);
			claims.put("created", this.generateCurrentDate());
			refreshedToken = this.generateToken(claims);
		}
		catch (Exception e)
		{
			refreshedToken = null;
		}
		return refreshedToken;
	}

	public static Boolean validateToken(String token)
	{
		if (token.equals("tomcat"))
		{
			return true;
		}
		return false;
	}

	public Optional<ReturnTokenResponse> validateToken(String token, String userNameFromRepo)
	{
		Optional<ReturnTokenResponse> result = Optional.empty();
		final String username = this.getUsernameFromToken(token);
		// final Date created = this.getCreatedDateFromToken(token);
		// final Date expiration = this.getExpirationDateFromToken(token);
		if ((username.equals(userNameFromRepo) && !(this.isTokenExpired(token))))
		{
			ReturnTokenResponse rtr = new ReturnTokenResponse();
			rtr.setUsername(username);
			rtr.setRoles(this.getRoleFromToken(token));

			return result = Optional.ofNullable(rtr);
		}
		return result;
	}

}
