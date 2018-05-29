/** \file
 * 
 * May 25, 2018
 *
 * Copyright Ian Kaplan 2018
 *
 * @author Ian Kaplan, www.bearcave.com, iank@bearcave.com
 */
package booksearch.test;

import static org.junit.Assert.*;

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
 * <h4>
 * FindBookByTitleTest
 * </h4>
 * <p>
 * Test searching for a book by a complete title or a title sub-string.
 * </p>
 * <p>
 * May 25, 2018
 * </p>
 * 
 * @author Ian Kaplan, iank@bearcave.com
 */
public class FindBookByTitleTest implements IDynamoDBKeys {
    private final static String BOOK_TABLE_NAME = "search_by_title_test_book_table";
    private final static String SEARCH_STRING = "Venice";
    AmazonDynamoDB client = null;
    BookTableService bookService = null;
    
    /**
     * Build a list of book objects with a common string ("Venice") in the title.
     * 
     * @return a list of books with a common word in the title
     */
    private List<BookInfo> similarBookTitles() {
        ArrayList<BookInfo> bookList = new ArrayList<BookInfo>();
        BookInfo testBook = BookTableWriteReadTest.buildBookInfo("Venice The Tourist Maze",
                                                                 "Robert C Davis", 
                                                                 GenreEnum.NONFICTION,
                                                                 "University of California Press",
                                                                 "2004",
                                                                 "15");
        bookList.add(testBook);
        testBook = BookTableWriteReadTest.buildBookInfo("The Architectural History of Venice ",
                                                        "Deborah Howard",
                                                        GenreEnum.NONFICTION,
                                                        "Yale University Press",
                                                        "2002",
                                                        "19.00");
        bookList.add(testBook);
        return bookList;
    }

    @Before
    public void setUp() throws Exception {
        // Create test book table
        DynamoDBService dynamodbService = new DynamoDBService( region, full_dynamodb_access_ID, full_dynamodb_access_KEY); 
        client = dynamodbService.getClient();
        // Allocate a BookTableService object and allocate a DynamoDB book table for testing
        bookService = new BookTableService( BOOK_TABLE_NAME );
        ArrayList<BookInfo> bookList = FindBookByAuthorTest.buildBookList();
        bookList.addAll( similarBookTitles());
        for (BookInfo book : bookList) {
            bookService.writeToBookTable( book, BOOK_TABLE_NAME );
        }
    }

    @After
    public void tearDown() throws Exception {
        if (BookTableWriteReadTest.tableExists(client, BOOK_TABLE_NAME )) {
            client.deleteTable( BOOK_TABLE_NAME );
        }
    }

    @Test
    public void testFindBookByTitle() {
        List<BookInfo> foundBooks = bookService.findBookByTitle(SEARCH_STRING);
        List<BookInfo> similarBooks = similarBookTitles();
        boolean allFound = false;
        if (foundBooks != null && foundBooks.size() == similarBooks.size()) {
            allFound = true;
            for (BookInfo book : foundBooks) {
                if (! similarBooks.contains(book)) {
                    allFound = false;
                    break;
                }
            } // for
            if (allFound) {
                System.out.println("Test passed");
            } else {
                fail("All books were not found");
            }
        } else {
            fail("No book were returned by the search");
        }
    }

}
