package com.diting.error;

import java.lang.*;

/**
 * AppError.
 */
public interface AppError {
    int getHttpStatusCode();

    AppErrorException exception();

    Error error();
}
