package com.chicos_ingenieros.zenkai.BackUp.Domain;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Port for reading and rewriting the database content. The administrator identified by
 * {@code keepUserEmail} is never deleted nor overwritten.
 */
public interface BackupDataPort {

    /** Writes a JSON snapshot of every table to {@code out} and returns the number of rows exported. */
    int exportData(OutputStream out);

    /** Atomically replaces the data with the snapshot read from {@code in}. */
    void replaceData(InputStream in, String keepUserEmail);

    /** Atomically deletes all the data. */
    void purgeData(String keepUserEmail);
}
