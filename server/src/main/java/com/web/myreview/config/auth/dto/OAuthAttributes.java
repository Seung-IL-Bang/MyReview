package com.web.myreview.config.auth.dto;

import com.web.myreview.domain.user.Role;
import com.web.myreview.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private OAuth2User oAuth2User;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(OAuth2User oAuth2User, String name, String email, String picture) {
        this.oAuth2User = oAuth2User;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId,
                                     OAuth2User oAuth2User) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(oAuth2User);
        }
        return ofGoogle(oAuth2User);
    }

    private static OAuthAttributes ofGoogle(OAuth2User oAuth2User) {

        return OAuthAttributes.builder()
                .name(oAuth2User.getAttributes().get("name").toString())
                .email(oAuth2User.getAttributes().get("email").toString())
                .picture(oAuth2User.getAttributes().get("picture").toString())
                .build();
    }

    private static OAuthAttributes ofKakao(OAuth2User oAuth2User) {
        Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
        Map<String, Object> account = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");

        return OAuthAttributes.builder()
                .name(properties.get("nickname").toString())
                .email(account.get("email").toString())
                .picture(properties.get("profile_image").toString())
                .build();
    }


    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)
                .build();
    }
}
