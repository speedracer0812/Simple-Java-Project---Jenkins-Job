package org.lacare.idam.ad.query;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;

public class QueryLdap {

    // port: 389
    private static String url = "ldap://localhost:10389";
    private static String username = "freewind";
    private static String password = "mypassword";

    static DirContext ldapContext;

    public static void main(String[] args) throws NamingException {
        System.out.println("Java test Active Directory");

        Hashtable<String, String> ldapEnv = new Hashtable<>(11);
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnv.put(Context.PROVIDER_URL, url);
        ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        ldapEnv.put(Context.SECURITY_PRINCIPAL, username);
        ldapEnv.put(Context.SECURITY_CREDENTIALS, password);
        //ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
        //ldapEnv.put(Context.SECURITY_PROTOCOL, "simple");
        ldapContext = new InitialDirContext(ldapEnv);

        // Create the search controls
        SearchControls searchCtls = new SearchControls();

        //Specify the attributes to return
        String returnedAtts[] = {"sn", "givenName", "samAccountName"};
        searchCtls.setReturningAttributes(returnedAtts);

        //Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //specify the LDAP search filter
        String searchFilter = "(&(cn=test*)(objectClass=user))";

        //Specify the Base for the search
        String searchBase = "dc=archerscripts,dc=com";
        //initialize counter to total the results
        int totalResults = 0;

        // Search for objects using the filter
        NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);

        //Loop through the search results
        while (answer.hasMoreElements()) {
            SearchResult sr = answer.next();

            totalResults++;

            System.out.println(">>>" + sr.getName());
            Attributes attrs = sr.getAttributes();
            System.out.println(">>>>>>" + attrs.get("samAccountName"));
        }

        System.out.println("Total results: " + totalResults);
        ldapContext.close();
    }

}
