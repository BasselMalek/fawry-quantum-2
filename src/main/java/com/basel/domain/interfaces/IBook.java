package com.basel.domain.interfaces;

import java.util.Date;

public interface IBook {
    String getIsbn();
    String getTitle();
    Date getPublishDate();
    Double getPrice();
    String getDesc();

}
