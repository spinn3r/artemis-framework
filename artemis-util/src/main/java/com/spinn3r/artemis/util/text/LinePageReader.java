package com.spinn3r.artemis.util.text;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.function.IOSupplier;
import com.spinn3r.artemis.util.io.Closeables;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Read an InputStream by batches of lines.
 */
public class LinePageReader {

    private final IOSupplier<InputStream> inputStreamSupplier;

    private final Charset charset;

    private final int pageSize;

    public LinePageReader(Charset charset, int pageSize, IOSupplier<InputStream> inputStreamSupplier) throws IOException {
        this.charset = charset;
        this.inputStreamSupplier = inputStreamSupplier;
        this.pageSize = pageSize;
    }

    public LinePageIterator iterator() throws IOException {
        return new DefaultLinePageIterator();
    }

    private class DefaultLinePageIterator implements LinePageIterator, Closeable {

        private ImmutableList<String> current;

        private final InputStream inputStream;

        private final InputStreamReader inputStreamReader;

        private final BufferedReader bufferedReader;

        DefaultLinePageIterator() throws IOException {
            this.inputStream = inputStreamSupplier.get();
            this.inputStreamReader = new InputStreamReader(inputStream, charset);
            this.bufferedReader = new BufferedReader(inputStreamReader);

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

        @Override
        public void close() throws IOException {
            Closeables.close(bufferedReader, inputStreamReader, inputStream);
        }

    }

}

