package kr.co.market.config;

import kr.co.market.provider.CustomAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private SessionRegistry sessionRegistry;

    private final String[] ignorePaths = {"/img/**", "/js/**", "/css/**", "/bootstrap/**", "/favicon.ico", "/senderLoginCode", "/pinSalesInfo"};

    @Autowired
    @Override
    public void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.customAuthenticationProvider);
    }

    @Bean(name = "sessionRegistry")
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers(ignorePaths);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeRequests(authorize -> authorize.antMatchers("/test/**").permitAll());
//        httpSecurity.authorizeRequests(authorize -> authorize.antMatchers("/updateUsedPin").permitAll());

        httpSecurity.authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/login", "/loginFailed").permitAll()
                .antMatchers("/**").authenticated()
                .and().formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginProcess")
                .failureUrl("/loginFailed")
                .defaultSuccessUrl("/loginSuccess", true)
                .usernameParameter("userid")
                .passwordParameter("password")
                .permitAll()
                .and().logout()
//                .logoutUrl("/cp/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);
        httpSecurity.csrf().disable();

        httpSecurity.sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .sessionRegistry(sessionRegistry);
    }

    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

    @Bean                           // bean for http session listener
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {               // This method will be called when session created
                log.info("Session Created with session id : {}", se.getSession().getId());
            }
            @Override
            public void sessionDestroyed(HttpSessionEvent se) {         // This method will be automatically called when session destroyed
                log.info("Session Destroyed, Session id : {}", se.getSession().getId());
            }
        };
    }

}
