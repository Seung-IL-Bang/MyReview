package com.web.myreview.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    // 스프링 시큐리티에서는 권한 코드에 항상 'ROLE_' 이 앞에 있어야만 한다.
    GUEST("ROLE_GUEST", "게스트"),
    USER("ROLE_USER", "회원");

    private final String authorization;
    private final String title;
}
