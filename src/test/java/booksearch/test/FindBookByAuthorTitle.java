/** \file
 * 
 * May 16, 2018
 *
 * Copyright Ian Kaplan 2018
 *
 * @author Ian Kaplan, www.bearcave.com, iank@bearcave.com
 */
package booksearch.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import booksearch.dynamodb.IDynamoDBKeys;
import booksearch.model.BookInfo;
import booksearch.model.GenreEnum;
import booksearch.service.BookTableService;
import booksearch.service.DynamoDBService;

/**
 * Test for book search by author and title
 * 
 * FindBookByAuthorTitle
 * May 16, 2018
 * 
 * @author Ian Kaplan, iank@bearcave.com
 */
public class FindBookByAuthorTitle implements IDynamoDBKeys {
    private final static String BOOK_TABLE_NAME = "search_by_author_title_test_book_table";
    AmazonDynamoDB client = null;
    BookTableService bookService = null;

    @Before
    public void setUp() throws Exception {
        // Create test book table
        DynamoDBService dynamodbService = new DynamoDBService( region, full_dynamodb_access_ID, full_dynamodb_access_KEY); 
        client = dynamodbService.getClient();
        // Allocate a BookTableService object and allocate a DynamoDB book table for testing
        bookService = new BookTableService( BOOK_TABLE_NAME );
    }

    @After
    public void tearDown() throws Exception {
        if (BookTableWriteReadTest.tableExists(client, BOOK_TABLE_NAME )) {
            client.deleteTable( BOOK_TABLE_NAME );
        }
    }

    @Test
    public void testSearchByTitleAuthor() {
        // Add entries to the book table
        ArrayList<BookInfo> bookList = FindBookByAuthorTest.buildBookList();
        for (BookInfo book : bookList) {
            bookService.writeToBookTable( book, BOOK_TABLE_NAME );
        }
        BookTableService bookService = new BookTableService( BOOK_TABLE_NAME );
        BookInfo testBook = BookTableWriteReadTest.buildBookInfo("Neuromancer",
                "William Gibson",
                GenreEnum.SCIENCE_FICTION,
                "Ace",
                "1984",
                "14.77");
        List<BookInfo> searchRslt = bookService.findBookByTitleAuthor(testBook.getAuthor(), testBook.getTitle());
        if (searchRslt != null && searchRslt.size() > 0) {
            if (searchRslt.size() == 1) {
                BookInfo rsltObj = searchRslt.get(0);
                if (testBook.equals(rsltObj)) {
                    System.out.println("Test passed");
                } else {
                    fail("Search did not return the correct BookInfo: " + rsltObj.toString() );
                }
            } else {
                fail("Search returned more than one book. Size = " + searchRslt.size() );
            }
        } else {
            fail("Search did not return a result");
        }
    }

}
