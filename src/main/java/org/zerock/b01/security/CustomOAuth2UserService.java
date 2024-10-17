package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;
import org.zerock.b01.repository.MemberRepository;
import org.zerock.b01.security.dto.MemberSecurityDTO;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    /* 대부분의 소셜로그인은 0Auth2 방식으러 처리되는데 이는 문자열로 구성된 토큰을 주고받는 방식으로 토큰을 발행, 검사하는 방식 통해서 서버간의 데이터를 교환한다.

    CustomOAuth2UserService 에서는 카카오 서비스에서 가져온 이메일 이용하여 같은 이메일이 있는지 확인 해보고 없을 경우는 자동으로 회원가입으로 하고 멤버시큐리티 디티오에다가 반환한다.
   */

    //필드
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder ;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // loadUser dptjsms 카카오랑 연동된 결과를 OAuth2UserRequest 으로 처리하기 때문에 원하는 정보 즉, 이메일을 추출해야한다.

        log.info("userRequest - - - - - -");
        log.info(userRequest);

        log.info("oauth2 user - - - - - - -");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        String clientName = clientRegistration.getClientName();

        log.info("이름 : " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

       String email = null ;

       switch (clientName) {
           case "kakao":
               email = getKakaoEmail(paramMap);
               break;
       }

       log.info("==============================================================");
       log.info(email);
       log.info("==============================================================");



        return genetateDTO(email, paramMap);




    }

    private MemberSecurityDTO genetateDTO(String email, Map<String , Object> params) {
        // genetateDTO -> 이미 회원 가입된 회원에 대해서 기존 정보 반환하고 새롭게 소셜 로그인된 사용자는 자동으로 회원가입 처리

        Optional<Member> result = memberRepository.findByEmail(email);

        // DB에 해당 이메일 사용자가 없으면?
        if (result.isEmpty()) {

            //회원 추가 - mid = 이메일 주소, 패스워드 1111
            Member member = Member.builder()
                    .mid(email)
                    .mpw(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            // 멤버시큐리티DTO 반환, 구성
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(email, "1111", email, false, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberSecurityDTO.setProps(params);

            return memberSecurityDTO;
        }else {

            Member member = result.get();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    member.getMid(),
                    member.getMpw(),
                    member.getEmail(),
                    member.isDel(),
                    member.isSocial(),
                    member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name())).collect(Collectors.toList())
            );

            return memberSecurityDTO;
        }
    }

    private String getKakaoEmail(Map<String, Object>  paramMap) {

        log.info("KAKAO ============================================");

        Object value = paramMap.get("kakao_Account");

        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap)   value ;

        String email = (String)accountMap.get("email");

        log.info("email = = = = = =" + email);

        return email ;
    }
}
