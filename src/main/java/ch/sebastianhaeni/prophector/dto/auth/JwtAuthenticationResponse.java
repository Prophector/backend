package ch.sebastianhaeni.prophector.dto.auth;

import lombok.Value;

@Value
public class JwtAuthenticationResponse {
    String accessToken;
    UserInfo user;
}
