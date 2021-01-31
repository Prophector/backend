package ch.sebastianhaeni.prophector.dto.auth;

public enum SocialProvider {

    GOOGLE("google"),
    GITHUB("github");

    private final String providerType;

    public String getProviderType() {
        return providerType;
    }

    SocialProvider(final String providerType) {
        this.providerType = providerType;
    }

}
