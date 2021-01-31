package ch.sebastianhaeni.prophector.model;

import ch.sebastianhaeni.prophector.dto.auth.SocialProvider;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String providerUserId;

    private String email;

    private boolean enabled;

    private String displayName;

    private LocalDate createdDate;

    private LocalDate modifiedDate;

    private String password;

    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

    @Enumerated(EnumType.STRING)
    private Role role;
}
