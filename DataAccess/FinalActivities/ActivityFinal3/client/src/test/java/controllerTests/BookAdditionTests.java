package controllerTests;


import com.da.dg2526.cli.Command;
import com.da.dg2526.cli.ParsedCommand;
import com.da.dg2526.controllers.AddNewBook;
import com.da.dg2526.controllers.LendBook;
import com.da.dg2526.exceptions.ControllerValidationException;
import com.da.dg2526.restapi.HttpResponse;
import com.da.dg2526.restapi.RestApiConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookAdditionTests extends AbstractTestsBase {

    @Test
    public void BookAdding_success() throws IOException, JSONException {
        RestApiConnection connection = mock(RestApiConnection.class);

        var path = testResourcePath("usersXmlTest.xml");

        String endpoint = "/books/import";
        when(connection.post(eq(endpoint), anyString())).thenReturn(new HttpResponse(200, "Ok", "{\"status\":\"Success\"}"));


        AddNewBook addNewBook = new AddNewBook(connection);
        ParsedCommand parsed = new ParsedCommand(Command.ADD, new String[]{path.toString()});

        addNewBook.addFromXml(parsed);

        // Tests of sent json is actually correct
        var jsonCapture = ArgumentCaptor.forClass(String.class);

        // Apparently this argument matcher must be used, or it fails.
        verify(connection).post(eq(endpoint), jsonCapture.capture());

        var jsonArray = new JSONArray(jsonCapture.getValue());
        assertEquals(2, jsonArray.length());

        var book1 = jsonArray.getJSONObject(0);

        assertEquals(testBook1Success.isbn().toUpperCase(), book1.getString("isbn"));
        assertEquals(testBook1Success.title(), book1.getString("title"));
        assertEquals(testBook1Success.copies(), book1.getInt("copies"));
        assertEquals(testBook1Success.category(), book1.getString("category"));

        var book2 = jsonArray.getJSONObject(1);

        assertEquals(testBook2Success.isbn().toUpperCase(), book2.getString("isbn"));
        assertEquals(testBook2Success.title(), book2.getString("title"));
        assertEquals(testBook2Success.copies(), book2.getInt("copies"));
        assertEquals(testBook2Success.category(), book2.getString("category"));

    }


    @Test
    public void BookAddingDataErrors_failure() throws IOException {
        RestApiConnection connection = mock(RestApiConnection.class);

        var path = testResourcePath("usersXmlTestFail.xml");

        AddNewBook addNewBook = new AddNewBook(connection);
        ParsedCommand parsed = new ParsedCommand(Command.ADD, new String[]{path.toString()});

        addNewBook.addFromXml(parsed);


        verifyNoInteractions(connection);
    }

    @Test
    public void BookAddingPathErrors_failure() {
        RestApiConnection connection = mock(RestApiConnection.class);

        var path = "///////totallynotapath.xml";

        AddNewBook addNewBook = new AddNewBook(connection);
        ParsedCommand parsed = new ParsedCommand(Command.ADD, new String[]{path});

        addNewBook.addFromXml(parsed);
        // Path error does not throw any exceptions.

        verifyNoInteractions(connection);
    }

    private Path testResourcePath(String fileName) {
        try {
            // Finds the file in resources
            var resource = getClass().getClassLoader().getResource(fileName);

            if (resource == null) {
                throw new IllegalArgumentException("Test resource not found: " + fileName);
            }
            return Paths.get(resource.toURI());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test resource: " + fileName, e);
        }
    }

}
