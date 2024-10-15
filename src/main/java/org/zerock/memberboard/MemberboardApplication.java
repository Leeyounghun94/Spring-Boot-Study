package org.zerock.memberboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // 감시 체제 발동 !
public class MemberboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberboardApplication.class, args);
    }

}
