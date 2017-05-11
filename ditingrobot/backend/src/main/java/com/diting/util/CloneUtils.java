package com.diting.util;

import com.rits.cloning.Cloner;

public final class CloneUtils {
    // thread safe
    private static Cloner cloner = new Cloner();

    private CloneUtils() {
    }

    public static <T> T clone(T source) {
        if (source == null)
            return null;

        return cloner.deepClone(source);
    }
}
