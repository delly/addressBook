package com.ericsson.eduhang.addressBook;

import java.util.List;

public class Main {
	
	public static void main(String[] args) {

		String order = null;
		AddressBook addressBook = new AddressBook();

		System.out.println("~Welcome to BIGCOW address Book~");
		order = addressBook.doIO();

		while (!order.equals("quit")) {
			addressBook.getlogger().info("Send Message: {}", order);
			if (order.equals("add")) {
				addressBook.add();
			} else if (order.equals("show")) {
				addressBook.show(addressBook.getPersonInfos());
			} else if (order.equals("find -comp")) {
				List<PersonInfo> personList = addressBook.compFind();
				addressBook.show(personList);
			} else if (order.equals("find -part")) {
				List<PersonInfo> personList = addressBook.partFind();
				addressBook.show(personList);
			} else if (order.equals("save")) {
				while (addressBook.getPath().equals("")) {
					System.out.println("Please enter the path: ");
					addressBook.setPath(addressBook.doIO() + ".addr");
				}
				addressBook.save();
			} else if (order.equals("load")) {
				System.out.println("Please enter the path: ");
				addressBook.setPath(addressBook.doIO() + ".addr");
				addressBook.load();
			} else {
				System.out.println("Please enter the right order!");
			}
			System.out.println("~Welcome to BIGCOW address Book~");
			order = addressBook.doIO();
		}
	}
}
