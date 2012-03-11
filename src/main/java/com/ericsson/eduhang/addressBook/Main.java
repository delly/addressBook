package com.ericsson.eduhang.addressBook;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;


public class Main implements Runnable {
	
	public static void main(String[] args) throws IOException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(new Main());
		executorService.shutdown();
	}

	@Override
	public void run() {
		String order = null;
		AddressBook addressBook = new AddressBook();

			System.out.println("~Welcome to BIGCOW address Book~");
		try {
			order = addressBook.doIO();
		} catch (IOException e1) {
			addressBook.getlogger().error(e1.toString());
			e1.printStackTrace();
		}

		while (!order.equals("quit")) {
			try {
				try {
					addressBook.sendMessage(order);
				} catch (IOException e) {
					addressBook.getlogger().error(e.toString());
					e.printStackTrace();
				}
			} catch (ParserConfigurationException e) {
				addressBook.getlogger().error(e.toString());
				e.printStackTrace();
			} catch (SAXException e) {
				addressBook.getlogger().error(e.toString());
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				addressBook.getlogger().error(e.toString());
				e.printStackTrace();
			} catch (TransformerException e) {
				addressBook.getlogger().error(e.toString());
				e.printStackTrace();
			}
			System.out.println("~Welcome to BIGCOW address Book~");
			try {
				order = addressBook.doIO();
			} catch (IOException e) {
				addressBook.getlogger().error(e.toString());
				e.printStackTrace();
			}
		}
		
	}
}
