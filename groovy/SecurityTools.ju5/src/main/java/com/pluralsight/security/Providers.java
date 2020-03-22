package com.pluralsight.security;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.Map;


/**
 * @author kevinj
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Providers
{
    public static void main(String[] args)
    {
        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            System.out.println(provider.getName() + " " + provider.getVersionStr());

            System.out.println("------------------------------------------------------------------------------------------");

            for (Map.Entry<Object, Object> objectObjectEntry : provider.entrySet()) {
                System.out.println("\t" + objectObjectEntry.getKey() + ": " + objectObjectEntry.getValue());
            }
            System.out.println("------------------------------------------------------------------------------------------");
        }
    }

}
