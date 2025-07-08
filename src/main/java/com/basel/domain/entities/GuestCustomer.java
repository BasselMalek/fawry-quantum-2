package com.basel.domain.entities;

import com.basel.app.services.MailingService;
import com.basel.app.services.ShippingService;
import com.basel.domain.dtos.QuantifiedEntry;
import com.basel.domain.interfaces.Deliverable;
import com.basel.domain.interfaces.Mailable;
import com.basel.domain.repos.Inventory;

public class GuestCustomer extends User {
    private final String address;
    private final MailingService mailingService;
    private final Inventory inventory;
    private final ShippingService shippingService;
    private double balance;
    private QuantifiedEntry selectedBook;


    public GuestCustomer(String email, String address, double initialBalance, Inventory inventory, ShippingService shippingService, MailingService mailingService) {
        super("guest", email, "guest");
        this.balance = initialBalance;
        this.address = address;
        this.inventory = inventory;
        this.shippingService = shippingService;
        this.mailingService = mailingService;
    }

    public double getBalance() {
        return balance;
    }


    public boolean modifyBalance(double amount) {
        if (this.balance + amount >= 0) {
            this.balance += amount;
            return true;
        }
        return false;
    }

    public Boolean setSelectedBook(String isbn, int quantity) {
        QuantifiedEntry query = inventory.getBook(isbn);
        if (query != null && quantity <= query.getQuantity()) {
            this.selectedBook = new QuantifiedEntry(query.getBook(), quantity);
            return true;
        }
        return false;
    }

    public boolean checkout(Inventory inventory) {
        if (selectedBook == null) {
            System.out.println("Error: No book is selected");
            return false;
        }

        if ((!(selectedBook.getBook() instanceof Deliverable)) && (!(selectedBook.getBook() instanceof Mailable))) {
            System.out.println("Selected book is a demo book. Select the \"Read demo\" option to view");
            return true;
        }

        if (balance < selectedBook.getBook().getPrice() * selectedBook.getQuantity()) {
            System.out.println("Error: Insufficient balance");
            System.out.printf("Required: $%.2f, Available: $%.2f%n", selectedBook.getBook().getPrice() * selectedBook.getQuantity(), balance);
            return false;
        }

        if (selectedBook instanceof Deliverable) {
            inventory.updateQuantity(selectedBook.getBook().getIsbn(), inventory.getBook(selectedBook.getBook().getIsbn()).getQuantity() - selectedBook.getQuantity());
            shippingService.handleDeliverableBook((DeliverableBook) selectedBook.getBook());
        } else if (selectedBook instanceof Mailable) {
            mailingService.handleMailableBook((MailableBook) selectedBook.getBook());
        }

        modifyBalance(-(selectedBook.getBook().getPrice() * selectedBook.getQuantity()));
        System.out.printf("Paid: $%.2f, Available: $%.2f%n", selectedBook.getBook().getPrice() * selectedBook.getQuantity(), balance);
        System.out.println("Order completed successfully!");
        return true;
    }

    public void viewSelectedBook() {
        System.out.println("================== SELECTED BOOK ==================");
        System.out.println();

        System.out.println("  ISBN: " + selectedBook.getBook().getIsbn());
        System.out.println("  Title: " + selectedBook.getBook().getTitle());
        System.out.println("  Year of publishing: " + selectedBook.getBook().getPublishDate());
        System.out.println("  Description: " + selectedBook.getBook().getDesc());
        System.out.println("  Price: $" + String.format("%.2f", selectedBook.getBook().getPrice()));

        if (selectedBook.getBook() instanceof Deliverable) {
            System.out.println("  Type: Paperback");
            System.out.println("  Weight: " + String.format("%.2f", ((Deliverable) selectedBook.getBook()).getWeight()) + " lbs");
            System.out.println("  Quantity Available: " + selectedBook.getQuantity());
        } else if (selectedBook.getBook() instanceof Mailable) {
            System.out.println("  Type: EBook");
            System.out.println("  File Type:" + ((Mailable) selectedBook.getBook()).getFileType());
        } else {
            System.out.println("  Type: Demo");
        }
        System.out.println();

        System.out.println("===============================================");
    }

    public String getSelectedBookType() {
        if (selectedBook.getBook() instanceof Deliverable) {
            return "paperback";
        } else if (selectedBook.getBook() instanceof Mailable) {
            return "ebook";
        } else {
            return "demo";
        }
    }
}
