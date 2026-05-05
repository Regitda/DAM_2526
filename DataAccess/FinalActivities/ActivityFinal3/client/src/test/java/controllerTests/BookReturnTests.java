package controllerTests;

import com.da.dg2526.cli.Command;
import com.da.dg2526.cli.ParsedCommand;
import com.da.dg2526.controllers.ReturnBook;
import com.da.dg2526.restapi.HttpResponse;
import com.da.dg2526.restapi.RestApiConnection;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class BookReturnTests extends AbstractTestsBase{
    @Test
    public void BookReturn_success() throws IOException {
        RestApiConnection connection = mock(RestApiConnection.class);

        String endpoint = "/books/" + testIsbn + "/return?userId=" + testUserId;
        when(connection.post(endpoint, "")).thenReturn(new HttpResponse(200, "Ok", "{\"status\":\"Success\"}"));

        ReturnBook returnBook = new ReturnBook(connection);
        ParsedCommand parsed = new ParsedCommand(Command.RETURN, new String[]{testIsbn, testUserId});

        returnBook.returnBook(parsed);

        verify(connection).post(
                endpoint,
                ""
        );
    }

    @Test
    public void BookReturnIsbnInvalid_failure(){
        RestApiConnection connection = mock(RestApiConnection.class);

        ReturnBook returnBook = new ReturnBook(connection);
        ParsedCommand parsed = new ParsedCommand(Command.RETURN, new String[]{testIsbnInvalid, testUserId});

        returnBook.returnBook(parsed);

        verifyNoInteractions(connection);
    }

    @Test
    public void BookReturnUserIdInvalid_failure(){
        RestApiConnection connection = mock(RestApiConnection.class);

        ReturnBook returnBook = new ReturnBook(connection);
        ParsedCommand parsed = new ParsedCommand(Command.RETURN, new String[]{testIsbn, testUserIdInvalid});

        returnBook.returnBook(parsed);

        verifyNoInteractions(connection);
    }

    @Test
    public void BookReturnIsbnAndUserIdInvalid_failure() {
        RestApiConnection connection = mock(RestApiConnection.class);

        ReturnBook returnBook = new ReturnBook(connection);
        ParsedCommand parsed = new ParsedCommand(Command.RETURN, new String[]{testIsbnInvalid, testUserIdInvalid});

        returnBook.returnBook(parsed);

        verifyNoInteractions(connection);
    }
}
