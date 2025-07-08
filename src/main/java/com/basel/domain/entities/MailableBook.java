package com.basel.domain.entities;

import com.basel.domain.interfaces.Mailable;

import java.util.Date;

public class MailableBook extends Book implements Mailable {
    private final String fileType;
    public MailableBook(String isbn, String title, String desc, Date date, Double price, String fileType) {
        super(isbn, title, desc, date, price);
        this.fileType = fileType;
    }

    @Override
    public String getFileType() {
        return fileType;
    }
}
