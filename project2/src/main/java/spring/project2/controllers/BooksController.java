package spring.project2.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring.project2.models.Book;
import spring.project2.models.Person;
import spring.project2.services.BooksService;
import spring.project2.services.PeopleService;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;

    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("books", booksService.findAll());
        return "books/index";
    }

    @RequestMapping(method = RequestMethod.GET, params = {"page", "books_per_page"})
    public String index(Model model,
                        @RequestParam(name = "page") int page,
                        @RequestParam(name = "books_per_page") int booksPerPage) {
        model.addAttribute("books", booksService.findAll(page, booksPerPage));
        return "books/index";
    }

    @RequestMapping(method = RequestMethod.GET, params = {"sort_by_year"})
    public String index(Model model,
                        @RequestParam(name = "sort_by_year") boolean sortByYear) {
        model.addAttribute("books", booksService.findAll(sortByYear));
        return "books/index";
    }

    @RequestMapping(method = RequestMethod.GET, params = {"page", "books_per_page", "sort_by_year"})
    public String index(Model model,
                        @RequestParam(name = "page") int page,
                        @RequestParam(name = "books_per_page") int booksPerPage,
                        @RequestParam(name =  "sort_by_year") boolean sortByYear) {
        model.addAttribute("books", booksService.findAll(page, booksPerPage, sortByYear));
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping ("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));
        model.addAttribute("people", peopleService.findAll());
        model.addAttribute("owner", peopleService.getOwner(id));
        return "books/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books/{id}";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/free")
    public String free(@PathVariable("id") int id) {
        booksService.deleteConnection(id);
        return "redirect:/books/{id}";
    }

    @PostMapping("/{id}/set")
    public String set(@PathVariable("id") int id, @ModelAttribute Person person) {
        booksService.setConnection(id, person.getId());
        return "redirect:/books/{id}";
    }

    @GetMapping("/search")
    public String search(Model model){
        model.addAttribute("books", null);
        return "books/search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, params = {"beginning_of_the_name"})
    public String search(@RequestParam(name = "beginning_of_the_name") String beginningOfTheName, Model model){
        model.addAttribute("books", booksService.findByBookNameStartingWith(beginningOfTheName));
        return "books/search";
    }
}
