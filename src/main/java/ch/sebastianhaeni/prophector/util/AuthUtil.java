package ch.sebastianhaeni.prophector.util;

import ch.sebastianhaeni.prophector.dto.auth.LocalUser;
import ch.sebastianhaeni.prophector.exception.UnauthorizedException;
import ch.sebastianhaeni.prophector.model.ProphetModel;
import org.springframework.security.core.Authentication;

public final class AuthUtil {

    private AuthUtil() {
        // util
    }

    public static void checkModelPermission(Authentication authentication, ProphetModel model) {
        var principal = (LocalUser) authentication.getPrincipal();
        var user = principal.getUser();

        if (!model.getOwner().equals(user)) {
            throw new UnauthorizedException();
        }
    }

}
