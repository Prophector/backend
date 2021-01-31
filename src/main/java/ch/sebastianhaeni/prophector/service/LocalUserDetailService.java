package ch.sebastianhaeni.prophector.service;

import ch.sebastianhaeni.prophector.dto.auth.LocalUser;
import ch.sebastianhaeni.prophector.exception.ResourceNotFoundException;
import ch.sebastianhaeni.prophector.model.User;
import ch.sebastianhaeni.prophector.util.GeneralUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("localUserDetailService")
public class LocalUserDetailService implements UserDetailsService {

    private final UserService userService;

    public LocalUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public LocalUser loadUserByUsername(final String email) throws UsernameNotFoundException {
        var user = userService.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email + " was not found in the database"));
        return createLocalUser(user);
    }

    @Transactional
    public LocalUser loadUserById(Long id) {
        var user = userService.findUserById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return createLocalUser(user);
    }

    private LocalUser createLocalUser(User user) {
        return new LocalUser(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                GeneralUtils.buildSimpleGrantedAuthorities(user.getRole()),
                user,
                null,
                null);
    }
}
