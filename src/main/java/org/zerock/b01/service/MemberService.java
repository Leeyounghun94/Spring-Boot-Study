package org.zerock.b01.service;

import org.zerock.b01.dto.MemberJoinDTO;

public interface MemberService {

    // 회원가입에서 유심히 볼 것은 이미 해당 아이디가 존재할 경우 MemberRepository의 save()가 insert가 아닌 update로 실행되야 한다. 같은 아이디면 중복으로 예외 발생


    static class MidExistExcetion extends Exception {
        // MidExistExcetion 이라는 예외를 static으로 선언해서 필요한 곳에서 사용하도록 한다.
    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistExcetion ;

}
