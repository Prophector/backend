package ch.sebastianhaeni.prophector.dto.auth;

import ch.sebastianhaeni.prophector.util.GeneralUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class LocalUser extends User implements OAuth2User, OidcUser {

    @Nullable
    private final OidcIdToken idToken;
    @Nullable
    private final OidcUserInfo userInfo;
    private final ch.sebastianhaeni.prophector.model.User user;
    private Map<String, Object> attributes;

    public LocalUser(final String userId,
                     final String password,
                     final boolean enabled,
                     final boolean accountNonExpired,
                     final boolean credentialsNonExpired,
                     final boolean accountNonLocked,
                     final Collection<? extends GrantedAuthority> authorities,
                     final ch.sebastianhaeni.prophector.model.User user,
                     @Nullable OidcIdToken idToken,
                     @Nullable OidcUserInfo userInfo) {
        super(userId, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.user = user;
        this.idToken = idToken;
        this.userInfo = userInfo;
    }

    public static LocalUser create(ch.sebastianhaeni.prophector.model.User user,
                                   Map<String, Object> attributes,
                                   OidcIdToken idToken,
                                   OidcUserInfo userInfo) {
        var localUser = new LocalUser(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                GeneralUtils.buildSimpleGrantedAuthorities(user.getRole()),
                user,
                idToken,
                userInfo);
        localUser.setAttributes(attributes);
        return localUser;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return this.user.getDisplayName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.attributes;
    }

    @Override
    public @Nullable OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

    @Override
    public @Nullable OidcIdToken getIdToken() {
        return this.idToken;
    }

    public ch.sebastianhaeni.prophector.model.User getUser() {
        return user;
    }
}
