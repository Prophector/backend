package ch.sebastianhaeni.prophector.service;

import ch.sebastianhaeni.prophector.dto.auth.LocalUser;
import ch.sebastianhaeni.prophector.exception.OAuth2AuthenticationProcessingException;
import ch.sebastianhaeni.prophector.exception.UserAlreadyExistAuthenticationException;
import ch.sebastianhaeni.prophector.model.Role;
import ch.sebastianhaeni.prophector.model.User;
import ch.sebastianhaeni.prophector.repository.UserRepository;
import ch.sebastianhaeni.prophector.security.oauth2.user.OAuth2UserInfo;
import ch.sebastianhaeni.prophector.security.oauth2.user.OAuth2UserInfoFactory;
import ch.sebastianhaeni.prophector.util.GeneralUtils;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(value = "transactionManager")
    public User registerNewUser(String registrationId, OAuth2UserInfo userInfo) throws UserAlreadyExistAuthenticationException {
        if (userRepository.existsByEmail(userInfo.getEmail())) {
            throw new UserAlreadyExistAuthenticationException("User with email id " + userInfo.getEmail() + " already exists");
        }
        var user = buildUser(registrationId, userInfo);
        var now = LocalDate.now();
        user.setCreatedDate(now);
        user.setModifiedDate(now);
        user = userRepository.save(user);
        userRepository.flush();
        return user;
    }

    private User buildUser(String registrationId, OAuth2UserInfo formDTO) {
        var user = new User();
        user.setDisplayName(formDTO.getName());
        user.setEmail(formDTO.getEmail());
        user.setPassword("");
        user.setRole(Role.ROLE_USER);
        user.setProvider(GeneralUtils.toSocialProvider(registrationId));
        user.setEnabled(true);
        user.setProviderUserId(formDTO.getId());
        return user;
    }

    public Optional<User> findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public LocalUser processUserRegistration(String registrationId, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) {
        var oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);
        if (!StringUtils.hasLength(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name was not provided.");
        } else if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException(
                    "Email address was not provided by social login. " +
                            "Please change your social account settings to include the email or choose a different provider.");
        }
        var user = findUserByEmail(oAuth2UserInfo.getEmail())
                .map(u -> {
                    if (!u.getProvider().equals(GeneralUtils.toSocialProvider(registrationId))) {
                        throw new OAuth2AuthenticationProcessingException(
                                "Looks like you are already signed up with a " + u.getProvider() + " account. " +
                                        "Please use your " + u.getProvider() + " account to login.");
                    }
                    return updateExistingUser(u, oAuth2UserInfo);
                })
                .orElseGet(() -> registerNewUser(registrationId, oAuth2UserInfo));

        return LocalUser.create(user, attributes, idToken, userInfo);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setDisplayName(oAuth2UserInfo.getName());
        return userRepository.save(existingUser);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
}
