package com.github.cao.awa.apricot.utils.text;

import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * Processing string.
 *
 * @author cao_awa
 * @since 1.0.0
 */
public class TextUtil {
    /**
     * Do partition for abiding size. <br>
     *
     * @param str
     *         String
     * @param size
     *         Partition size
     * @return Partitioned strings
     *
     * @see #partitionToList(String, int, boolean)
     */
    @NotNull
    public static List<String> partitionToList(@NotNull String str, int size) {
        return partitionToList(
                str,
                size,
                false
        );
    }

    /**
     * Do partition for abiding size. <br>
     * <br>
     *
     * <pre>
     * Sample1:
     *      Process string "AAABBBCCCDDD" as size 3.
     *      Result will be ["AAA", "BBB", "CCC", "DDD"].
     * </pre>
     *
     * <pre>
     * Sample2:
     *      Process string "AAABBBPPPP" as size 3.
     *      If save unnecessary data
     *      Result will be ["AAA", "BBB", "PPP", "P"].
     *      Else then will be ["AAA", "BBB", "PPP"].
     * </pre>
     *
     * @param str
     *         String
     * @param size
     *         Partition size
     * @param saveUnnecessary
     *         Save unnecessary or not
     * @return Partitioned strings
     *
     * @author cao_awa
     * @since 1.0.0
     */
    @NotNull
    public static List<String> partitionToList(@NotNull String str, int size, boolean saveUnnecessary) {
        int capacity = str.length() / size + 1;
        List<String> result = new ObjectArrayList<>(capacity);
        int cursor = 0;
        for (int i = 0; i < capacity; i++) {
            int nextCursor = cursor + size;
            if (str.length() > cursor) {
                if (str.length() > nextCursor) {
                    result.add(str.substring(
                            cursor,
                            nextCursor
                    ));
                } else if (saveUnnecessary) {
                    result.add(str.substring(cursor));
                    break;
                }
            }
            cursor = nextCursor;
        }
        return result;
    }

    /**
     * Do partition for abiding size.
     *
     * @param str
     *         String
     * @param size
     *         Partition size
     * @return Partitioned strings
     *
     * @author cao_awa
     * @see #partitionToArray(String, int, boolean)
     * @since 1.0.0
     */
    @NotNull
    public static String[] partitionToArray(@NotNull String str, int size) {
        return partitionToArray(
                str,
                size,
                false
        );
    }

    /**
     * Do partition for abiding size.
     *
     * @param str
     *         String
     * @param size
     *         Partition size
     * @return Partitioned strings
     *
     * @author cao_awa
     * @see #partitionToList(String, int, boolean)
     * @since 1.0.0
     */
    @NotNull
    public static String[] partitionToArray(@NotNull String str, int size, boolean saveUnnecessary) {
        int capacity = str.length() / size + 1;
        String[] strings = new String[capacity];
        int cursor = 0;
        for (int i = 0; i < capacity; i++) {
            int nextCursor = cursor + size;
            if (str.length() > cursor) {
                if (str.length() > nextCursor) {
                    strings[i] = str.substring(
                            cursor,
                            nextCursor
                    );
                } else if (saveUnnecessary) {
                    strings[i] = str.substring(cursor);
                    break;
                } else {
                    strings[i] = str.substring(cursor);
                    String[] processNull = new String[capacity - 1];
                    System.arraycopy(
                            strings,
                            0,
                            processNull,
                            0,
                            processNull.length
                    );
                    return processNull;
                }
            }
            cursor = nextCursor;
        }
        return strings;
    }

    @NotNull
    public static List<String> equivalentRepeated(@NotNull String str) {
        List<String> result = new ObjectArrayList<>();
        StringBuilder builder = new StringBuilder();
        char last = str.charAt(0);
        boolean started = false;
        for (char value : str.toCharArray()) {
            if (started) {
                if (Objects.equals(
                        value,
                        last
                )) {
                    builder.append(value);
                } else {
                    builder.append(last);
                    result.add(builder.toString());
                    builder = new StringBuilder();
                }
                last = value;
            } else {
                started = true;
            }
        }
        builder.append(last);
        result.add(builder.toString());
        return result;
    }

    @NotNull
    public static List<String> splitToList(@NotNull String source, char delim) {
        final String[] strings = new String[source.length() / 2];
        final int length = source.length();
        final StringBuilder builder = new StringBuilder();
        int counts = 0;

        for (int i = 0; i < length; i++) {
            char c = source.charAt(i);
            if (c == delim) {
                strings[counts] = builder.toString();
                builder.setLength(0);
                counts++;
            } else {
                builder.append(c);
            }
        }

        if (builder.length() > 0) {
            strings[counts] = builder.toString();
            counts++;
        }

        final String[] list = new String[counts];

        System.arraycopy(
                strings,
                0,
                list,
                0,
                list.length
        );

        return List.of(list);
    }
}
