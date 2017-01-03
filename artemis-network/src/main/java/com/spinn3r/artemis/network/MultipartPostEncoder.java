package com.spinn3r.artemis.network;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class MultipartPostEncoder {

	private static final String LINE_FEED = "\n";
	
	public static final String BOUNDARY = "--------------------------8d0b9818393ce388";
	
	public static String encodeMultipart(Map<String, ?> params) {
		
		final StringBuilder str = new StringBuilder();
		
		params.forEach((name, val) -> {
			Collection<?> values;
			
			if(val instanceof Collection) {
				values = (Collection<?>) val;
			} else {
				values = Collections.singleton(val);
			}
			
			for(Object value : values) {
				str.append("--");
				str.append(BOUNDARY);
				str.append(LINE_FEED);
				str.append("Content-Disposition: form-data; name=\"");
				str.append(name);
				str.append("\"");
				str.append(LINE_FEED);
				str.append("Content-Type: text/plain; charset=UTF-8");
				str.append(LINE_FEED);
				str.append(LINE_FEED);
				str.append(value);
				str.append(LINE_FEED);
			}
		});
		
		if(!params.isEmpty()) {
			str.append("--");
			str.append(BOUNDARY);
			str.append("--");
		}
		
		return str.toString();
	}
	
}
