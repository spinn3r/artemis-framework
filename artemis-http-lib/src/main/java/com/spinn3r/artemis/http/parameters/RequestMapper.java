package com.spinn3r.artemis.http.parameters;

import com.google.common.collect.Maps;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 */
public class RequestMapper {

    /**
     * Map the parameters from this HTTP request to he given object.
     */
    public static <T> T map( HttpServletRequest request, Class<T> clazz ) {
        return mapFromMultipleValues( request.getParameterMap(), clazz );

    }

    /**
     * Take the given parameter map, and convert it to a class instance via
     * reflection.
     *
     * @param parameters
     * @param clazz
     * @param <T>
     */
    protected static <T> T mapFromMultipleValues( Map<String,String[]> parameters, Class<T> clazz ) {

        Map<String,String> map = Maps.newLinkedHashMap();

        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {

            if ( entry.getValue().length != 1 ) {
                throw new IllegalArgumentException( "Map must have at most one entry per value: " + parameters.toString() );
            }

            map.put( entry.getKey(), entry.getValue()[0] );

        }

        return map( map, clazz );

    }

    /**
     * Perform a basic dictionary mapping from key to value and return a POJO.
     */
    public static <T> T map( Map<String,String> map , Class<T> clazz ) {

        ModelMapper modelMapper = new ModelMapper();

        return modelMapper.map( map, clazz );

    }

}
