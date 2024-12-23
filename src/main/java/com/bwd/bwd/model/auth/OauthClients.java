package com.bwd.bwd.model.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "oauth_clients")
@Entity
@Getter
@Setter
public class OauthClients implements UserDetails {
	private static final long serialVersionUID = -7858869558953243875L;
	
	@Id
    @Column(name = "client_id", length = 80)
    private String client_id;
	
	@Column(name = "client_secret")
	private String client_secret;
	

	
	public void setValues(String ci, String cs)
	{
		this.client_id = ci;
		this.client_secret = cs;
	
	}
	
	public OauthClients getOauthClients()
	{
		return this;
	}
	
	public void setOauthClients(OauthClients oc)
	{
		this.client_id = oc.client_id;
		this.client_secret = oc.client_secret;
		
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		//return List.of(new SimpleGrantedAuthority(role.name()));
		return null;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return client_secret;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return client_id;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true; 
	}
}
