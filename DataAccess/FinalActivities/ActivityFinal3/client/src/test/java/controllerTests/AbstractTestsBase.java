package controllerTests;

import com.da.dg2526.models.dto.BookDataDto;

abstract class AbstractTestsBase {


    protected final String testUserId = "USER100T";
    protected final String testIsbn = "BOOK1ISBNTEST";

    protected final String testUserIdInvalid = "bookIdTestbookIdTestbookIdTest";
    protected final String testIsbnInvalid = "bookIdTestbookIdTestbookIdTest";

    protected final String title = "titleTest";
    protected final String author = "authorTest";
    protected final String publisher = "publisherTest";
    protected final String description = "descriptionTest";
    protected final String category = "categoryTest";


        /*
    *   <book>
        <isbn>1234567890123</isbn>
        <title>Clean Code</title>
        <copies>3</copies>
        <outline>Programming best practices</outline>
        <publisher>Prentice</publisher>
        <category>LIT</category>
    </book>

    <book>
        <isbn>9876543210123</isbn>
        <title>Roman History</title>
        <copies>1</copies>
        <category>HIST</category>
    </book>
    * */

    protected final BookDataDto testBook1Success = new BookDataDto("1234567890123", "Clean Code", 3, "Programming best practices", "Prentice", "LIT");
    protected final BookDataDto testBook2Success = new BookDataDto("9876543210123", "Roman History", 1, "", "", "HIST");

}
