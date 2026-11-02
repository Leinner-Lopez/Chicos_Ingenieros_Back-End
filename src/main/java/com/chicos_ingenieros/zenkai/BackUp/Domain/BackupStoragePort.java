package com.chicos_ingenieros.zenkai.BackUp.Domain;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Port for the physical backup files (where they live is an infrastructure concern).
 */
public interface BackupStoragePort {

    OutputStream openForWrite(String fileName);

    InputStream openForRead(String fileName);

    long size(String fileName);

    String sha256(String fileName);

    void delete(String fileName);
}
