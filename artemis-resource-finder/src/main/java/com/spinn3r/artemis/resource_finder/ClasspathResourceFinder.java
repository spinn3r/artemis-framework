package com.spinn3r.artemis.resource_finder;

import com.google.common.collect.ImmutableList;
import com.spinn3r.artemis.resource_finder.references.FileResourceReference;
import com.spinn3r.artemis.resource_finder.references.ResourceReference;
import com.spinn3r.artemis.resource_finder.references.ZipEntryResourceReference;
import com.spinn3r.log5j.Logger;

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
 *
 *
 */
public class ClasspathResourceFinder {

    private static final Logger log = Logger.getLogger();

    private final long maxJarFileSize;

    private final boolean ignoreBrokenJarFiles;

    private ClasspathResourceFinder(long maxJarFileSize, boolean ignoreBrokenJarFiles) {

        this.maxJarFileSize = maxJarFileSize;
        this.ignoreBrokenJarFiles = ignoreBrokenJarFiles;
    }

    public static class Builder {

        private long maxJarFileSize = 10_000_000;

        private boolean ignoreBrokenJarFiles = false;

        /**
         * Don't attempt to open large .jar files as the JVM has trouble with
         * these sometimes.
         *
         * @param maxJarFileSize
         */
        public Builder withMaxJarFileSize(long maxJarFileSize) {
            this.maxJarFileSize = maxJarFileSize;
            return this;
        }

        public Builder withIgnoreBrokenJarFiles(boolean ignoreBrokenJarFiles) {
            this.ignoreBrokenJarFiles = ignoreBrokenJarFiles;
            return this;
        }

        public ClasspathResourceFinder build() {
            return new ClasspathResourceFinder( maxJarFileSize, ignoreBrokenJarFiles );
        }

    }

    public ImmutableList<ResourceReference> findResources(String regex ) throws IOException {
        return findResources( Pattern.compile( regex ) );
    }

    /**
     * Find all elements of java.class.path, get a Collection of resources
     */
    public ImmutableList<ResourceReference> findResources(Pattern pattern ) throws IOException {

        List<ResourceReference> result = new ArrayList<>();
        String classPath = System.getProperty( "java.class.path", "." );
        String[] classPathElements = classPath.split(System.getProperty("path.separator"));

        for( String classPathElement : classPathElements){
            File file = new File(classPathElement);

            if ( ! file.exists() )
                continue;

            if ( ! file.isDirectory() && file.length() > maxJarFileSize )
                continue;

            result.addAll( findResources(file, pattern));

        }

        return ImmutableList.copyOf( result );

    }

    private Collection<ResourceReference> findResources(File file, Pattern pattern) throws IOException {
        List<ResourceReference> result = new ArrayList<>();

        if( file.isDirectory() ){
            result.addAll( findResourcesFromDirectory(file, pattern));
        } else {
            result.addAll( findResourcesFromJarFile(file, pattern));
        }

        return result;

    }

    private Collection<ResourceReference> findResourcesFromJarFile(File file, Pattern pattern) throws IOException {

        List<ResourceReference> result = new ArrayList<>();

        try( ZipFile zf = new ZipFile(file) ) {

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

            return result;

        } catch (IOException e) {

            String msg = String.format( "Could not open zip file: %s (length=%,d bytes)",
                                        file.getAbsolutePath(), file.length() );

            if ( ignoreBrokenJarFiles ) {
                log.warn( msg );
                return result;
            } else {
                throw new IOException( msg, e );
            }

        }

    }

    private Collection<ResourceReference> findResourcesFromDirectory(File directory, Pattern pattern) throws IOException {
        return findResourcesFromDirectory( directory, directory, pattern );
    }

    private Collection<ResourceReference> findResourcesFromDirectory(File root, File directory, Pattern pattern) throws IOException {

        Path rootDirectoryPath = Paths.get( root.getAbsolutePath() );

        List<ResourceReference> result = new ArrayList<>();

        File[] fileList = directory.listFiles();

        if ( fileList == null ) {
            return result;
        }

        for( File file : fileList ) {

            if( file.isDirectory() ) {

                // recurs into the sub directories.
                result.addAll( findResourcesFromDirectory( root, file, pattern));

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
