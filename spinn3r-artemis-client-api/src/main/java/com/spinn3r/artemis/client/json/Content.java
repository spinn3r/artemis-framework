package com.spinn3r.artemis.client.json;

import com.spinn3r.artemis.schema.core.Msg;

/**
 * Stores content in our index including the full HTML as well  as the metadata we were able to extract.  Some of these  fields are HTML which are cleaned any unsafe elements which might cause cross scripting attacks or other vulnerabilities are removed.  Additionally, All URLs are fully expanded. Encoding is UTF-8. 
 */
public class Content extends BaseContent implements Msg {

}