/** \file
 * 
 * Apr 10, 2018
 *
 * Copyright Ian Kaplan 2018
 *
 * @author Ian Kaplan, www.bearcave.com, iank@bearcave.com
 */
package booksearch.test;

import java.util.logging.Logger;

import org.junit.Test;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

import booksearch.dynamodb.CreateBookTable;
import booksearch.dynamodb.IDynamoDBKeys;
import booksearch.service.DynamoDBService;

/**
 * <h4>
 * CreateBookTableTest
 * </h4>
 * <p>
 * Test creation of the BookTable class. A temporary table is created by this test,
 * and then removed.
 * </p>
 * 
 * Apr 10, 2018
 * 
 * @author Ian Kaplan, iank@bearcave.com
 */
public class CreateBookTableTest implements IDynamoDBKeys {
    private final static String BOOK_TABLE_NAME = "create_book_table_test";
    private final static long DYNAMODB_READ_THROUGHPUT = 4;
    private final static long DYNAMODB_WRITE_THROUGHPUT = 4;
    private final Logger log = Logger.getLogger(this.getClass().getName());
    private DynamoDBService dynamodbService = new DynamoDBService( region, full_dynamodb_access_ID, full_dynamodb_access_KEY); 
    
    void tearDown() {
        log.info("Removing the test table " + BOOK_TABLE_NAME );
        AmazonDynamoDB client = dynamodbService.getClient();
        client.deleteTable( BOOK_TABLE_NAME );
    }

    @Test
    public void test() {

        CreateBookTable bookTable = new CreateBookTable( BOOK_TABLE_NAME );
        AmazonDynamoDB client = dynamodbService.getClient();
        if (! bookTable.tableExists(client)) {
            bookTable.createTable(client, DYNAMODB_READ_THROUGHPUT, DYNAMODB_WRITE_THROUGHPUT);
        }
        if (bookTable.tableExists(client)) {
            log.info("The DynamoDB table " + BOOK_TABLE_NAME + " was successfully created");
        }
        log.info("Test passed");
        System.out.println("Test passed");
    }

}
