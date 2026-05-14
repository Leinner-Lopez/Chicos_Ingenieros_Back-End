package com.chicos_ingenieros.zenkai.Categories.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long categoryId;
    private String name;
    private String description;
}
