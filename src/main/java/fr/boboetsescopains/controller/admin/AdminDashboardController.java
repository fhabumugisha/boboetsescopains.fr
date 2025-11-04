package fr.boboetsescopains.controller.admin;

import fr.boboetsescopains.entity.Book;
import fr.boboetsescopains.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final BookService bookService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("publishedCount", bookService.countByStatus(Book.BookStatus.PUBLISHED));
        model.addAttribute("draftCount", bookService.countByStatus(Book.BookStatus.DRAFT));
        model.addAttribute("totalBooks", bookService.getAllBooks().size());
        model.addAttribute("recentBooks", bookService.getAllBooks().stream().limit(5).toList());

        return "admin/dashboard";
    }
}
