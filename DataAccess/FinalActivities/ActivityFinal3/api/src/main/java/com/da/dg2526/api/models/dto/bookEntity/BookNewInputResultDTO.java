package com.da.dg2526.api.models.dto.bookEntity;

public record BookNewInputResultDTO(String isbn, String title, Integer copies, String outline, String publisher,
                                    String category) {}
