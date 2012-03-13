package com.ericsson.eduhang.addressBook;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author eduhang
 * 
 */
public class AddressBook {
    private static final Logger logger = LoggerFactory.getLogger(AddressBook.class);

    private List<PersonInfo> personInfos;
    private String path;
    private boolean modify;

    /**
     * constructor
     */
    public AddressBook() {
        super();
        personInfos = new ArrayList<PersonInfo>();
        path = "";
        modify = false;
    }

    /**
     * getPersonInfos
     * 
     * @return personInfos List,all the information stored in the address book
     */
    public List<PersonInfo> getPersonInfos() {
        return personInfos;
    }

    /**
     * setPersonInfos
     * 
     * @param personInfos
     *            List,all the information stored in the address book
     */
    public void setPersonInfos(List<PersonInfo> personInfos) {
        this.personInfos = personInfos;
    }

    /**
     * getPath
     * 
     * @return path String,the file path of the address book
     */
    public String getPath() {
        return path;
    }

    /**
     * setPath
     * 
     * @param path
     *            String,the file path of the address book
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * doIO interaction with customer
     * 
     * @return str String,the input of the customer
     * @throws IOException
     */
    public String doIO() throws IOException {
        logger.info("entering doIO...");
        String str;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        str = input.readLine();
        logger.debug(str);
        logger.info("leaving doIO...");
        return str;
    }

    /**
     * 
     * @param input
     *            String,the information to add,format like "name;phone;address"
     * @return addSuccess Boolean,whether the add performance is suecess or not
     * @throws IOException
     */
    public boolean add(String input) throws IOException {
        logger.info("entering add...");
        boolean addSuccess = false;
        PersonInfo personInfo = new PersonInfo();
        String[] content = input.split(";");
        if (content.length == 3) {
            personInfo.setName(content[0]);
            personInfo.setPhone(content[1]);
            personInfo.setAddress(content[2]);
            personInfos.add(personInfo);
            addSuccess = true;
        }
        logger.info("leaving add...");
        return addSuccess;
    }

    /**
     * compFind find the person who's phone is completely matched with the phone you input
     * 
     * @param phone
     *            String,the phone number of the person you want to find
     * @return resultList List,all the person who's phone number is matched with the phone you input
     * @throws IOException
     */
    public List<PersonInfo> compFind(String phone) throws IOException {
        logger.info("entering compFind...");
        ArrayList<PersonInfo> resultList = null;
        for (PersonInfo personInfo : personInfos) {
            if (personInfo.getPhone().equals(phone)) {
                if (resultList == null) {
                    resultList = new ArrayList<PersonInfo>();
                }
                resultList.add(personInfo);
            }
        }
        logger.debug(resultList.toString());
        logger.info("leaving compFind...");
        return resultList;
    }

    /**
     * partFind find the person who's phone is partly matched with the phone you input
     * 
     * @param phone
     *            String,the phone number of the person you want to find
     * @return resultList List,all the person who's phone number is matched with the phone you input
     * @throws IOException
     */
    public List<PersonInfo> partFind(String phone) throws IOException {
        logger.info("entering partFind...");
        ArrayList<PersonInfo> resultList = null;
        for (PersonInfo personInfo : personInfos) {
            if (personInfo.getPhone().indexOf(phone) != -1) {
                if (resultList == null) {
                    resultList = new ArrayList<PersonInfo>();
                }
                resultList.add(personInfo);
            }
        }
        logger.debug(resultList.toString());
        logger.info("leaving partFind...");
        return resultList;
    }

    /**
     * show show person information
     * 
     * @param personList
     *            List<PersonInfo>,the list of person you want to show
     * @return String,the person information
     */
    public String show(List<PersonInfo> personList) {
        logger.info("entering show...");
        StringBuilder builder = new StringBuilder();
        if (personList != null) {
            for (PersonInfo personInfo : personList) {
                builder.append("Name: ").append(personInfo.getName()).append("\n");
                builder.append("Phone: ").append(personInfo.getPhone()).append("\n");
                builder.append("Address: ").append(personInfo.getAddress()).append("\n");
            }
        } else {
            builder.append("There's no match person!");
        }
        logger.debug(builder.toString());
        logger.info("leaving show...");
        return builder.toString();
    }

    /**
     * fileExists
     * 
     * @return boolean,if addr file exits and it's not in modify mode, return true
     */
    public boolean fileExists() {
        boolean isExist = false;
        File file = new File(path);
        if (file.exists() && modify == false) {
            isExist = true;
        }
        return isExist;
    }

    /**
     * save save the information into a addr file
     * 
     * @param needCreate
     *            boolean,if the file exits, needCreate is false,if the file not exits,needCreate is true
     * @throws IOException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public void save(boolean needCreate) throws IOException, TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {
        logger.info("entering save...");
        File file = new File(path);
        if (needCreate == true) {
            file.createNewFile();
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        for (PersonInfo person : personInfos) {
            Element XMLperson = document.createElement("person");
            Element XMLname = document.createElement("name");
            Element XMLphone = document.createElement("phone");
            Element XMLaddress = document.createElement("address");
            XMLname.setTextContent(person.getName());
            XMLphone.setTextContent(person.getPhone());
            XMLaddress.setTextContent(person.getAddress());
            XMLperson.appendChild(XMLname);
            XMLperson.appendChild(XMLphone);
            XMLperson.appendChild(XMLaddress);
            document.appendChild(XMLperson);
        }
        StreamResult result = new StreamResult(file);
        Transformer transformer;
        transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
        logger.info("leaving save...");
    }

    /**
     * load load the address book
     * 
     * @return boolean,whether load is success or not
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public boolean load() throws SAXException, IOException, ParserConfigurationException {
        logger.info("entering load...");
        boolean loadSuccess = true;
        File file = new File(path);
        if (!file.exists()) {
            loadSuccess = false;
        } else {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            personInfos = new ArrayList<PersonInfo>();
            modify = true;
            NodeList persons = document.getElementsByTagName("person");
            for (int i = 0; i < persons.getLength(); i++) {
                Element XMLperson = (Element) persons.item(i);
                Node XMLname = XMLperson.getElementsByTagName("name").item(0);
                Node XMLphone = XMLperson.getElementsByTagName("phone").item(0);
                Node XMLaddress = XMLperson.getElementsByTagName("address").item(0);
                PersonInfo personInfo = new PersonInfo();
                personInfo.setName(XMLname.getTextContent());
                personInfo.setPhone(XMLphone.getTextContent());
                personInfo.setAddress(XMLaddress.getTextContent());
                personInfos.add(personInfo);
            }
        }
        logger.info("leaving load...");
        return loadSuccess;
    }

}
