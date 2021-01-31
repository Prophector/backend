package ch.sebastianhaeni.prophector.dto.auth;

import lombok.Value;

import java.util.List;

@Value
public class UserInfo {
    String id;
    String displayName;
    String email;
    List<String> roles;
    String hash;
}
