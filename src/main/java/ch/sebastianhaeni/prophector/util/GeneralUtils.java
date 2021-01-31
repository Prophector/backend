package ch.sebastianhaeni.prophector.util;

import ch.sebastianhaeni.prophector.dto.auth.LocalUser;
import ch.sebastianhaeni.prophector.dto.auth.SocialProvider;
import ch.sebastianhaeni.prophector.dto.auth.UserInfo;
import ch.sebastianhaeni.prophector.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralUtils {

    public static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(Role... roles) {
        return Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    public static SocialProvider toSocialProvider(String providerId) {
        return Arrays.stream(SocialProvider.values())
                .filter(socialProvider -> socialProvider.getProviderType().equals(providerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Social provider " + providerId + " is not implemented."));
    }

    public static UserInfo buildUserInfo(LocalUser localUser) {
        var roles = localUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        var user = localUser.getUser();
        return new UserInfo(user.getId().toString(), user.getDisplayName(), user.getEmail(), roles, MD5Util.md5Hex(user.getEmail()));
    }
}
