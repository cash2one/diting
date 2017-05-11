package com.diting.core;

import java.io.Serializable;

/**
 * Identifiable.
 *
 * @param <T> enum id type.
 */
public interface Identifiable<T> extends Serializable {
    T getId();
}