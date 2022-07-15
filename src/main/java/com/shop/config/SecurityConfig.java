package com.shop.config;

import com.shop.service.CustomOAuth2UserService;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberService memberService;
    private final CustomOAuth2UserService customOAuth2UserService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * .loginPage("로그인 페이지 경로")  -> 앞으로 로그인은 이 경로에서 수행하게 된다는 뜻이다.
         * .loginProcessingUrl("로그인 처리 경로") -> 로그인 form 의 action 과 일치시켜주어야 한다.
         */
        http
                .csrf().ignoringAntMatchers("/api/**") /* REST API 사용 예외처리 */
                .and()
                .formLogin()
                /**
                 * loginPage를 사용하면 loginProcessingUrl 가 null일 경우에 loginPage의 파라미터로 변경시킨다
                 * ex)
                 *  .loginPage("/members/login")만 사용하게 되면
                 *  .loginProcessingUrl("/members/login") 자동 설정
                 */
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        http.authorizeRequests()
                .mvcMatchers("/posts/write/**").authenticated()
                .mvcMatchers("/", "/members/**","/error/**","/posts/**",
                        "/item/**", "/images/**", "/posts/read/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());

    }
}
