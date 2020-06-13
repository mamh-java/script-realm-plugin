/*
 * The MIT License
 *
 * Copyright (c) 2004-2009, Sun Microsystems, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.ldap_realm;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.security.AbstractPasswordBasedSecurityRealm;
import hudson.security.GroupDetails;
import hudson.security.SecurityRealm;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.jfree.util.Log;
import org.kohsuke.stapler.DataBoundConstructor;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class LdapSecurityRealm extends AbstractPasswordBasedSecurityRealm {
    private static final Logger LOGGER = Logger.getLogger(LdapSecurityRealm.class.getName());

    private String server;

    @DataBoundConstructor
    public LdapSecurityRealm(String server) {
		this.server = server;
    }

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	/**
     * Authenticate a login attempt. 登陆核心方法
     * This method is the heart of a {@link AbstractPasswordBasedSecurityRealm}.
     */
    protected UserDetails authenticate(String username, String password) throws AuthenticationException {
    	// 这个方法就是通过ldap人在，然后返回一个UserDetails实例对象
		Log.info("this ldap server is: " + server);
		GrantedAuthority[] groups = loadGroups(username);
        LdapUserDetail user = new LdapUserDetail(username, "", true, true, true, true, groups, username, username);
        return user;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        GrantedAuthority[] groups = loadGroups(username);
        return new User(username, "", true, true, true, true, groups);
    }

    @Override
    public GroupDetails loadGroupByGroupname(final String groupname) throws UsernameNotFoundException, DataAccessException {
		LdapGroupDetail group = new LdapGroupDetail(groupname);
		return group;
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<SecurityRealm> {
        public String getDisplayName() {
            return "ldap realm"; // 插件的display name
        }
    }

    protected GrantedAuthority[] loadGroups(String username) throws AuthenticationException {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(AUTHENTICATED_AUTHORITY);
		GrantedAuthority[] groups = authorities.toArray(new GrantedAuthority[0]);

		return groups;
    }

}
