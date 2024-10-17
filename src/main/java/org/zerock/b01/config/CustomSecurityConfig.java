package org.zerock.b01.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.b01.security.CustomUserDetailService;
import org.zerock.b01.security.handler.Custom403Handler;
import org.zerock.b01.security.handler.CustomSocialLoginSuccessHandler;

import javax.sql.DataSource;

@Configuration  // 스프링 부트 환경설정을 참고한다
@RequiredArgsConstructor    // @Configuration 와 세트
@Log4j2
@EnableMethodSecurity(prePostEnabled = true)// 시큐리티6버전에서 추가!
// prePostEnabled -> 원하는곳에 @PreAutohorize / @PostAuthorize 를 사용하여 권한 체크!
//@EnableGlobalAuthentication -> 시큐리티 5버전이하 에서는 사용
public class CustomSecurityConfig {

    // Spring Security를 개발자가 수정해서 사용하는 환경설정 부분 !


    // Will secure any request with [org.springframework.security.web.session.DisableEncodeUrlFilter@3a7149cb,  : 세션 ID가 URL에 포함되는 것을 막기 위해 HttpServletResponse를 사용해서 URL이 인코딩 되는 것을 막기 위한 필터이다.
// org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@6fdd8f7b, : SpringSecurityContextHolder는 기본적으로 ThreadLocal 기반으로 동작하는데, 비동기와 관련된 기능을 쓸 때에도 SecurityContext를 사용할 수 있도록 만들어주는 필터이다.
// org.springframework.security.web.context.SecurityContextHolderFilter@7dd9f279, : SecurityContext가 없으면 만들어주는 필터이다. SecurityContext는 Authentication 객체를 보관하는 인터페이스이다. SecurityContext를 통해 한 요청에 대해서 어떤 필터에서도 같은 Authentication 객체를 사용할 수 있다.
// org.springframework.security.web.header.HeaderWriterFilter@39713148, : 응답에 Security와 관련된 헤더 값을 설정해주는 필터이다
// org.springframework.security.web.csrf.CsrfFilter@46160e36, : CSRF 공격을 방어하기 위한 설정을 하는 필터이다.
// org.springframework.security.web.authentication.logout.LogoutFilter@28b2bd69, : 로그아웃 요청을 처리하는 필터이다.  아래에 DefaultLogoutPageGeneratingFilter가 로그아웃 기본 페이지를 생성한다.
// org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter@1e98f693, username과 password를 쓰는 form 기반 인증을 처리하는 필터이다. //AuthenticationManager를 통한 인증을 실행한다.//성공하면 Authentication 객체를 SecurityHolder에 저장한 후 AuthenticationSuccessHandler를 실행한다. //실패하면 AuthenticationFailureHandler를 실행한다.
// org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter@27a4d812,  : 로그인 기본 페이지를 생성하는 필터이다.
// org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter@794e9ef0,  : 로그아웃 기본 페이지를 생성하는 필터이다.
// org.springframework.security.web.authentication.www.BasicAuthenticationFilter@93b8846, :HTTP header에 인증 값을 담아 보내는 BASIC 인증을 처리하는 필터이다.
// org.springframework.security.web.savedrequest.RequestCacheAwareFilter@f5e605, : 인증 처리 후 원래의 Request 정보로 재구성하는 필터이다.
// org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@2de21626,  : 서블릿 API 보안 메서드를 구현하는 요청 래퍼로 서블릿 요청을 채우는 필터이다.
// org.springframework.security.web.authentication.AnonymousAuthenticationFilter@4332ec9a, : 이 필터에 올 때까지 사용자가 인증되지 않았다면, 이 요청은 익명의 사용자가 보낸 것으로 판단할 수 있다. 이 익명 사용자에 관한 처리를 하는 필터이다.
// org.springframework.security.web.access.ExceptionTranslationFilter@3f49049d,  : 필터 처리 과정에서 인증 예외 또는 인가 예외가 발생한 경우 해당 예외를 잡아서 처리하는 필터이다.
// org.springframework.security.web.access.intercept.AuthorizationFilter@37ff1cbc] : HttpServletRequest에게 인증(authorization)을 제공한다. 이것은 Security Filter들의 하나인 FilterChainProxy안에 삽입되어있다
// SessionManagementFilter : 세션 생성 전략을 설정하는 필터이다. 최대 동시 접속 세션을 설정하고, 유효하지 않은 세션으로 접근했을 때의 처리, 세션 변조 공격 방지 등의 처리를 담당한다.
// ExceptionTranslationFilter : 필터 처리 과정에서 인증 예외 또는 인가 예외가 발생한 경우 해당 예외를 잡아서 처리하는 필터이다.
// FilterSecurityInterceptor  : 인가를 결정하는 AccessDicisionManager에게 접근 권한이 있는지 확인하고 처리하는 필터이다. 앞 필터들을 통과할 때 인증 또는 인가에 문제가 있으면 ExceptionTranslationFilter로 예외를 던진다.

    // 이 부분은 최소 10번은 읽어보고 이해하도록 하자..

    // 필드 주입
    private final DataSource dataSource ;   // DB 커넥션 풀

    private final CustomUserDetailService userDetailsService ;  // userDetail 정보 가지고 있음.




    @Bean   // API에서 사용할 객체를 선언한다
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 강제 로그인 화면을 안하게 설정 -> board/list -> login

        log.info("CustomSecurityConfig.SecurityFilterChain 메서드 실행 - - - - - - - -");

        log.info("--------------강제로 로그인 하지 않음--------------");
        log.info("--------------모든 사용자가 모든 경로에 접근 할 수 있음.---------");
        log.info("--------------application.properties파일에 로그 출력 레벨 추가---------");


        // 로그인 창 수동 설정
        // 5버전 이하 코드 : http.formLogin();
        http.formLogin(form -> {

            form.loginPage("/member/login")
                    .defaultSuccessUrl("/board/list");
        }// 자동으로 시큐리티에서 만든 로그인 폼을 내가 만든 로그인폼으로 활용한다.

        );

        // There was an unexpected error (type=Forbidden, status=403). -> csrf 토큰이 필요하다고 오류를 발생(해킹때문에)
        // 비활성화 처리(실무에서는 안된다.)

        // 5버전 코드 : http.csrf().disable();
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());  // 6버전 코드
        // 정식 발행 시 토큰을 사용을 해야 보안상 좋기 때문에 각 html 파일에다가 csrf 토큰을 전달해서 해결 해야한다.(테스트 할 때는 비활성하고)

        /* 여기에서 CSRF 토큰이 나오는데, 이 방식은 사이트 이용할때마다 매번 변경되는 문자열 생성하고 이를 요청 시 검증하는 방식이다.
        시큐리티의 경우 GET 제외한 나머지 방식에서는 토큰을 전송하도록 되어있다.
                토큰 이용하는것이 보안상 좋지만 기존의 POST/DELETE/PUT + AJAX 까지 수정 해야 한다는 단점이 있다.

         */

        http.rememberMe(httpSecurityRememberMeConfigurer -> {

            httpSecurityRememberMeConfigurer.key("12345678")    // 토큰 발행 시 참고되는 키 값
                    .tokenRepository(persistentTokenRepository())   // db를 사용하는 토큰 저장소
                    .userDetailsService(userDetailsService) // 사용자 정보에 대한 객체
                    .tokenValiditySeconds(60*60*24*30); // 쿠키 AgeTime(30일)
        });
        /*
        remember-me ->  쿠키에 유효 기간을 지정하여 쿠키를 브라우저에 보관하게 하고 쿠키 값인 특정한 문자열 보관하여 로그인 관련 정보를 유지 시키는 방식이다.
         */

        http.exceptionHandling((httpSecurityExceptionHandlingConfigurer -> {

            httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler());

        }));


        http.oauth2Login( httpSecurityOAuth2LoginConfigurer -> {

            httpSecurityOAuth2LoginConfigurer.loginPage("/member/login");

            httpSecurityOAuth2LoginConfigurer.successHandler(authenticationSuccessHandler());
        });

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // CSS, JS 파일에 필터가 적용되면 안되므로 수정 설정 메서드

        log.info("CustomSecurityConfig.WebSecurityCustomizer 메서드 실행  - - - - - - -");
        log.info("JS, CSS 와 같은 정적 페이지에 대한 Security 비활성화");

        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        // http://localhost:8080/css/style.css  정적 경로가 보이기 시작한다.
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 패스워드 암호화 처리해준다.

        log.info("CustomSecurityConfig.passwordEncoder 메서드 실행 - - - - - -");
        log.info("패스워드 암호화 처리중 . . . .");

        return new BCryptPasswordEncoder();// BCrypt라는 방식의 암호화 처리
    }


    @Bean // 위에 http.rememberMe 의 .tokenRepository(persistentTokenRepository()) 처리용
    public PersistentTokenRepository persistentTokenRepository() {

        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);

        return repo;
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {

        return new Custom403Handler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }
}

