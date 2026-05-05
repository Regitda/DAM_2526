package com.da.dg2526.models.dto;

public record BookDataDto(String isbn,

                          String title,

                          Integer copies,

                          String outline,

                          String publisher,

                          String category

) {
}
