package com.github.cao.awa.apricot.plugin.name;

public record PluginName(String eName, String name) {
    public static PluginName of(String eName, String name) {
        return new PluginName(
                eName,
                name
        );
    }

    /**
     * Returns a string representation of the record.
     * In accordance with the general contract of {@link Object#toString()},
     * the {@code toString} method returns a string that
     * "textually represents" this record. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * <p>
     * In addition to this general contract, record classes must further
     * participate in the invariant that any two records which are
     * {@linkplain Record#equals(Object) equal} must produce equal
     * strings.  This invariant is necessarily relaxed in the rare
     * case where corresponding equal component values might fail
     * to produce equal strings for themselves.
     *
     * @return a string representation of the object.
     *
     * @implSpec The implicitly provided implementation returns a string which
     * contains the name of the record class, the names of components
     * of the record, and string representations of component values,
     * so as to fulfill the contract of this method.
     * The precise format produced by this implicitly provided implementation
     * is subject to change, so the present syntax should not be parsed
     * by applications to recover record component values.
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return this.name;
    }
}
