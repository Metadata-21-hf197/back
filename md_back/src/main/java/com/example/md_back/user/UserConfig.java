package com.example.md_back.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class UserConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AuthProvider authProvider;

    //DB를 사용한 사용자 인증 처리 설정
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userService()).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(authProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
        web.ignoring().antMatchers("/css/**",
                "/js/**",
                "/img/**",
                "/lib/**",
                "/templates/fragments/**",
                "templates/layouts/**");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //권한 필요한 URI : antMatchers("URI").authenticated()
        //권한 불필요한 URI : antMatchers("URI").permitAll()
        http.authorizeRequests()
                .antMatchers("/**").authenticated()

                //security_login
                .and()
                .formLogin()
                .loginProcessingUrl("/user/login")
                .loginPage("/user/login")
                .defaultSuccessUrl("/") //main으로 수정
                .successHandler(new LoginSuccessHandler())
                .failureHandler(new LoginFailHandler())
                .permitAll()

                .and()
                .logout()
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/")//main으로 수정
                .invalidateHttpSession(true)
                .logoutSuccessHandler(new MyLogoutSuccessHandler())
                .permitAll();
    }

}