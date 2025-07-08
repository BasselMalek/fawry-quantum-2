package com.basel.domain.entities;

import com.basel.domain.interfaces.IBook;
import com.basel.domain.repos.Inventory;

import java.util.Date;
import java.util.List;

public class Admin extends User {
    private final Inventory inventory;

    public Admin(String username, String email, String password, Inventory inventory) {
        super(username, email, password);
        this.inventory = inventory;
    }

    public void addDeliverableBook(String isbn, String title, String desc, Date date, Double price, Double weight, int quantity) {
        inventory.addItem(new DeliverableBook(isbn, title, desc, date, price, weight), quantity);
    }

    public void addMailablebook(String isbn, String title, String desc, Date date, Double price, String filetype) {
        inventory.addItem(new MailableBook(isbn, title, desc, date, price, filetype), Integer.MAX_VALUE);
    }

    public void addDemoBook(String isbn, String title, String desc, Date date, Double price) {
        inventory.addItem(new Book(isbn, title, desc, date, price), Integer.MAX_VALUE);
    }


    public boolean removeBook(String id) {
        return inventory.removeBook(id);
    }

    public List<IBook> cleanOutdated(Date breakpoint) {
        return inventory.cleanUpOutdated(breakpoint);
    }
}
