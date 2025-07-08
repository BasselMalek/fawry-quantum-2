package com.basel.domain.repos;

import com.basel.domain.dtos.QuantifiedEntry;
import com.basel.domain.interfaces.Deliverable;
import com.basel.domain.interfaces.IBook;
import com.basel.domain.interfaces.Mailable;

import java.util.*;

public class Inventory {
    private final Map<String, QuantifiedEntry> bookInventory = new HashMap<>();

    static public void displayBooks(List<IBook> books) {
        if (books == null || books.isEmpty()) {
            System.out.println("No items available.");
            return;
        }

        System.out.println("================== BOOKS ==================");
        System.out.println();

        for (IBook item : books) {
            System.out.println("  ISBN: " + item.getIsbn());
            System.out.println("  Title: " + item.getTitle());
            System.out.println("  Year of publishing: " + item.getPublishDate().toString());
            System.out.println("  Description: " + item.getDesc());
            System.out.println("  Price: $" + String.format("%.2f", item.getPrice()));

            if (item instanceof Deliverable) {
                System.out.println("  Type: Paperback");
                System.out.println("  Weight: " + String.format("%.2f", ((Deliverable) item).getWeight()) + " lbs");
            } else if (item instanceof Mailable) {
                System.out.println("  Type: EBook");
                System.out.println("  File Type:" + ((Mailable) item).getFileType());
            } else {
                System.out.println("  Type: Demo");
            }
            System.out.println();

            System.out.println("===============================================");
        }
    }

    public void addItem(IBook item, int quantity) {
        bookInventory.put(item.getIsbn(), new QuantifiedEntry(item, quantity));
    }

    public QuantifiedEntry getBook(String id) {
        QuantifiedEntry query = bookInventory.get(id);
        if (query != null) {
            return query;
        }
        System.out.println("Invalid Type/Book ID.");
        return null;
    }

    public HashMap<String, QuantifiedEntry> getBooks() {
        return new HashMap<>(bookInventory);
    }

    public void updateQuantity(String id, int newQuantity) {
        QuantifiedEntry entry = getBook(id);
        if (entry != null) {
            entry.setQuantity(newQuantity);
        }
    }

    public boolean removeBook(String id) {
        QuantifiedEntry query = bookInventory.get(id);
        if (query != null) {
            bookInventory.remove(id);
            return true;
        }
        System.out.println("Invalid Type/Book ID.");
        return false;
    }

    public boolean hasBook(String id, int quantity) {
        QuantifiedEntry query = bookInventory.get(id);
        if (query != null && quantity > query.getQuantity()) {
            return true;
        }
        System.out.println("Invalid Book ISBN/Invalid quantity.");
        return false;
    }

    public void displayBooks() {
        HashMap<String, QuantifiedEntry> items = this.getBooks();
        if (items == null || items.isEmpty()) {
            System.out.println("No items available.");
            return;
        }

        System.out.println("================== BOOKS ==================");
        System.out.println();

        for (Map.Entry<String, QuantifiedEntry> entry : items.entrySet()) {
            QuantifiedEntry quantifiedEntry = entry.getValue();
            IBook item = quantifiedEntry.getBook();

            System.out.println("  ISBN: " + item.getIsbn());
            System.out.println("  Title: " + item.getTitle());
            System.out.println("  Year of publishing: " + item.getPublishDate());
            System.out.println("  Description: " + item.getDesc());
            System.out.println("  Price: $" + String.format("%.2f", item.getPrice()));

            if (item instanceof Deliverable) {
                System.out.println("  Type: Paperback");
                System.out.println("  Weight: " + String.format("%.2f", ((Deliverable) item).getWeight()) + " lbs");
                System.out.println("  Quantity Available: " + quantifiedEntry.getQuantity());
            } else if (item instanceof Mailable) {
                System.out.println("  Type: EBook");
                System.out.println("  File Type:" + ((Mailable) item).getFileType());
            } else {
                System.out.println("  Type: Demo");
            }
            System.out.println();

            System.out.println("===============================================");
        }
    }

    public List<IBook> cleanUpOutdated(Date breakpoint) {
        List<IBook> outdatedBooks = new ArrayList<>();
        bookInventory.entrySet().removeIf(entry -> {
            IBook book = entry.getValue().getBook();
            if (breakpoint.after(book.getPublishDate())){
            outdatedBooks.add(book);
            return true;
            }
            return false;
        });
        return outdatedBooks;
    }

}