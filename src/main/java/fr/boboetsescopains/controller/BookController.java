package fr.boboetsescopains.controller;

import fr.boboetsescopains.entity.Book;
import fr.boboetsescopains.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * Display single book details
     */
    @GetMapping("/{slug}")
    public String showBook(@PathVariable String slug, Model model) {
        Book book = bookService.getBookBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        // Increment views
        bookService.incrementBookViews(book.getId());

        model.addAttribute("book", book);
        model.addAttribute("pageTitle", book.getTitle());

        // Get related books (other published books)
        List<Book> relatedBooks = bookService.getAllPublishedBooks().stream()
                .filter(b -> !b.getId().equals(book.getId()))
                .limit(3)
                .toList();

        model.addAttribute("relatedBooks", relatedBooks);

        return "books/show";
    }
}
