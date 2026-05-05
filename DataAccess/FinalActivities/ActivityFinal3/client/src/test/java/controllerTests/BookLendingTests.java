package controllerTests;

import com.da.dg2526.cli.Command;
import com.da.dg2526.cli.ParsedCommand;
import com.da.dg2526.controllers.LendBook;
import com.da.dg2526.restapi.HttpResponse;
import com.da.dg2526.restapi.RestApiConnection;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public class BookLendingTests extends AbstractTestsBase{

    @Test
    public void BookLending_success() throws IOException {
        RestApiConnection connection = mock(RestApiConnection.class);

        String endpoint = "/books/" + testIsbn + "/lend?userId=" + testUserId;
        when(connection.post(endpoint, "")).thenReturn(new HttpResponse(200, "Ok", "{\"status\":\"Success\"}"));

        LendBook lend = new LendBook(connection);
        ParsedCommand parsed = new ParsedCommand(Command.LEND, new String[]{testIsbn, testUserId});

        lend.lendBook(parsed);

        verify(connection).post(
                endpoint,
                ""
        );
    }

    @Test
    public void BookLendingIsbnInvalid_failure(){
        RestApiConnection connection = mock(RestApiConnection.class);

        LendBook lend = new LendBook(connection);
        ParsedCommand parsed = new ParsedCommand(Command.LEND, new String[]{testIsbnInvalid, testUserId});

        lend.lendBook(parsed);

        verifyNoInteractions(connection);
    }

    @Test
    public void BookLendingUserIdInvalid_failure(){
        RestApiConnection connection = mock(RestApiConnection.class);

        LendBook lend = new LendBook(connection);
        ParsedCommand parsed = new ParsedCommand(Command.LEND, new String[]{testIsbn, testUserIdInvalid});

        lend.lendBook(parsed);

        verifyNoInteractions(connection);
    }

    @Test
    public void BookLendingIsbnAndUserIdInvalid_failure() {
        RestApiConnection connection = mock(RestApiConnection.class);

        LendBook lend = new LendBook(connection);
        ParsedCommand parsed = new ParsedCommand(Command.LEND, new String[]{testIsbnInvalid, testUserIdInvalid});

        lend.lendBook(parsed);

        verifyNoInteractions(connection);
    }

}
