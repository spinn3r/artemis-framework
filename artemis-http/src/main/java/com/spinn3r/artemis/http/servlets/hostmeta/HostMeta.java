package com.spinn3r.artemis.http.servlets.hostmeta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class HostMeta {

    private String version;

    private String role;

    private String hostname;

    private String uptime;

    public HostMeta(String version, String role, String hostname, String uptime) {
        this.version = version;
        this.role = role;
        this.hostname = hostname;
        this.uptime = uptime;
    }

    protected Map<String,String> toMap() {

        Map<String,String> map = new LinkedHashMap<>();

        map.put( "version"  , version );
        map.put( "role"     , role );
        map.put( "hostname" , hostname );
        map.put( "uptime" , uptime );

        return map;

    }

    public String toJSON() {

        try {

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.writerWithDefaultPrettyPrinter()
              .writeValueAsString( toMap() );

        } catch (JsonProcessingException e) {
            throw new RuntimeException( e );
        }

    }

}
