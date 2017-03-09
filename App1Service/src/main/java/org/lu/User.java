package org.lu;

import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

@Getter
public class User
{
	final private String username;
	final private List<GrantedAuthority> grantedAuthorities;

	public User(String username, List<String> roles)
	{
		this.username = username;
		this.grantedAuthorities = new LinkedList<>();
		roles.stream().forEach(e -> {
			GrantedAuthority grantedAuthority = new GrantedAuthority()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getAuthority()
				{
					return e;
				}
			};
			this.grantedAuthorities.add(grantedAuthority);
		});
	}

}
