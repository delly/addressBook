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
import javax.xml.transform.TransformerConfigurationException;
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

	public void add() {
		PersonInfo personInfo = new PersonInfo();
		System.out.println("Please enter name: ");
		personInfo.setName(doIO());
		System.out.println("Please enter phone number: ");
		personInfo.setPhone(doIO());
		System.out.println("Please enter address: ");
		personInfo.setAddress(doIO());
		personInfos.add(personInfo);
	}

	public String doIO() {
		String str;
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					System.in));
			str = input.readLine();
		} catch (IOException e) {
			System.out.println("Please insert the information!");
			str = doIO();
		}
		return str;
	}

	public List<PersonInfo> compFind() {
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

	public List<PersonInfo> partFind() {
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

	public boolean save() {
		logger.info("entering save...");
		File file = new File(path);
		try {
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
		} catch (IOException e) {
			logger.error(e.toString());
			//e.printStackTrace();
			//return false;
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
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
		} catch (ParserConfigurationException e) {
			logger.error(e.toString());
			//e.printStackTrace();
			//return false;
		} catch (TransformerConfigurationException e) {
			logger.error(e.toString());
			//e.printStackTrace();
			//return false;
		} catch (TransformerFactoryConfigurationError e) {
			logger.error(e.toString());
			//e.printStackTrace();
			//return false;
		} catch (TransformerException e) {
			logger.error(e.toString());
			//e.printStackTrace();
			//return false;
		}
		return true;
	}

	public boolean load() {
		logger.info("entering load...");
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("File not exists!");
			return false;
		} else {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			try {
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
			} catch (IOException e) {
				logger.error(e.toString());
				//e.printStackTrace();
				//return false;
			} catch (ParserConfigurationException e) {
				logger.error(e.toString());
				//e.printStackTrace();
				//return false;
			} catch (SAXException e) {
				logger.error(e.toString());
				//e.printStackTrace();
				//return false;
			}
		}
		return true;
	}

}
