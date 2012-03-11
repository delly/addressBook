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

public class AddressBook {
	final Logger logger = LoggerFactory.getLogger(AddressBook.class);

	private List<PersonInfo> personInfos = null;
	private String path = null;
	private boolean modify;


	public AddressBook() {
		super();
		personInfos = new ArrayList<PersonInfo>();
		path = "";
		modify = false;
	}

	public List<PersonInfo> getPersonInfos() {
		return personInfos;
	}
	

	public void setPersonInfos(List<PersonInfo> personInfos) {
		this.personInfos = personInfos;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public Logger getlogger() {
		return logger;
	}

	public boolean add() throws IOException {
		PersonInfo personInfo = new PersonInfo();
		System.out.println("Please enter in this fomat: name;phone;address");
		String str = doIO();
		 String[] content = str.split(";");
		 if(content.length == 3) {
			 personInfo.setName(content[0]);
			 personInfo.setPhone(content[1]);
			 personInfo.setAddress(content[2]);
			personInfos.add(personInfo);
			return true;
		 } else {
			 System.out.println("Please enter information in the right format!");
			 return false;
		 }
	}

	public String doIO() throws IOException {
		String str;
			BufferedReader input = new BufferedReader(new InputStreamReader(
					System.in));
			str = input.readLine();
		return str;
	}

	public List<PersonInfo> compFind() throws IOException {
		ArrayList<PersonInfo> resultList = null;
		System.out.println("Please enter the phone number: ");
		String phone = doIO();
		for (PersonInfo personInfo : personInfos) {
			if (personInfo.getPhone().equals(phone)) {
				if (resultList == null) {
					resultList = new ArrayList<PersonInfo>();
				}
				resultList.add(personInfo);
			}
		}
		return resultList;
	}

	public List<PersonInfo> partFind() throws IOException {
		ArrayList<PersonInfo> resultList = null;
		System.out.println("Please enter the phone number: ");
		String phone = doIO();
		for (PersonInfo personInfo : personInfos) {
			if (personInfo.getPhone().indexOf(phone) != -1) {
				if (resultList == null) {
					resultList = new ArrayList<PersonInfo>();
				}
				resultList.add(personInfo);
			}
		}
		return resultList;
	}

	public void show(List<PersonInfo> personList) {
		if (personList != null) {
			for (PersonInfo personInfo : personList) {
				System.out.println("Name: " + personInfo.getName());
				System.out.println("Phone: " + personInfo.getPhone());
				System.out.println("address: " + personInfo.getAddress());
			}
		} else {
			System.out.println("There's no match person!");
		}
	}

	public boolean save() throws IOException,TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {
		logger.info("entering save...");
		File file = new File(path);
			if (file.exists() && modify == false) {
				System.out
						.println("File exists! Do you want to recover the file ?");
				String order = doIO();
				if (!order.equals("y")) {
					return false;
				}
			} else {
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
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
		return true;
	}

	public boolean load() throws SAXException, IOException, ParserConfigurationException {
		logger.info("entering load...");
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("File not exists!");
			return false;
		} else {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder;
					builder = factory.newDocumentBuilder();
					Document document = builder.parse(file);
				personInfos = new ArrayList<PersonInfo>();
				modify = true;
				NodeList persons = document.getElementsByTagName("person");
				for (int i=0; i<persons.getLength(); i++) {
					Element XMLperson = (Element)persons.item(i);
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
		return true;
	}
	
	public void sendMessage(String order) throws IOException, ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException {
		logger.info("Send Message: {}", order);
		if (order.equals("add")) {
			add();
		} else if (order.equals("show")) {
			show(getPersonInfos());
		} else if (order.equals("find -comp")) {
			List<PersonInfo> personList = compFind();
			show(personList);
		} else if (order.equals("find -part")) {
			List<PersonInfo> personList = partFind();
			show(personList);
		} else if (order.equals("save")) {
			while (getPath().equals("")) {
				System.out.println("Please enter the path: ");
				setPath(doIO() + ".addr");
			}
			save();
		} else if (order.equals("load")) {
			System.out.println("Please enter the path: ");
			setPath(doIO() + ".addr");
			load();
		} else {
			System.out.println("Please enter the right order!");
		}
		
	}

}
