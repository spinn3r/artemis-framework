package com.spinn3r.artemis.util.text;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.io.Closeables;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Read an InputStream by batches of lines.
 */
public class LinePageReader implements Closeable {

    private final InputStreamReader inputStreamReader;

    private final BufferedReader bufferedReader;

    private final int pageSize;

    public LinePageReader(InputStream inputStream, Charset charset, int pageSize) throws IOException {
        this.inputStreamReader = new InputStreamReader(inputStream, charset);
        this.bufferedReader = new BufferedReader(inputStreamReader);
        this.pageSize = pageSize;
    }

    public LinePageIterator iterator() throws IOException {
        return new DefaultLinePageIterator();
    }

    @Override
    public void close() throws IOException {
        Closeables.close(bufferedReader, inputStreamReader);
    }

    private class DefaultLinePageIterator implements LinePageIterator {

        private ImmutableList<String> current;

        DefaultLinePageIterator() throws IOException {
            current = read();
        }

        @Override
        public boolean hasNext() {
            return current.size() > 0;

        }

        @Override
        public ImmutableList<String> next() throws IOException {

            ImmutableList<String> result = current;

            current = read();

            return result;

        }

        private ImmutableList<String> read() throws IOException {

            List<String> result = Lists.newArrayList();

            for (int i = 0; i < pageSize; i++) {
                String line = bufferedReader.readLine();

                if (line == null)
                    break;

                result.add(line);
            }

            return ImmutableList.copyOf(result);

        }

    }

}

