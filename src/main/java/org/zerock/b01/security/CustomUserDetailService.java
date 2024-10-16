package org.zerock.b01.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.repository.MemberRepository;
import org.zerock.b01.security.dto.MemberSecurityDTO;

import java.util.Optional;
import java.util.stream.Collectors;


@Log4j2
@Service
//@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    // 스프링 시큐리티 객체의 실제로 인증을 처리하는 UserDetailsService 인터페이스의 구현체가 있는데 이 구현체를 내 마음대로 Custom 처리 하는 클래스..
    // UserDetailsService 인터페이스는 loadUserByUsername() 이라는 메서드가 있는데 이 메서드는 실제 인증을 처리할 때 호출되는 부분이다



    //필드
    private PasswordEncoder passwordEncoder;

    private MemberRepository memberRepository ;


    //기본 생성자
    public CustomUserDetailService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }// 현재 클래스가 동작할 때 기본적으로 암호 처리 객체를 생성한다.
    //BCryptPasswordEncoder -> 알고리즘으로 암호화 처리, 같은 문자열이라고 해도 처리 결과가 매번 다르기 때문에 패스워드 암호화에 많이 사용




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 로그인 시 아이디를 처리를 담당하는 메서드
        // 리턴 객체는 UserDetails라는 객체, -> 인증과 관련된 정보를 저장하는 역할한다!

        log.info("CustomUserDetailService.loadUserByUsername 메서드 실행  - - - - -");
        log.info("loadUserByUsername" + username);

        Optional<Member> result = memberRepository.getWithRoles(username);

        if (result.isEmpty()) {// 해당 아이디 사용자가 없다면
            throw new UsernameNotFoundException("해당 정보를 찾을 수 없습니다.");
        }

        Member member = result.get();   // 결과를 가져와서 엔티티에 담는다.

        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(    // 멤버 객체에 있는 정보를 멤버 시큐리티 DTO에다가 담는다.
                member.getMid(),
                member.getMpw(),
                member.getEmail(),
                member.isDel(),
                false,
                member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name())).collect(Collectors.toList())    // 룰을 가져와서 콜렉션 처리한다.
        );

        log.info("멤버 시큐리티 디티오 적용 한 값  = = = = = = =" + memberSecurityDTO);
        log.info(memberSecurityDTO);


       /* UserDetails userDetails = User.builder()
                .username("user1")
                //.password("1111") -> 암호화가 안됨.
                .password(passwordEncoder.encode("1111"))
                .authorities("ROLE_USER")   // 유저 권한
                //.authorities("ROLE_ADMIN") -> 이렇게도 쓰는다는걸 알고가자.
                .build();

        return userDetails ;

       729p. 제거 */

        return memberSecurityDTO;
    }
}
