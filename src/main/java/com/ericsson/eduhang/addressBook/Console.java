package com.ericsson.eduhang.addressBook;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class Console implements Runnable {

    private AddressBook addressBook;
    private static final Logger logger = LoggerFactory.getLogger(Console.class);

    /**
     * constructor
     */
    public Console() {
        super();
        addressBook = new AddressBook();
    }

    /**
     * add operation
     * 
     * @throws IOException
     */
    public void doAdd() throws IOException {
        System.out.println("Please enter in this fomat: name;phone;address");
        String str = addressBook.doIO(System.in);
        if (addressBook.add(str) == false)
            System.out.println("Please enter information in the right format!");
    }

    /**
     * complete find operation
     * 
     * @throws IOException
     */
    public void doCompFind() throws IOException {
        System.out.println("Please enter the phone number: ");
        String phone = addressBook.doIO(System.in);
        System.out.print(addressBook.show(addressBook.compFind(phone)));
    }

    /**
     * part find operation
     * 
     * @throws IOException
     */
    public void doPartFind() throws IOException {
        System.out.println("Please enter the phone number: ");
        String phone = addressBook.doIO(System.in);
        System.out.print(addressBook.show(addressBook.partFind(phone)));
    }

    /**
     * load operation
     * 
     * @throws IOException
     */
    public void doLoad() throws IOException {
        System.out.println("Please enter the path: ");
        addressBook.setPath(addressBook.doIO(System.in) + ".addr");
        try {
            if (!addressBook.load()) {
                System.out.println("addr file not exit!");
            }
        } catch (SAXException e) {
            logger.error("jaxp error at doLoad", e);
        } catch (ParserConfigurationException e) {
            logger.error("jaxp error at doLoad", e);
        }
    }

    /**
     * show operation
     */
    public void doShow() {
        System.out.print(addressBook.show(addressBook.getPersonInfos()));
    }

    /**
     * save operation
     * 
     * @throws IOException
     */
    public void doSave() throws IOException {
        if (addressBook.getPath().equals("")) {
            System.out.println("Please enter the path: ");
            addressBook.setPath(addressBook.doIO(System.in) + ".addr");
        }
        if (addressBook.fileExists()) {
            System.out.println("File exists! Do you want to recover the file ? (y/n)");
            String order = addressBook.doIO(System.in);
            if (order.equals("y")) {
                try {
                    addressBook.save(false);
                } catch (TransformerFactoryConfigurationError e) {
                    logger.error("jaxp error at doSave", e);
                } catch (TransformerException e) {
                    logger.error("jaxp error at doSave", e);
                } catch (ParserConfigurationException e) {
                    logger.error("jaxp error at doSave", e);
                }
            }
        } else {
            try {
                addressBook.save(true);
            } catch (TransformerFactoryConfigurationError e) {
                logger.error("jaxp error at doSave", e);
            } catch (TransformerException e) {
                logger.error("jaxp error at doSave", e);
            } catch (ParserConfigurationException e) {
                logger.error("jaxp error at doSave", e);
            }
        }
    }

    /**
     * sendMessage do different operation based on the order of the customer
     * 
     * @param order
     *            String,the operation you want to do
     * @throws IOException
     */
    public void sendMessage(String order) throws IOException {
        if (order.equals("load")) {
            doLoad();
        } else if (order.equals("save")) {
            doSave();
        } else if (order.equals("find -comp")) {
            doCompFind();
        } else if (order.equals("find -part")) {
            doPartFind();
        } else if (order.equals("show")) {
            doShow();
        } else if (order.equals("add")) {
            doAdd();
        } else {
            System.out.print("Please enter the right order!");
        }
    }

    @Override
    public void run() {
        System.out.println("~Welcome to BIGCOW address Book~");
        try {
            String order = addressBook.doIO(System.in);
            while (!order.equals("quit")) {
                sendMessage(order);
                System.out.println("~Welcome to BIGCOW address Book~");
                order = addressBook.doIO(System.in);
            }
        } catch (IOException e) {
            logger.error("io error at run", e);
        }
    }

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Console());
        executorService.shutdown();
    }
}
