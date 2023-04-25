package com.web.myreview.config.handler;

import com.web.myreview.config.auth.dto.OAuthAttributes;
import com.web.myreview.domain.user.User;
import com.web.myreview.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class OAuth2UserSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();


        OAuthAttributes attributes = OAuthAttributes.of(registrationId, (OAuth2User) authentication.getPrincipal());

        User user = saveOrUpdate(attributes);
        redirect(request, response);

    }

    private void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String accessToken = delegateAccessToken(username, authorities);  // (6-1)
//        String refreshToken = delegateRefreshToken(username);     // (6-2)

        String uri = createURI("accessToken", "refreshToken", request).toString();   // (6-3)
        getRedirectStrategy().sendRedirect(request, response, uri);   // (6-4)
    }

    private URI createURI(String accessToken, String refreshToken, HttpServletRequest request) {

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);


        return UriComponentsBuilder
                .newInstance()
                .scheme("https")
                .host("localhost")
                .port(3000)
                .path("/")
                .queryParams(queryParams)
                .build()
                .toUri();
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
