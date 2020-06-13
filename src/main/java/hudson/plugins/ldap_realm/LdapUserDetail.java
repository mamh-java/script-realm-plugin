package hudson.plugins.ldap_realm;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.User;

import java.util.logging.Logger;

public class LdapUserDetail extends User {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(LdapUserDetail.class.getName());

    // additional attributes from Active Directory
    private final String displayName ;
    private final String mail;

    public LdapUserDetail(String username, String password,
                          boolean enabled, boolean accountNonExpired,
                          boolean credentialsNonExpired, boolean accountNonLocked,
                          GrantedAuthority[] authorities,
                          String displayName, String mail)
            throws IllegalArgumentException {
        // Acegi doesn't like null password, but during remember-me processing
        // we don't know the password so we need to set some dummy. See #1229
        super(username, password != null ? password : "PASSWORD", enabled,
                accountNonExpired, credentialsNonExpired, accountNonLocked,
                authorities);

        this.displayName = displayName;
        this.mail = mail;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getMail() {
        return mail;
    }


}

