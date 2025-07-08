package com.basel.domain.entities;

import com.basel.domain.interfaces.Deliverable;

import java.util.Date;

public class DeliverableBook extends Book implements Deliverable {
    private final Double weight;
        public DeliverableBook(String isbn, String title, String desc, Date date, Double price, Double weight) {
            super(isbn, title, desc, date, price);
            this.weight = weight;
        }

    @Override
    public Double getWeight() {
        return weight;
    }
}
