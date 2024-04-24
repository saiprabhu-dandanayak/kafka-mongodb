package com.prabhu.kafka.producer.service;

import com.prabhu.kafka.producer.dto.BookDTO;
import java.util.List;

public interface BookService {
    void createBook(BookDTO bookDTO);
    List<BookDTO> getAllBooks();
}
