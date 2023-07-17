package com.example.jisayboard.config;

import com.example.jisayboard.service.CustomOAuth2UserService;
import com.example.jisayboard.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final LoginService loginService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true);
        web.httpFirewall(firewall);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/member/myPage").hasRole("MEMBER")
                .antMatchers("/member/updatePassword").hasRole("MEMBER")
                .antMatchers("/cars2").hasRole("MEMBER")
                .regexMatchers("^.*(?<!/)/{2}.*$").permitAll()


                .and().formLogin()
                .loginPage("/loginPage")
                .defaultSuccessUrl("/list")
                .failureUrl("/loginPage")
                .usernameParameter("memberId")
                .passwordParameter("memberPassword")
                .permitAll()


                .and()
                .oauth2Login()
                .defaultSuccessUrl("/")
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()

                .and().logout()
                .logoutUrl("/member/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")


                .and().
                headers().frameOptions().sameOrigin()

                .and().requiresChannel()
                .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                .requiresSecure();

//                .and()
//                .sessionManagement()
//                .maximumSessions(1) //세션 최대 허용 수
//                .maxSessionsPreventsLogin(false);




    }
}