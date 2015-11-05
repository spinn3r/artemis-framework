package com.spinn3r.artemis.client.watcher;

import com.spinn3r.artemis.client.json.Content;

import java.io.File;
import java.util.List;

/**
 * The result of a parse of content.
 */
public class Parse {

    private File file;

    private List<Content> content;

    public Parse(File file, List<Content> content) {
        this.file = file;
        this.content = content;
    }

    /**
     * The file used to store this data.  If your policy is DELETE this file
     * will not exist afterwards and reads will fail.
     */
    public File getFile() {
        return file;
    }

    /**
     * All the content returned.
     */
    public List<Content> getContent() {
        return content;
    }

}
