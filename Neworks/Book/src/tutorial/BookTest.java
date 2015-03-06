/**
 * 
 */
package tutorial;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author hxu
 *
 */


public class BookTest extends TestCase {

	private Book book1;
	private Book book2;
	private Book book3;
	/**
	 * @param arg0
	 */
	public BookTest(String arg0) {
		super(arg0);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	    book1 = new Book("ES", 12.99);
	    book2 = new Book("The Gate", 11.99);
		book3 = new Book("ES", 12.99);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		book1 = null;
	    book2 = null;
		book3 = null;
	}
	

	public void testEquals(){
        assertFalse(book2.equals(book1));
        assertTrue(book1.equals(book1));
	}

	public void testNotEquals(){
        assertFalse(book2.equals(book3));
        assertTrue(book1.equals(book3));
	}

	public static Test suite(){
		return new TestSuite(BookTest.class);
		/*
		TestSuite suite = new TestSuite();
		suite.addTest(new BookTest("testEquals"));
		return suite; */
	}

}
