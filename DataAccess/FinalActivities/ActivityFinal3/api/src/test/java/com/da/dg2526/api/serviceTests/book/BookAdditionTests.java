package com.da.dg2526.api.serviceTests.book;

import com.da.dg2526.api.exceptions.ServiceValidationException;
import com.da.dg2526.api.models.entities.BookEntity;
import com.da.dg2526.api.models.entities.CategoryEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookAdditionTests extends AbstractBookServiceTestBase {



    @Test
    void addNewBook_success() {
        var category = new CategoryEntity();
        category.setCode("OTHER");
        category.setName("Other");

        when(bookEntityDAO.existsById(testBookDTO.isbn()))
                .thenReturn(false);

        when(categoryEntityDAO.findById(testBookDTO.category()))
                .thenReturn(Optional.of(category));

        var result = bookService.addNewBook(testBookDTO);

        assertEquals("9999999999999", result.isbn());
        assertEquals("test", result.title());
        assertEquals(2, result.copies());
        assertEquals("Other", result.category());

        verify(bookEntityDAO).save(any(BookEntity.class));
    }

    @Test
    void addNewBook_bookCategoryInvalid() {
        when(bookEntityDAO.existsById(testBookDTO.isbn()))
                .thenReturn(false);

        when(categoryEntityDAO.findById(testBookDTO.category())).thenReturn(Optional.empty());

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> bookService.addNewBook(testBookDTO));

        // Error code on category not being valid.
        assertEquals("Category doesn't exist: " + testBookDTO.category(), exception.getMessage());

        // Making sure it did not reach to save it.
        verify(bookEntityDAO, never()).save(any(BookEntity.class));
    }


    @Test
    void addNewBook_bookAlreadyExists() {
        // Check of id existing in db.
        when(bookEntityDAO.existsById(testBookDTO.isbn())).thenReturn(true);

        ServiceValidationException exception = assertThrows(ServiceValidationException.class, () -> bookService.addNewBook(testBookDTO));

        // Error code on book existing.
        assertEquals("Book already exists", exception.getMessage());

        // Making sure it did not save somehow.
        verify(bookEntityDAO, never()).save(any(BookEntity.class));

        // Test that category was not checked, my category test runs after id check.
        verify(categoryEntityDAO, never()).findById(anyString());
    }


}
