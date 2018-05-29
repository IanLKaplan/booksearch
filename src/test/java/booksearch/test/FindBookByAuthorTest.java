/** \file
 * 
 * May 3, 2018
 *
 * Copyright Ian Kaplan 2018
 *
 * @author Ian Kaplan, www.bearcave.com, iank@bearcave.com
 */
package booksearch.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
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
 * FindBookByAuthorTest
 * </h4>
 * <p>
 * A unit test for the BookTableService function findBookByAuthor(). This test verifies that a query on the book
 * author returns the expected books.
 * </p>
 * <p>
 * May 7, 2018
 * </p>
 * 
 * @author Ian Kaplan, iank@bearcave.com
 */
public class FindBookByAuthorTest implements IDynamoDBKeys {
    private final static String BOOK_TABLE_NAME = "search_by_author_test_book_table";
    AmazonDynamoDB client = null;
    BookTableService bookService = null;
    
    /**
     * @return a List of books by the author William Gibson
     */
    private static ArrayList<BookInfo> gibsonBooks() {
        ArrayList<BookInfo> bookList = new ArrayList<BookInfo>();
        BookInfo testBook = BookTableWriteReadTest.buildBookInfo("Neuromancer",
                                                                 "William Gibson",
                                                                 GenreEnum.SCIENCE_FICTION,
                                                                 "Ace",
                                                                 "1984",
                                                                 "14.77");
        bookList.add(testBook);
        testBook = BookTableWriteReadTest.buildBookInfo("Count Zero",
                                                        "William Gibson",
                                                        GenreEnum.SCIENCE_FICTION,
                                                        "HarperCollins Publishers",
                                                        "1986",
                                                        "47.50");
        bookList.add(testBook);
        testBook = BookTableWriteReadTest.buildBookInfo("Mona Lisa Overdrive",
                                                        "William Gibson",
                                                        GenreEnum.SCIENCE_FICTION,
                                                        "Bantam Books",
                                                        "1988",
                                                        "14.00");
        return bookList;
    }
    
    /**
     * 
     * @return a List of books for the book database
     */
    public static ArrayList<BookInfo> buildBookList() {
        ArrayList<BookInfo> bookList = new ArrayList<BookInfo>();
        ArrayList<BookInfo> sprawlBooks = gibsonBooks();
        bookList.addAll(sprawlBooks);
        BookInfo testBook = BookTableWriteReadTest.buildBookInfo("Luna: New Moon",
                                                        "Ian McDonald",
                                                        GenreEnum.SCIENCE_FICTION,
                                                        "Tor Books",
                                                        "2015",
                                                        "16.23");
        bookList.add(testBook);
        testBook = BookTableWriteReadTest.buildBookInfo("The Hydrogen Sonata",
                                                        "Iain M. Banks",
                                                        GenreEnum.SCIENCE_FICTION,
                                                        "Orbit Books",
                                                        "2012",
                                                        "17.29");
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
    }

    @After
    public void tearDown() throws Exception {
        if (BookTableWriteReadTest.tableExists(client, BOOK_TABLE_NAME )) {
            client.deleteTable( BOOK_TABLE_NAME );
        }
    }

    @Test
    public void testFindBookByAuthor() {
        // Add entries to the book table
        ArrayList<BookInfo> bookList = buildBookList();
        for (BookInfo book : bookList) {
            bookService.writeToBookTable( book, BOOK_TABLE_NAME );
        }
        ArrayList<BookInfo> sprawlBooks = gibsonBooks();
        List<BookInfo> booksFound = bookService.findBookByAuthor("William Gibson");
        // Now check that the expected books are returned by the query
        assertNotNull("The list of book returned is null", booksFound);
        assertTrue("The size of the book list should be " + sprawlBooks.size() + ", is " + booksFound.size(), 
                   booksFound.size() == sprawlBooks.size());
        HashMap<String, BookInfo> bookHash = new HashMap<String, BookInfo>();
        for (BookInfo book : sprawlBooks) {
            bookHash.put(book.getTitle(), book);
        }
        for (BookInfo book : booksFound) {
            if (! bookHash.containsKey(book.getTitle())) {
                fail("Incorrect book was returned. Title is " + book.getTitle() );
            } else {
                if (! book.equals( bookHash.get( book.getTitle()))) {
                    fail("For book title " + book.getTitle() + " the record does not contain all of the correct data");
                }
            }
        }
        System.out.println("Test passed");
    }

}
