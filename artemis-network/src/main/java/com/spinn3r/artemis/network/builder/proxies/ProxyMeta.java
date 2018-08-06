package com.spinn3r.artemis.network.builder.proxies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxyMeta implements Comparable<ProxyMeta> {

    private final String host;

    private final int port;

    private final String country;

    private final String provider;

    public ProxyMeta( @JsonProperty("host") String host,
                      @JsonProperty("port") int port,
                      @JsonProperty("country") String country,
                      @JsonProperty("provider") String provider) {
        this.host = host;
        this.port = port;
        this.country = country;
        this.provider = provider;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getCountry() {
        return country;
    }

    public String getProvider() {
        return provider;
    }

    public ProxyReference toProxyReference() {
        return ProxyReferences.create("HTTP", host, port);
    }

    public String format() {
        return String.format("%s:%s", host, port);
    }

    @Override
    public int compareTo(ProxyMeta o) {
        return format().compareTo(o.format());
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		result = prime * result + ((provider == null) ? 0 : provider.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProxyMeta other = (ProxyMeta) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		if (provider == null) {
			if (other.provider != null)
				return false;
		} else if (!provider.equals(other.provider))
			return false;
		return true;
	}

	@Override
    public String toString() {
        return "ProxyMeta{" +
                 "host='" + host + '\'' +
                 ", port=" + port +
                 ", country='" + country + '\'' +
                 ", provider='" + provider + '\'' +
                 '}';
    }

}
