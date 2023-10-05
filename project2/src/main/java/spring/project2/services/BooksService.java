package spring.project2.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.project2.models.Book;
import spring.project2.models.Person;
import spring.project2.repositories.BooksRepository;
import spring.project2.repositories.PeopleRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(int page, int booksPerPage) {
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Object findAll(boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(Sort.by("yearOfProduction"));
        }
        return booksRepository.findAll();
    }

    public Object findAll(int page, int booksPerPage, boolean sortByYear) {
        if(sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("yearOfProduction"))).getContent();
        }
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public Book findOne(int book_id) {
        Optional<Book> foundBook = booksRepository.findById(book_id);
        return foundBook.orElse(null);
    }

    @Transactional
    public void update(int bookId, Book book) {
        book.setBookId(bookId);
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public List<Book> getBooks(int personId) {
        Optional<Person> person = peopleRepository.findById(personId);
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            return person.get().getBooks();
        }
        return Collections.EMPTY_LIST;
    }

    @Transactional
    public void deleteConnection(int bookId) {
        Optional<Book> bookOptional = booksRepository.findById(bookId);
        if(bookOptional.isPresent()) {
            Book book = bookOptional.get();
            Person person = book.getOwner();
            if(person != null) {
                person.getBooks().remove(book);
                book.setOwner(null);
            }
        }
    }

    @Transactional
    public void setConnection(int bookId, int personId) {
        Optional<Person> personOptional = peopleRepository.findById(personId);
        Optional<Book> bookOptional = booksRepository.findById(bookId);
        if(bookOptional.isPresent() & personOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setDateOfIssue(new Date());
            Person person = personOptional.get();
            if(person.getBooks() == null) {
                person.setBooks(new ArrayList<>());
            }
            person.getBooks().add(book);
            book.setOwner(person);
        }
    }

    public List<Book> findByBookNameStartingWith(String startingWith) {
        return booksRepository.findByBookNameStartingWith(startingWith);
    }
}
