package com.basel.app.services;

import com.basel.domain.dtos.QuantifiedEntry;
import com.basel.domain.entities.Book;
import com.basel.domain.entities.DeliverableBook;
import com.basel.domain.interfaces.IBook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShippingService {
    public Boolean handleDeliverableBook(DeliverableBook book) {
        return true;
    }
}