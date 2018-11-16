package org.seuksa.frmk.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public final class StreamUtils {

    private StreamUtils() {
    }

    /**
     * @param file
     * @return
     * @throws Exception
     */
    public static byte[] fileToByteArraay(final File file) throws Exception {
        FileInputStream stream = new FileInputStream(file);
        final byte[] byteDataBig = new byte[(int) file.length()];
        stream.read(byteDataBig, 0, (int) file.length());
        stream.close();
        return byteDataBig;
    }

    /**
     * Read input stream and write into new String value.
     * @param is
     * @return
     * @throws IOException
     */
    public static String streamToString(final InputStream is) throws IOException {
        if (is != null) {
            final Writer writer = new StringWriter();
            final char[] buffer = new char[1024];
            try {
                final Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            }
            finally {
                is.close();
            }
            return writer.toString();
        }
        else {
            return "";
        }
    }
}
