package com.prabhu.kafka.producer.service.impl;

import com.prabhu.kafka.producer.document.Book;
import com.prabhu.kafka.producer.dto.BookDTO;
import com.prabhu.kafka.producer.exception.BookStorageException;
import com.prabhu.kafka.producer.repository.BookRepository;
import com.prabhu.kafka.producer.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KafkaTemplate<String, Book> kafkaTemplate;

    @Override
    public void createBook(BookDTO bookDTO) {
        try {
            Book book = modelMapper.map(bookDTO, Book.class);
            kafkaTemplate.send("book-topic", book);
        } catch (DataAccessException ex) {
            throw new BookStorageException("Failed to save book: " + bookDTO.getTitle());
        }
    }
    @Override
    public List<BookDTO> getAllBooks() {
        try {
            List<Book> books = bookRepository.findAll();
            return books.stream()
                    .map(book -> modelMapper.map(book, BookDTO.class))
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            throw new BookStorageException("Failed to fetch books");
        }
    }
}
