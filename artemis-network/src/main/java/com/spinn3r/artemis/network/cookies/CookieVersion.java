package com.spinn3r.artemis.network.cookies;

public enum CookieVersion {
    
    VERSION_0_NETSCAPE, VERSION_1_RFC2965;
    
    public static final CookieVersion DEFAULT = VERSION_1_RFC2965;
 
    public static CookieVersion fromVersion(int version) {
        if(version < 0 || version > 1) {
            return DEFAULT;
        }
        return values()[version];
    }

}
