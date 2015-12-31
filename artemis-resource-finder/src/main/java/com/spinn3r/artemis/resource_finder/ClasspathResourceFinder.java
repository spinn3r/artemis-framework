package com.spinn3r.artemis.resource_finder;

import com.spinn3r.artemis.resource_finder.references.FileResourceReference;
import com.spinn3r.artemis.resource_finder.references.ResourceReference;
import com.spinn3r.artemis.resource_finder.references.ZipEntryResourceReference;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A resource loader that uses <q>java.class.path</q> to enumerate all the entries
 * one by one, using a ZipFile to enumerate the entries or just opening the
 * directory manually.
 */
public class ClasspathResourceFinder {

    /**
     * for all elements of java.class.path get a Collection of resources Pattern
     * pattern = Pattern.compile(".*"); gets all resources
     *
     * @param pattern
     *            the pattern to match
     * @return the resources in the order they are found
     */
    public Collection<ResourceReference> getResources( Pattern pattern ) throws IOException {

        List<ResourceReference> result = new ArrayList<>();
        String classPath = System.getProperty( "java.class.path", "." );
        String[] classPathElements = classPath.split(System.getProperty("path.separator"));

        for( String element : classPathElements){
            result.addAll(getResources(element, pattern));
        }

        return result;

    }

    private Collection<ResourceReference> getResources( String element, Pattern pattern) throws IOException {
        List<ResourceReference> result = new ArrayList<>();


        File file = new File(element);

        if(file.isDirectory()){
            result.addAll(getResourcesFromDirectory(file, pattern));
        } else{
            result.addAll(getResourcesFromJarFile(file, pattern));
        }

        return result;

    }

    private Collection<ResourceReference> getResourcesFromJarFile( File file, Pattern pattern) throws IOException {

        List<ResourceReference> result = new ArrayList<>();

        try( ZipFile zf = new ZipFile(file); ) {

            Enumeration e = zf.entries();
            while (e.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) e.nextElement();
                String fileName = ze.getName();
                boolean accept = pattern.matcher( fileName ).matches();
                if (accept) {

                    ResourceReference resourceReference
                      = new ZipEntryResourceReference( ResourceHolder.JAR, fileName, file, ze );

                    result.add( resourceReference );

                }

            }

        }

        return result;
    }

    private Collection<ResourceReference> getResourcesFromDirectory( File directory, Pattern pattern) throws IOException {
        return getResourcesFromDirectory( directory, directory, pattern );
    }

    private Collection<ResourceReference> getResourcesFromDirectory( File root, File directory, Pattern pattern) throws IOException {

        Path rootDirectoryPath = Paths.get( root.getAbsolutePath() );

        List<ResourceReference> result = new ArrayList<>();

        File[] fileList = directory.listFiles();

        if ( fileList == null ) {
            return result;
        }

        for( File file : fileList ) {

            if( file.isDirectory() ) {

                // recurs into the sub directories.
                result.addAll(getResourcesFromDirectory( root, file, pattern));

            } else{

                Path filePath = Paths.get( file.getAbsolutePath() );

                Path relativePath = rootDirectoryPath.relativize( filePath );
                String fileName = relativePath.toString();

                boolean accept = pattern.matcher(fileName).matches();
                if(accept){

                    ResourceReference resourceReference
                      = new FileResourceReference( ResourceHolder.DIR, fileName, file );

                    result.add(resourceReference);

                }

            }
        }

        return result;

    }

}
