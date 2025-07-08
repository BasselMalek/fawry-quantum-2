package com.basel.domain.presentation;

import com.basel.app.services.MailingService;
import com.basel.app.services.ShippingService;
import com.basel.domain.entities.Admin;
import com.basel.domain.entities.GuestCustomer;
import com.basel.domain.entities.User;
import com.basel.domain.interfaces.IBook;
import com.basel.domain.repos.Inventory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static Date parseDate(String sdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return sdf.parse(sdate);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-mm-dd format.");
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Inventory inventory = new Inventory();
        ShippingService shippingService = new ShippingService();
        MailingService mailingService = new MailingService();
        Date currentDate = new Date();
        User loggedInUser = null;
        Admin admin = new Admin("bookowner", "qa@fawry.com", "qa123", inventory);
        while (true) {
            System.out.println("Hi to Benji's Bookstore!");
            System.out.println("If you are an admin select (a), if you are a customer select (c)");
            System.out.println();
            String userChoice = scanner.next();
            if (userChoice.equals("a")) {
                System.out.print("Email: ");
                String email = scanner.next();
                System.out.print("Password: ");
                String password = scanner.next();
                if (admin.verifyLogin(email, password)) {
                    loggedInUser = admin;
                } else {
                    System.out.println("Invalid Data.");
                }
            } else if (userChoice.equals("c")) {
                System.out.print("Email: ");
                String email = scanner.next();
                scanner.nextLine();
                System.out.print("Address: ");
                String address = scanner.nextLine();
                System.out.print("Recharge balance:$ ");
                Double balance = scanner.nextDouble();
                loggedInUser = new GuestCustomer(email, address, balance, inventory, shippingService, mailingService);
            }
            while (loggedInUser != null) {
                if (loggedInUser instanceof GuestCustomer) {
                    System.out.println("Hi!");
                    System.out.println();
                    System.out.println("1. Browse Books");
                    System.out.println("2. View Selected Book");
                    System.out.println("3. Checkout");
                    System.out.println("5. Log Out");
                    System.out.println("4. Read Selected Demo Book");
                    System.out.println();
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();
                    switch (choice) {
                        case 1:
                            System.out.println();
                            inventory.displayBooks();
                            System.out.println();
                            System.out.print("Enter book ISBN to select or enter q to go back: ");
                            String produceId = scanner.next();
                            if (produceId.equals("q")) {
                                break;
                            }
                            System.out.print("Enter desired quantity to add to cart: ");
                            int quantity = scanner.nextInt();
                            if (((GuestCustomer) loggedInUser).setSelectedBook(produceId, quantity)) {
                                System.out.println("Book Selected!");
                            } else {
                                System.out.println("Invalid input.");
                            }
                            break;
                        case 2:
                            ((GuestCustomer) loggedInUser).viewSelectedBook();
                            break;
                        case 3:
                            boolean checkout = ((GuestCustomer) loggedInUser).checkout(inventory);
                            break;
                        case 4:
                            if (((GuestCustomer) loggedInUser).getSelectedBookType().equals("demo")) {
                                System.out.println("Hi! i was gonna put lorem ipsum here but i figured i'd put a thank you for the hiring team");
                            } else {
                                System.out.println("Selected book is not a demo book.");
                            }
                            break;
                        case 5:
                            loggedInUser = null;
                            break;
                    }
                } else {
                    System.out.println("Logged in!");
                    System.out.println("1. Add Book");
                    System.out.println("2. Remove Book");
                    System.out.println("3. Browse Books");
                    System.out.println("4. Clean Outdated Books");
                    System.out.println("5. Log Out");
                    System.out.println();
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            System.out.print("Enter book type (paperback, ebook, demo): ");
                            String bookType = scanner.next().toLowerCase();
                            System.out.print("Enter ISBN: ");
                            String isbn = scanner.next();
                            System.out.print("Enter title: ");
                            scanner.nextLine();
                            String title = scanner.nextLine();
                            System.out.print("Enter publishing date (dd-mm-yyyy): ");
                            String psdate = scanner.next();
                            Date pdate = parseDate(psdate);
                            if (pdate == null) {
                                System.out.println("Invalid inmput.");
                                break;
                            }
                            scanner.nextLine();
                            System.out.print("Enter description: ");
                            String desc = scanner.nextLine();
                            System.out.print("Enter price: ");
                            Double price = scanner.nextDouble();



                            switch (bookType) {
                                case "paperback":
                                    System.out.print("Enter quantity: ");
                                    int quantity = scanner.nextInt();
                                    System.out.print("Enter weight: ");
                                    Double weight = scanner.nextDouble();
                                    admin.addDeliverableBook(isbn, title, desc, pdate, price, weight, quantity);
                                    System.out.println("Paperback book added successfully!");
                                    break;

                                case "ebook":
                                    System.out.print("Enter file type (pdf, epub, mobi): ");
                                    String filetype = scanner.next();
                                    admin.addMailablebook(isbn, title, desc, pdate, price, filetype);
                                    System.out.println("Ebook added successfully!");
                                    break;

                                case "demo":
                                    admin.addDemoBook(isbn, title, desc, pdate, price);
                                    System.out.println("Demo book added successfully!");
                                    break;

                                default:
                                    System.out.println("Invalid book type.");
                                    break;
                            }
                            System.out.println();
                            break;

                        case 2:
                            System.out.print("Enter book ID to remove: ");
                            String bookId = scanner.next();

                            if (admin.removeBook(bookId)) {
                                System.out.println("Book removed successfully!");
                            } else {
                                System.out.println("Book not found or could not be removed.");
                            }
                            System.out.println();
                            break;

                        case 3:
                            System.out.println();
                            inventory.displayBooks();
                            System.out.println();
                            break;

                        case 4:
                            System.out.print("Enter breakpoint date (yyyy-mm-dd): ");
                            String dateStr = scanner.next();
                            Date breakpoint = parseDate(dateStr);
                            if (breakpoint != null) {
                                List<IBook> removedBooks = admin.cleanOutdated(breakpoint);

                                if (removedBooks.isEmpty()) {
                                    System.out.println("No outdated books found.");
                                } else {
                                    System.out.println("Removed " + removedBooks.size() + " outdated books:");
                                    Inventory.displayBooks(removedBooks);
                                }
                            } else {
                                System.out.println("Invalid inmput.");
                            }

                            System.out.println();
                            break;
                        case 5:
                            loggedInUser = null;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            System.out.println();
                            break;
                    }
                }
            }
        }
    }
}
