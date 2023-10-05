package spring.project2.models;


import jakarta.persistence.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "author")
    private String author;

    @Column(name = "year_of_production")
    private int yearOfProduction;

    @ManyToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "date_of_issue")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfIssue;

    @Transient
    private final int DAYS = -1;


    public Book() {
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Book(String book_name, String author, int year_of_production) {
        this.bookName = book_name;
        this.author = author;
        this.yearOfProduction = year_of_production;
    }

    public int getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getAuthor() {
        return author;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public void setBookId(int book_id) {
        this.bookId = book_id;
    }

    public void setBookName(String book_name) {
        this.bookName = book_name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setYearOfProduction(int year_of_production) {
        this.yearOfProduction = year_of_production;
    }
    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public boolean bookIsOverdue() {
        return translateToDays(new Date().getTime() - dateOfIssue.getTime()) > DAYS;
    }

    private long translateToDays(long milliseconds) {
        return TimeUnit.MILLISECONDS.toDays(milliseconds);
    }

    @Override
    public String toString() {
        return this.getBookName() + ", " + this.getAuthor() + ", " + this.getYearOfProduction();
    }
}
