/** \file
 * 
 * May 11, 2018
 *
 * Copyright Ian Kaplan 2018
 *
 * @author Ian Kaplan, www.bearcave.com, iank@bearcave.com
 */
package booksearch.controller;

import booksearch.service.BookTableService;

/**
 * <h4>
 * BookControllerBase
 * </h4>
 * <p>
 * This base class provides the BookTableService object to its subclasses. This allows a single BookTableService object
 * to be shared by all subclasses (which makes them all singleton classes).
 * </p>
 * <p>
 * May 22, 2018
 * </p>
 * 
 * @author Ian Kaplan, iank@bearcave.com
 */
abstract class BookControllerBase {
    private final static String BOOK_TABLE_NAME = "book_table";
    private final static BookTableService bookTableService = new BookTableService( BOOK_TABLE_NAME );
    protected static final String BOOK_LIST = "bookList";
    
    protected BookTableService getBookTableService() {
        return bookTableService;
    }
    
}
