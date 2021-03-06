package com.example.md_back.user;

import com.example.md_back.dto.LoginDTO;
import com.example.md_back.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthProvider implements AuthenticationProvider {
    @Autowired
    UserService userService;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
        String password = authentication.getCredentials().toString();
        return authenticate(id, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public Authentication authenticate(String id, String password) throws org.springframework.security.core.AuthenticationException {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();

        LoginDTO principal = (LoginDTO) userService.loadUserByUsername(id);
        User pUser = principal.getUser();
        principal.setUsername(pUser.getMemberName());
        if(principal == null) {
            throw new UsernameNotFoundException("wrongid");
        } else if(principal != null && !principal.getPassword().equals(password)) {
            throw new BadCredentialsException("wrongpw");
        }

        grantedAuthorityList.add(new SimpleGrantedAuthority(principal.getUser().getUserRole()));

        return new MyAuthentication(id, password, grantedAuthorityList, principal);
    }

}
