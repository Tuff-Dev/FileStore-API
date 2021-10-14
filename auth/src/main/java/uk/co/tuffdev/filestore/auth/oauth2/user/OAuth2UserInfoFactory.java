package uk.co.tuffdev.filestore.auth.oauth2.user;

import uk.co.tuffdev.filestore.auth.exception.OAuth2AuthenticationProcessingException;
import uk.co.tuffdev.filestore.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {

            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
