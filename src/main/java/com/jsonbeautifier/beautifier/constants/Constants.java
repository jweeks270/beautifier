package com.jsonbeautifier.beautifier.constants;

import java.util.List;

public class Constants {
    private Constants() {
        throw new UnsupportedOperationException("This is a constants file and should not be instantiated");
    }

    public static final List<String> GARBAGE_STRINGS = List.of("{", "}", "(", ")", "[", "]");
}
