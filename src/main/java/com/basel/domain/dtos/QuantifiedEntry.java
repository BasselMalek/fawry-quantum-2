package com.basel.domain.dtos;


import com.basel.domain.interfaces.IBook;

public class QuantifiedEntry {
        private final IBook book;
        private int quantity;

        public QuantifiedEntry(IBook book, int quantity){
            this.book = book;
            this.quantity = quantity;
        }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public IBook getBook() {
        return book;
    }
}
