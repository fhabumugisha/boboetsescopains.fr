package fr.boboetsescopains.controller;

import fr.boboetsescopains.entity.Book;
import fr.boboetsescopains.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BookService bookService;

    @GetMapping("/")
    public String home(Model model) {
        List<Book> books = bookService.getAllPublishedBooks();
        model.addAttribute("books", books);
        model.addAttribute("pageTitle", "Bobo et ses copains");
        return "index";
    }
}
