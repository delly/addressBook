package com.ericsson.eduhang.addressBook;

/**
 * 
 */

import static org.junit.Assert.*;

import java.io.File;
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
import org.junit.AfterClass;
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

    @AfterClass
    public static void doAfterClass() {
        File file = new File("test.addr");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testSave() throws IOException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("bigcow").setPhone("15021659169").setAddress("ericsson");
        personInfos.add(personInfo);
        addrBook.setPersonInfos(personInfos);
        addrBook.setPath("test.addr");

        addrBook.save(true);
    }

    @Test
    public void testLoad() throws IOException, SAXException, ParserConfigurationException {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("bigcow").setPhone("15021659169").setAddress("ericsson");
        personInfos.add(personInfo);
        addrBook.setPersonInfos(personInfos);
        addrBook.setPath("test.addr");

        assertEquals(addrBook.load(), true);
        assertEquals(addrBook.getPersonInfos(), personInfos);
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
        List<PersonInfo> findResult = new ArrayList<PersonInfo>();
        personInfo.setName("bigcow").setPhone("15021659169").setAddress("ericsson");
        personInfos.add(personInfo);
        PersonInfo personInfo1 = new PersonInfo();
        personInfo1.setName("niuniu").setPhone("520").setAddress("Jiaoda");
        personInfos.add(personInfo1);
        findResult.add(personInfo1);
        addrBook.setPersonInfos(personInfos);

        String input = "520";
        assertEquals(addrBook.compFind(input), findResult);
    }

    @Test
    public void testPartFind() throws IOException {
        PersonInfo personInfo = new PersonInfo();
        List<PersonInfo> findResult = new ArrayList<PersonInfo>();
        personInfo.setName("bigcow").setPhone("15021659169").setAddress("ericsson");
        personInfos.add(personInfo);
        findResult.add(personInfo);
        personInfo = new PersonInfo();
        personInfo.setName("niuniu").setPhone("520").setAddress("Jiaoda");
        personInfos.add(personInfo);
        findResult.add(personInfo);
        addrBook.setPersonInfos(personInfos);

        String input = "5";
        assertEquals(addrBook.partFind(input), findResult);
    }

    @Test
    public void testShow() throws IOException {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("bigcow").setPhone("15021659169").setAddress("ericsson");
        personInfos.add(personInfo);

        String result = "Name: bigcow\nPhone: 15021659169\nAddress: ericsson\n";
        assertEquals(addrBook.show(personInfos), result);
    }

    @Test
    public void testShowNull() throws IOException {
        String result = "There's no match person!";
        assertEquals(addrBook.show(null), result);
    }

    @Test
    public void testFileExist() {
        addrBook.setPath("test.addr");
        assertEquals(addrBook.fileExists(), true);
    }

    @Test
    public void testAdd() throws IOException {
        PersonInfo personInfo = new PersonInfo();
        List<PersonInfo> findResult = new ArrayList<PersonInfo>();
        personInfo.setName("niu").setPhone("520").setAddress("Jiaoda");
        findResult.add(personInfo);

        String input = "niu;520;Jiaoda";
        assertEquals(addrBook.add(input), true);
        assertEquals(addrBook.getPersonInfos(), findResult);
    }

    @Test
    public void testAddNeg() throws IOException {
        String input = "niu;222";
        assertEquals(addrBook.add(input), false);
    }

}
