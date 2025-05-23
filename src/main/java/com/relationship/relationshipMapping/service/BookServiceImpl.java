package com.relationship.relationshipMapping.service;

import com.relationship.relationshipMapping.dto.Mapper;
import com.relationship.relationshipMapping.dto.requestDto.BookRequestDto;
import com.relationship.relationshipMapping.dto.responseDto.BookResponseDto;
import com.relationship.relationshipMapping.model.Author;
import com.relationship.relationshipMapping.model.Book;
import com.relationship.relationshipMapping.model.Category;
import com.relationship.relationshipMapping.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Transactional
    @Override
    public BookResponseDto addBook(BookRequestDto bookRequestDto) {
       Book book = new Book();
       book.setName(bookRequestDto.getName());
       if(bookRequestDto.getAuthorIds().isEmpty()){
           throw new IllegalArgumentException("You need at least an author");
       } else{
           List<Author> authors = new ArrayList<>();
           for (Long authorId: bookRequestDto.getAuthorIds()){
               Author author = authorService.getAuthor(authorId);
               authors.add(author);
           }
           book.setAuthors(authors);
       }
       if (bookRequestDto.getCategoryId() == null){
           throw new IllegalArgumentException("book should have at least one category");
       }
       Category category = categoryService.getCategory(bookRequestDto.getCategoryId());
       book.setCategory(category);

       Book book1 = bookRepository.save(book);
       return Mapper.bookToBookResponseDto(book1);

    }

    @Override
    public BookResponseDto getBookById(Long bookId) {
      Book book = getBook(bookId);
      return Mapper.bookToBookResponseDto(book);
    }

    @Override
    public Book getBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new IllegalArgumentException("cannot find book with id:" + bookId));
        return  book;
    }

    @Override
    public List<BookResponseDto> getBooks() {
        List<Book> books = StreamSupport.stream(bookRepository.findAll().spliterator(),false)
                .collect(Collectors.toList());
        return Mapper.booksToBookResponseDtos(books);
    }

    @Override
    public BookResponseDto deleteBook(Long bookId) {
       Book book = getBook(bookId);
       bookRepository.delete(book);
       return Mapper.bookToBookResponseDto(book);
    }
    @Transactional
    @Override
    public BookResponseDto editBook(Long bookId, BookRequestDto bookRequestDto) {
        Book bookToEdit = getBook(bookId);
        bookToEdit.setName(bookRequestDto.getName());
        if(!bookRequestDto.getAuthorIds().isEmpty()){
            List<Author> authors = new ArrayList<>();
            for(Long authorId: bookRequestDto.getAuthorIds()){
                Author author = authorService.getAuthor(authorId);
                authors.add(author);
            }
            bookToEdit.setAuthors(authors);
        }
        if (bookRequestDto.getCategoryId() != null){
            Category category = categoryService.getCategory(bookRequestDto.getCategoryId());
            bookToEdit.setCategory(category);
        }
        return Mapper.bookToBookResponseDto(bookToEdit);
    }

    @Override
    public BookResponseDto addAuthorToBook(Long bookId, Long authorId) {
       Book book = getBook(bookId);
       Author author = authorService.getAuthor(authorId);
       if(author.getBooks().contains(author)){
           throw new IllegalArgumentException("this author is already assigned to this book");
       }
       book.addAuthor(author);
       author.addBook(book);
       return Mapper.bookToBookResponseDto(book);
    }

    @Override
    public BookResponseDto deleteAuthorFromBook(Long bookId, Long authorId) {
        Book book = getBook(bookId);
        Author author = authorService.getAuthor(authorId);
        if(!(author.getBooks().contains(book))){
            throw new IllegalArgumentException("book does not have this author");
        }
        author.removeBook(book);
        book.deleteAuthor(author);
        return Mapper.bookToBookResponseDto(book);
    }

    @Override
    public BookResponseDto addCategoryToBook(Long bookId, Long categoryId) {
        Book book = getBook(bookId);
        Category category = categoryService.getCategory(categoryId);
        if(Objects.nonNull(book.getCategory())){
            throw new IllegalArgumentException("Book already has a category");
        }
        book.setCategory(category);
        category.addBook(book);
        return Mapper.bookToBookResponseDto(book);
    }

    @Override
    public BookResponseDto removeCategoryFromBook(Long bookId, Long categoryId) {
       Book book = getBook(bookId);
       Category category = categoryService.getCategory(categoryId);
       if(!(Objects.nonNull(book.getCategory()))){
           throw new IllegalArgumentException("book does not have a category to delete");
       }
       book.setCategory(null);
       category.removeBook(book);
       return Mapper.bookToBookResponseDto(book);
    }
}
