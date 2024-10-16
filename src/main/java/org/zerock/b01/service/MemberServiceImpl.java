package org.zerock.b01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.repository.MemberRepository;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    //필드
    private final ModelMapper modelMapper ;// 모델 객체용 처리

    private final MemberRepository memberRepository;    // 회원 가입

    private final PasswordEncoder passwordEncoder;  // 패스워드 인코딩


    //메서드
    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistExcetion {
        // mid가 존재할 경우 이 메서드가 발생 -> 정상적인 회원가입이 되면 패스워드를 인코딩함

        String mid = memberJoinDTO.getMid();

        boolean exist = memberRepository.existsById(mid);
        // existsById 이용하여 mid 값이 유일한지 체크하고 문제 생기면 입셉션 발동

        if (exist) {

            throw new MidExistExcetion();
        }

        Member member = modelMapper.map(memberJoinDTO, Member.class);

        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
        member.addRole(MemberRole.USER);

        log.info("= = = = = = = = = = = =  = = = = = = = = = = =  = = = = = = = = = ");
        log.info("MemberServiceImpl.join 메서드 실행 - - - - - - ");
        log.info(member);
        log.info(member.getRoleSet());

        memberRepository.save(member);

    }
}
