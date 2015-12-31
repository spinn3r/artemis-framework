package com.spinn3r.artemis.resource_finder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ResourceFilter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Allows us to find resources via a given filename pattern.  For example we
 * could search for *.json.
 *
 * We can then filter the resources by path.  This is the best way we've found
 * to do this so far and while expensive is currently reliable.
 */
public class ResourceFinder {

    public ImmutableList<URL> findResources( String pattern ) {

        List<URL> resources =
          CPScanner.scanResources( new ResourceFilter()
                                     .resourceName( pattern ) );

        return ImmutableList.copyOf( resources );

    }

    public ImmutableList<URL> findResources( String packageName, String pattern ) {

        List<URL> resources =
          CPScanner.scanResources( new ResourceFilter()
                                     .packageName( packageName )
                                     .resourceName( pattern ) );

        return ImmutableList.copyOf( resources );

    }

    /**
     * List directory contents for a resource folder. Not recursive.
     * This is basically a brute-force implementation.
     * Works for regular files and also JARs.
     *
     * @author Greg Briggs
     * @param clazz Any java class that lives in the same place as the resources you want.
     * @param path Should end with "/", but not start with one.
     * @return Just the name of each member item, not the full paths.
     * @throws URISyntaxException
     * @throws IOException
     */
    public Set<String> getResourceListing(Class<?> clazz, String path) throws IOException {

        URL dirURL = null;
        try {
            dirURL = clazz.getClassLoader().getResource(path);

            if (dirURL != null && dirURL.getProtocol().equals("file")) {
                /* A file path: easy enough */

                Set<String> result = Sets.newHashSet();
                File dir = new File( dirURL.toURI() );
                for (String current : dir.list()) {
                    result.add( path + "/" + current );
                }

                return result;

            }

            if (dirURL == null) {
            /*
             * In case of a jar file, we can't actually find a directory.
             * Have to assume the same jar as clazz.
             */
                String myClassName = clazz.getName().replace(".", "/") + ".class";
                dirURL = clazz.getClassLoader().getResource(myClassName);
                if ( dirURL == null ) {
                    throw new IOException( "Couldn't find dir URL for class " + clazz.getName() );
                }

            }

            if (dirURL.getProtocol().equals("jar")) {
            /* A JAR path */
                String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
                JarFile jar = new JarFile( URLDecoder.decode(jarPath, "UTF-8"));
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                Set<String> result = new HashSet<>(); //avoid duplicates in case it is a subdirectory
                while(entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(path)) { //filter according to the path
                        String entry = name.substring(path.length());
                        int checkSubdir = entry.indexOf("/");
                        if (checkSubdir >= 0) {
                            // if it is a subdirectory, we just return the directory name
                            entry = entry.substring(0, checkSubdir);
                        }
                        result.add(path + "/" + entry);
                    }
                }

                return result;
            }
        } catch (URISyntaxException e) {
            throw new IOException( e );
        }

        throw new UnsupportedOperationException( "Cannot list files for URL " + dirURL );

    }

}
