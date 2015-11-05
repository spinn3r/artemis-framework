package com.spinn3r.artemis.http.servlets;

import com.google.common.collect.Sets;
import com.spinn3r.artemis.util.text.TextStream;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class IndexWriter {

    private Map<String,ServletHolder> servlets;

    private String hostname;

    private String role;

    public IndexWriter(Map<String, ServletHolder> servlets, String hostname, String role) {
        this.servlets = servlets;
        this.hostname = hostname;
        this.role = role;
    }

    public String toHTML() {

        TextStream out = new TextStream();

        out.println( String.format( "<h1>Index of %s (%s)</h1>", hostname, role ) );

        Set<String> keys = Sets.newTreeSet( servlets.keySet() );

        out.println( "<html>" );

        out.println( "<style>" );
        out.println( "* { font-family: Arial, Helvetica, sans-serif }" );
        out.println( "th { text-align: left; }" );
        out.println( ".col-path { min-width: 5em; padding-right: 5px;}" );
        out.println( "</style>" );

        out.println( "<body>" );
        out.println( "<table>" );

        out.println( "<tr>" );
        out.println( "<th>" );
        out.println( "path" );
        out.println( "</th>" );
        out.println( "<th>" );
        out.println( "classname" );
        out.println( "</th>" );

        out.println( "</tr>" );

        for (String key : keys) {

            out.println( "<tr>" );

            out.println( "<td class='col-path'>" );
            out.println( String.format( "<a href='%s'>%s</a><br/>", key, key ) );
            out.println( "</td>" );

            out.println( "<td class='col-classname'>" );

            out.println( String.format( "<a href='%s'>%s</a><br/>", key, servlets.get( key ).getClassName() ) );

            out.println( "</td>" );

            out.println( "</tr>" );

        }

        out.println( "</table>" );
        out.println( "</body>" );
        out.println( "</html>" );

        return out.toString();

    }

}
