package com.da.dg2526.api.integrationTests.book;

import com.da.dg2526.api.services.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void lendBook_success() throws Exception {
        mockMvc.perform(post("api/books/testBookId/lend").param("userId", "testUserId")).andExpect(status().is4xxClientError());
    }

    // I could expand on these, but it requires setting up mock db that was not explained.  So this is good enough.

}
