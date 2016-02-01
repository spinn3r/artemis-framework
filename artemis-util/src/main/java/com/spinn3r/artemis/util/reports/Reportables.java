package com.spinn3r.artemis.util.reports;

import com.spinn3r.artemis.json.Msg;
import com.spinn3r.artemis.util.text.TextFormatter;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public class Reportables {

    public static String formatReport( Reportable reportable, String _defaultReport ) {

        if ( reportable != null ) {
            return reportable.report();
        }

        return _defaultReport;

    }

    public static String formatReport( Class<?> clazz , Map<String,?> map ) {
        return formatReport( clazz.getSimpleName(), map );
    }

    /**
     * Format a report in the form of a map where the keys are the names
     * of the metadata into a pretty report.  We take into consideration
     * embedded Reportable as well and call report() on them instead of
     * toString.
     */
    @SuppressWarnings( "rawtypes" )
    public static String formatReport( String name , Map<String,?> map ) {

        StringBuilder buff = new StringBuilder();

        buff.append( "\n" );
        buff.append( name );
        buff.append( " {\n" );

        for (Map.Entry<String, ?> entry : map.entrySet()) {

            if ( entry.getValue() instanceof Reportable ) {

                buff.append( String.format( "    %s: \n", entry.getKey() ) );

                Reportable reportable = (Reportable) entry.getValue();
                String report = reportable.report();
                String indentedReport = TextFormatter.indent( report, "        " );

                buff.append( indentedReport );
                buff.append( "\n" );


            } else if ( entry.getValue() instanceof Msg ) {

                    buff.append( String.format( "    %s: \n", entry.getKey() ) );

                    Msg msg = (Msg) entry.getValue();
                    String json = msg.toJSON();
                    String indentedJson = TextFormatter.indent( json, "        " );

                    buff.append( indentedJson );
                    buff.append( "\n" );

            } else if ( entry.getValue() instanceof Collection ) {

                buff.append( String.format( "    %s: [\n", entry.getKey() ) );

                Collection collection = (Collection)entry.getValue();

                for (Object current : collection) {
                    buff.append( formatValue( current ) );
                }

                buff.append( String.format( "    ]\n"  ) );

            } else {
                buff.append( String.format( "    %s: %s\n", entry.getKey(), entry.getValue() ) );
            }

        }

        buff.append( "}\n" );

        return buff.toString();

    }

    private static String formatValue( Object value ) {

        if ( value == null ) {
            return "null";
        } if ( value instanceof Reportable ) {

            Reportable reportable = (Reportable)value;
            String report = reportable.report();
            String indentedReport = TextFormatter.indent( report, "        " );
            return indentedReport;
        } else {
            return value.toString();
        }

    }

}
