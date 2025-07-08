package com.basel.domain.entities;

import com.basel.domain.interfaces.IBook;

import java.util.Date;


public class Book implements IBook {

    private final String isbn;
    private final String title;
    private final String desc;
    private final Date date;
    private final Double price;

    public Book(String isbn, String title, String desc, Date date, Double price) {
        this.isbn = isbn;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.price = price;
    }

    @Override
    public String getIsbn() {
        return isbn;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDesc() {
        return desc;
    }
    @Override
    public Date getPublishDate() {
        return date;
    }

    @Override
    public Double getPrice() {
        return price;
    }
}
