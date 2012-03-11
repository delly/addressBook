package com.ericsson.eduhang.addressBook;

/**
 * 
 */


import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author eduhang
 * 
 */
public class AddressBookTest {
	List<PersonInfo> personInfos = null;
	AddressBook addrBook = null;

	protected final Mockery context = new JUnit4Mockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	public Action ChangeBytes(final byte[] result) {
		return new ChangeBytesActionTest(result);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		addrBook = new AddressBook();
		personInfos = new ArrayList<PersonInfo>();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// set system.in to be "quit"
	}

	@Test
	public void testSave() throws IOException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName("bigcow").setPhone("15021659169").setAddress(
				"ericsson");
		personInfos.add(personInfo);
		addrBook.setPersonInfos(personInfos);
		addrBook.setPath("test.addr");

		final InputStream origin = System.in;
		final InputStream mockStream = context.mock(InputStream.class);
		System.setIn(mockStream);
		context.checking(new Expectations() {
			{
				final byte[] b = new byte[8192];
				oneOf(mockStream).read(b, 0, 8192);

				final byte[] result = new byte[8192];
				result[0] = 'y';
				result[1] = '\n';
				will(ChangeBytes(result));
			}
		});

		assertEquals(addrBook.save(), true);
		System.setIn(origin);
	}
	
	@Test
	public void testSaveFailuer() throws IOException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName("bigcow").setPhone("15021659169").setAddress(
				"ericsson");
		personInfos.add(personInfo);
		addrBook.setPersonInfos(personInfos);
		addrBook.setPath("test.addr");

		final InputStream origin = System.in;
		final InputStream mockStream = context.mock(InputStream.class);
		System.setIn(mockStream);
		context.checking(new Expectations() {
			{
				final byte[] b = new byte[8192];
				oneOf(mockStream).read(b, 0, 8192);

				final byte[] result = new byte[8192];
				result[0] = 'n';
				result[1] = '\n';
				will(ChangeBytes(result));
			}
		});

		assertEquals(addrBook.save(), false);
		System.setIn(origin);
	}
	
	@Test
	public void testLoad() throws IOException, SAXException, ParserConfigurationException {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName("bigcow").setPhone("15021659169").setAddress(
				"ericsson");
		personInfos.add(personInfo);
		addrBook.setPersonInfos(personInfos);
		addrBook.setPath("test.addr");

		
		assertEquals(addrBook.load(), true);
		assertEquals(addrBook.getPersonInfos(),personInfos);
	}
	
	@Test
	public void testLoadFailure() throws IOException, SAXException, ParserConfigurationException {
		addrBook.setPath("fail.addr");
		assertEquals(addrBook.load(), false);
	}
	
	@Test
	public void testDoIO() throws IOException {

		final InputStream origin = System.in;
		final InputStream mockStream = context.mock(InputStream.class);
		System.setIn(mockStream);
		context.checking(new Expectations() {
			{
				final byte[] b = new byte[8192];
				oneOf(mockStream).read(b, 0, 8192);

				final byte[] result = new byte[8192];
				result[0] = 'y';
				result[1] = '\n';
				will(ChangeBytes(result));
			}
		});
		String strResult = "y";

		assertEquals(addrBook.doIO(), strResult);
		System.setIn(origin);
	}
	
	@Test
	public void testCompFind() throws IOException {
		PersonInfo personInfo = new PersonInfo();
		List<PersonInfo> findResult = new ArrayList<PersonInfo> ();
		personInfo.setName("bigcow").setPhone("15021659169").setAddress(
				"ericsson");
		personInfos.add(personInfo);
		PersonInfo personInfo1 = new PersonInfo();
		personInfo1.setName("niuniu").setPhone("520").setAddress("Jiaoda");
		personInfos.add(personInfo1);
		findResult.add(personInfo1);
		addrBook.setPersonInfos(personInfos);
		
		final InputStream origin = System.in;
		final InputStream mockStream = context.mock(InputStream.class);
		System.setIn(mockStream);
		context.checking(new Expectations() {
			{
				final byte[] b = new byte[8192];
				oneOf(mockStream).read(b, 0, 8192);

				final byte[] result = new byte[8192];
				result[0] = '5';
				result[1] = '2';
				result[2] = '0';
				result[3] = '\n';
				will(ChangeBytes(result));
			}
		});

		assertEquals(addrBook.compFind(), findResult);
		//addrBook.sendMessage("find -comp");
		System.setIn(origin);
	}
	
	@Test
	public void testCompFindSendMessage() throws IOException, ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException {
		PersonInfo personInfo = new PersonInfo();
		List<PersonInfo> findResult = new ArrayList<PersonInfo> ();
		personInfo.setName("bigcow").setPhone("15021659169").setAddress(
				"ericsson");
		personInfos.add(personInfo);
		PersonInfo personInfo1 = new PersonInfo();
		personInfo1.setName("niuniu").setPhone("520").setAddress("Jiaoda");
		personInfos.add(personInfo1);
		findResult.add(personInfo1);
		addrBook.setPersonInfos(personInfos);
		
		final InputStream origin = System.in;
		final InputStream mockStream = context.mock(InputStream.class);
		System.setIn(mockStream);
		context.checking(new Expectations() {
			{
				final byte[] b = new byte[8192];
				oneOf(mockStream).read(b, 0, 8192);

				final byte[] result = new byte[8192];
				result[0] = '5';
				result[1] = '2';
				result[2] = '0';
				result[3] = '\n';
				will(ChangeBytes(result));
			}
		});

		addrBook.sendMessage("find -comp");
		System.setIn(origin);
	}
	
	
	
	@Test
	public void testPartFind() throws IOException {
		PersonInfo personInfo = new PersonInfo();
		List<PersonInfo> findResult = new ArrayList<PersonInfo> ();
		personInfo.setName("bigcow").setPhone("15021659169").setAddress(
				"ericsson");
		personInfos.add(personInfo);
		findResult.add(personInfo);
		personInfo = new PersonInfo();
		personInfo.setName("niuniu").setPhone("520").setAddress("Jiaoda");
		personInfos.add(personInfo);
		findResult.add(personInfo);
		addrBook.setPersonInfos(personInfos);

		final InputStream origin = System.in;
		final InputStream mockStream = context.mock(InputStream.class);
		System.setIn(mockStream);
		context.checking(new Expectations() {
			{
				final byte[] b = new byte[8192];
				oneOf(mockStream).read(b, 0, 8192);

				final byte[] result = new byte[8192];
				result[0] = '5';
				result[1] = '\n';
				will(ChangeBytes(result));
			}
		});

		assertEquals(addrBook.partFind(), findResult);
		System.setIn(origin);
	}
	
	@Test
	public void testPartFindSendMessage() throws IOException, ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException {
		PersonInfo personInfo = new PersonInfo();
		List<PersonInfo> findResult = new ArrayList<PersonInfo> ();
		personInfo.setName("bigcow").setPhone("15021659169").setAddress(
				"ericsson");
		personInfos.add(personInfo);
		findResult.add(personInfo);
		personInfo = new PersonInfo();
		personInfo.setName("niuniu").setPhone("520").setAddress("Jiaoda");
		personInfos.add(personInfo);
		findResult.add(personInfo);
		addrBook.setPersonInfos(personInfos);

		final InputStream origin = System.in;
		final InputStream mockStream = context.mock(InputStream.class);
		System.setIn(mockStream);
		context.checking(new Expectations() {
			{
				final byte[] b = new byte[8192];
				oneOf(mockStream).read(b, 0, 8192);

				final byte[] result = new byte[8192];
				result[0] = '5';
				result[1] = '\n';
				will(ChangeBytes(result));
			}
		});

		addrBook.sendMessage("find -part");
		System.setIn(origin);
	}
	
	@Test
	public void testShow() throws IOException {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName("bigcow").setPhone("15021659169").setAddress(
				"ericsson");
		personInfos.add(personInfo);
		addrBook.setPersonInfos(personInfos);

		addrBook.show(personInfos);
	}
	
	@Test
	public void testShowNull() throws IOException, ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException {
		addrBook.sendMessage("show");
	}
	
	@Test
	public void testAdd() throws IOException {
		PersonInfo personInfo = new PersonInfo();
		List<PersonInfo> findResult = new ArrayList<PersonInfo> ();
		personInfo.setName("niu").setPhone("520").setAddress("Jiaoda");
		findResult.add(personInfo);

		final InputStream origin = System.in;
		final InputStream mockStream = context.mock(InputStream.class);
		System.setIn(mockStream);
		context.checking(new Expectations() {
			{
				final byte[] b = new byte[8192];
				oneOf(mockStream).read(b, 0, 8192);

				final byte[] result = new byte[8192];
				result[0] = 'n';
				result[1] = 'i';
				result[2] = 'u';
				result[3] = ';';
				result[4] = '5';
				result[5] = '2';
				result[6] = '0';
				result[7] = ';';
				result[8] = 'J';
				result[9] = 'i';
				result[10] = 'a';
				result[11] = 'o';
				result[12] = 'd';
				result[13] = 'a';
				result[14] = '\n';
				will(ChangeBytes(result));
			}
		});

		addrBook.add();
		assertEquals(addrBook.getPersonInfos(), findResult);
		System.setIn(origin);
	}
	
	@Test
	public void testAddSendMessage() throws IOException, ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException {
		PersonInfo personInfo = new PersonInfo();
		List<PersonInfo> findResult = new ArrayList<PersonInfo> ();
		personInfo.setName("niu").setPhone("520").setAddress("Jiaoda");
		findResult.add(personInfo);

		final InputStream origin = System.in;
		final InputStream mockStream = context.mock(InputStream.class);
		System.setIn(mockStream);
		context.checking(new Expectations() {
			{
				final byte[] b = new byte[8192];
				oneOf(mockStream).read(b, 0, 8192);

				final byte[] result = new byte[8192];
				result[0] = 'n';
				result[1] = 'i';
				result[2] = 'u';
				result[3] = ';';
				result[4] = '5';
				result[5] = '2';
				result[6] = '0';
				result[7] = ';';
				result[8] = 'J';
				result[9] = 'i';
				result[10] = 'a';
				result[11] = 'o';
				result[12] = 'd';
				result[13] = 'a';
				result[14] = '\n';
				will(ChangeBytes(result));
			}
		});

		addrBook.sendMessage("add");
		assertEquals(addrBook.getPersonInfos(), findResult);
		System.setIn(origin);
	}
	
}
