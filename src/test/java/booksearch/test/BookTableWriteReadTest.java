/** \file
 * 
 * Apr 11, 2018
 *
 * Copyright Ian Kaplan 2018
 *
 * @author Ian Kaplan, www.bearcave.com, iank@bearcave.com
 */
package booksearch.test;

import static org.junit.Assert.fail;

import java.util.logging.Logger;

import org.junit.After;
import org.junit.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

import booksearch.dynamodb.IDynamoDBKeys;
import booksearch.model.BookInfo;
import booksearch.model.GenreEnum;
import booksearch.service.BookTableService;
import booksearch.service.DynamoDBService;

/**
 * <h4>
 * BookTableWriteReadTest
 * </h4>
 * <p>
 * Test writes and reads to the DynamoDB book info table.
 * </p>
 * May 7, 2018
 * 
 * @author Ian Kaplan, iank@bearcave.com
 */
public class BookTableWriteReadTest implements IDynamoDBKeys {
    private final static String BOOK_TABLE_NAME = "test_book_table";

    public static BookInfo buildBookInfo( String title, 
                                    String author, 
                                    GenreEnum genre, 
                                    String publisher, 
                                    String year, 
                                    String price ) {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setGenre(genre.getName());
        bookInfo.setPublisher(publisher);
        bookInfo.setYear(year);
        bookInfo.setPrice(price);
        return bookInfo;
    }
    
    private final Logger log = Logger.getLogger( this.getClass().getName() ); 
    
    private static BookInfo testBook = buildBookInfo("Escape to Bonaire",
                                                     "Bonaire Bear",
                                                     GenreEnum.TRAVEL,
                                                     "Bonaire Books",
                                                     "2017",
                                                     "9.99");
    
    public static boolean tableExists(AmazonDynamoDB dynamoDBClient, final String tableName ) {
        boolean exists = true;
        final int timeout = 60 * 1000; // 60 seconds x 1000 msec/second
        final int pollInterval = 250; // polling interval in milliseconds (e.g., 1/4 second)
        try {
            TableUtils.waitUntilExists(dynamoDBClient, tableName, timeout, pollInterval);
        } catch (AmazonClientException e) {
            exists = false;
        }
        catch (InterruptedException e) {
            exists = false;
        }
        return exists;
    }
    
    /**
     * Remote the temporary table used for testing.
     * 
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        DynamoDBService dynamodbService = new DynamoDBService( region, full_dynamodb_access_ID, full_dynamodb_access_KEY); 
        AmazonDynamoDB client = dynamodbService.getClient();
        if (tableExists(client, BOOK_TABLE_NAME )) {
            client.deleteTable( BOOK_TABLE_NAME );
        }

    }
    
    @Test
    public void test() {
        DynamoDBService dynamodbService = new DynamoDBService( region, full_dynamodb_access_ID, full_dynamodb_access_KEY); 
        AmazonDynamoDB client = dynamodbService.getClient();
        BookTableService bookService = new BookTableService( BOOK_TABLE_NAME );
        if (tableExists(client, BOOK_TABLE_NAME)) {
            log.info("Table created");
            log.info("Writing to table...");
            bookService.writeToBookTable( testBook, BOOK_TABLE_NAME );
            log.info("Search for the BookInfo entry that was just written");
            if (bookService.hasBookEntry(testBook, BOOK_TABLE_NAME)) {
                log.info("Found BookInfo entry. Test passed");
            } else {
                fail("Could not find BookInfo entry in the table" );
            }
        } else {
            fail("The " + BOOK_TABLE_NAME + " was not created");
        }
    }

}
