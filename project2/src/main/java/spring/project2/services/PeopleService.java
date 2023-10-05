package spring.project2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.project2.models.Book;
import spring.project2.models.Person;
import spring.project2.repositories.BooksRepository;
import spring.project2.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Person getOwner(int bookId) {
        Optional<Book> book = booksRepository.findById(bookId);
        return book.orElse(new Book()).getOwner();
    }
}
