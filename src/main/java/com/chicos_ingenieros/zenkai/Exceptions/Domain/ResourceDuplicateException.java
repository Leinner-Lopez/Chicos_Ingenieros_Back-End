package com.chicos_ingenieros.zenkai.Exceptions.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ResourceDuplicateException extends RuntimeException {
    private String message;
}
