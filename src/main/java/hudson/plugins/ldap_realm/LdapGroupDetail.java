package hudson.plugins.ldap_realm;

import hudson.security.GroupDetails;
import org.acegisecurity.GrantedAuthority;

import java.util.logging.Logger;

public class LdapGroupDetail extends GroupDetails {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(LdapGroupDetail.class.getName());

    private String name;

    public LdapGroupDetail(String name) {
        super();
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

}

