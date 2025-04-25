package com.relationship.relationshipMapping.service;

import com.relationship.relationshipMapping.dto.requestDto.BookRequestDto;
import com.relationship.relationshipMapping.dto.responseDto.BookResponseDto;
import com.relationship.relationshipMapping.model.Book;

import java.util.List;

public interface BookService {
public BookResponseDto addBook(BookRequestDto bookRequestDto);
public BookResponseDto getBookById(Long bookId);
public Book getBook(Long bookId);
public List<BookResponseDto> getBooks();
public BookResponseDto deleteBook(Long bookId);
public BookResponseDto editBook(Long bookId, BookRequestDto bookRequestDto);
public BookResponseDto addAuthorToBook(Long bookId, Long authorId);
public BookResponseDto deleteAuthorFromBook(Long bookId, Long authorId);
public BookResponseDto addCategoryToBook(Long bookId, Long categoryId);
public BookResponseDto removeCategoryFromBook(Long bookId, Long categoryId);
}
