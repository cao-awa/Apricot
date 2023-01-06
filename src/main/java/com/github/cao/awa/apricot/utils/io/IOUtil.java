package com.github.cao.awa.apricot.utils.io;

import com.github.cao.awa.apricot.utils.file.*;
import it.unimi.dsi.fastutil.io.*;

import java.io.*;

/**
 * Utils for I/O.
 *
 * @author cao_awa
 * @since 1.0.0
 */
public class IOUtil {
    /**
     * Write input to output.
     *
     * @param output
     *         Output
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static void write(OutputStream output, InputStream input) throws IOException {
        output.write(input.readAllBytes());
        output.close();
        input.close();
    }

    /**
     * Write bytes to output.
     *
     * @param output
     *         Output
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static void write(OutputStream output, byte[] input) throws IOException {
        output.write(input);
        output.close();
    }

    /**
     * Write string to output.
     *
     * @param output
     *         Output
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static void write(OutputStream output, String input) throws IOException {
        write0(output, input);
        output.close();
    }

    /**
     * Write string to output.
     *
     * @param output
     *         Output
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static void write0(OutputStream output, String input) throws IOException {
        output.write(input.getBytes());
    }

    /**
     * Write string to output.
     *
     * @param writer
     *         Writer
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static void write(Writer writer, String input) throws IOException {
        write0(writer, input);
        writer.close();
    }

    /**
     * Write string to output.
     *
     * @param writer
     *         Writer
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static void write0(Writer writer, String input) throws IOException {
        writer.write(input);
    }

    /**
     * Write string to output.
     *
     * @param writer
     *         Writer
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static void write0(OutputStream writer, byte[] input) throws IOException {
        writer.write(input);
    }

    /**
     * Write information of reader to writer
     *
     * @param writer
     *         Writer
     * @param reader
     *         Reader
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static void write(Writer writer, Reader reader) throws IOException {
        char[] chars = new char[4096];
        int length;
        while ((length = reader.read(chars)) != - 1) {
            writer.write(
                    chars,
                    0,
                    length
            );
        }
        writer.close();
        reader.close();
    }

    /**
     * Write chars to output.
     *
     * @param writer
     *         Writer
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static void write(Writer writer, char[] input) throws IOException {
        writer.write(input);
        writer.close();
    }

    /**
     * Read input.
     *
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static String read(InputStream input) throws IOException {
        return read(new InputStreamReader(input));
    }


    /**
     * Read input.
     *
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static int read0(InputStream input, byte[] bytes) throws IOException {
        return input.read(bytes);
    }

    /**
     * Read input.
     *
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static String read(Reader input) throws IOException {
        char[] chars = new char[4096];
        int length;
        StringBuilder builder = new StringBuilder();
        while ((length = input.read(chars)) != - 1) {
            builder.append(
                    chars,
                    0,
                    length
            );
        }
        input.close();
        return builder.toString();
    }

    /**
     * Read input.
     *
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static String readLine(BufferedReader input) throws IOException {
        return input.readLine();
    }

    /**
     * Read input.
     *
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static byte[] readBytes(InputStream input) throws IOException {
        return input.readAllBytes();
    }

    /**
     * Read input.
     *
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static byte[] readBytes(Reader input) throws IOException {
        char[] chars = new char[4096];
        int length;
        StringBuilder builder = new StringBuilder();
        while ((length = input.read(chars)) != - 1) {
            builder.append(
                    chars,
                    0,
                    length
            );
        }
        input.close();
        return builder.toString().getBytes();
    }

    /**
     * Read input.
     *
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static char[] readChars(InputStream input) throws IOException {
        return readChars(new InputStreamReader(input));
    }

    /**
     * Read input.
     *
     * @param input
     *         Input
     * @throws IOException
     *         Happened IO error
     * @author cao_awa
     * @since 1.0.0
     */
    public static char[] readChars(Reader input) throws IOException {
        char[] chars = new char[4096];
        int length;
        StringBuilder builder = new StringBuilder();
        while ((length = input.read(chars)) != - 1) {
            builder.append(
                    chars,
                    0,
                    length
            );
        }
        input.close();
        return builder.toString().toCharArray();
    }

    public static void copy(String from, String to) throws IOException {
        FileUtil.mkdirsParent(new File(to));
        FastBufferedInputStream input = new FastBufferedInputStream(new FileInputStream(from));
        FastBufferedOutputStream output = new FastBufferedOutputStream(new FileOutputStream(to));
        byte[] buff = new byte[8192];
        int length;
        while ((length = input.read(buff, 0, buff.length)) != - 1) {
            output.write(buff, 0, length);
        }
        input.close();
        output.close();
    }
}
