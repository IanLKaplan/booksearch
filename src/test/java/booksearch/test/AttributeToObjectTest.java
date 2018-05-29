/** \file
 * 
 * Apr 25, 2018
 *
 * Copyright Ian Kaplan 2018
 *
 * @author Ian Kaplan, www.bearcave.com, iank@bearcave.com
 */
package booksearch.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import booksearch.dynamodb.DynamoDBUtil;
import booksearch.model.BookInfo;
import booksearch.model.GenreEnum;

/**
 * <h4>
 * AttributeToObjectTest
 * </h4>
 * <p>
 * The AttributeToObject function in DynamoDBUtil uses Java object introspection to 
 * intialize an object with the attribute values that are returned from an DynamoDB
 * query.
 * </p>
 * <p>
 * This code tests the functionality of the AttributeToObject function.
 * </p>
 * <p>
 * May 7, 2018
 * </p>
 * 
 * @author Ian Kaplan, iank@bearcave.com
 */
public class AttributeToObjectTest {
    
    private Map<String, AttributeValue> buildAttributeMap(BookInfo info) {
        Map<String, AttributeValue> attrMap = new HashMap<String, AttributeValue>();
        AttributeValue attr = new AttributeValue();
        attr.setS(info.getTitle());
        attrMap.put("title", attr);
        
        attr = new AttributeValue();
        attr.setS(info.getAuthor());
        attrMap.put("author", attr);
        
        attr = new AttributeValue();
        attr.setS(info.getGenre());
        attrMap.put("genre", attr);
        
        attr = new AttributeValue();
        attr.setS(info.getPublisher());
        attrMap.put("publisher", attr);
        
        attr = new AttributeValue();
        attr.setS(info.getYear());
        attrMap.put("year", attr);
        
        attr = new AttributeValue();
        attr.setS(info.getPrice());
        attrMap.put("price", attr);
        return attrMap;
    }

    @Test
    public void testAttributesToObject() {
        BookInfo testBook = BookTableWriteReadTest.buildBookInfo("Escape to Bonaire",
                "Bonaire Bear",
                GenreEnum.TRAVEL,
                "Bonaire Books",
                "2017",
                "9.99");
        Map<String, AttributeValue> attributeMap = buildAttributeMap(testBook);
        BookInfo testObj = new BookInfo();
        try {
            DynamoDBUtil.attributesToObject(testObj, attributeMap);
            assertEquals(testBook, testObj);
            System.out.println("Test passed");
        } catch (ReflectiveOperationException e) {
            fail("failed building object: " + e.getLocalizedMessage() );
        }
        
    }

}
