package fr.boboetsescopains.controller.admin;

import fr.boboetsescopains.entity.enums.BookStatus;

import fr.boboetsescopains.entity.Book;
// import fr.boboetsescopains.service.AIService;
import fr.boboetsescopains.service.BookService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/books")
// @RequiredArgsConstructor
@Slf4j
public class AdminBookController {

    private final BookService bookService;
    // private final AIService aiService;

    public AdminBookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * List all books
     */
    @GetMapping
    public String listBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        model.addAttribute("publishedCount", bookService.countByStatus(BookStatus.PUBLISHED));
        model.addAttribute("draftCount", bookService.countByStatus(BookStatus.DRAFT));
        return "admin/books/list";
    }

    /**
     * Show create book form
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("isEdit", false);
        return "admin/books/form";
    }

    /**
     * Create new book
     */
    @PostMapping
    public String createBook(
            @Valid @ModelAttribute Book book,
            BindingResult result,
            @RequestParam(value = "coverImageFile", required = false) MultipartFile coverImageFile,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/books/form";
        }

        try {
            bookService.createBook(book, coverImageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Livre créé avec succès!");
            return "redirect:/admin/books";
        } catch (IOException e) {
            log.error("Error creating book", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la création du livre: " + e.getMessage());
            return "redirect:/admin/books/new";
        }
    }

    /**
     * Show edit book form
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé"));

        model.addAttribute("book", book);
        model.addAttribute("isEdit", true);
        return "admin/books/form";
    }

    /**
     * Update book
     */
    @PostMapping("/{id}")
    public String updateBook(
            @PathVariable Long id,
            @Valid @ModelAttribute Book book,
            BindingResult result,
            @RequestParam(value = "coverImageFile", required = false) MultipartFile coverImageFile,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/books/form";
        }

        try {
            bookService.updateBook(id, book, coverImageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Livre mis à jour avec succès!");
            return "redirect:/admin/books";
        } catch (IOException e) {
            log.error("Error updating book", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour du livre: " + e.getMessage());
            return "redirect:/admin/books/" + id + "/edit";
        }
    }

    /**
     * Delete book (HTMX)
     */
    @HxRequest
    @DeleteMapping("/{id}")
    public HtmxResponse deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            return HtmxResponse.builder()
                    .redirect("/admin/books")
                    .build();
        } catch (Exception e) {
            log.error("Error deleting book", e);
            return HtmxResponse.builder()
                    .redirect("/admin/books?error=delete")
                    .build();
        }
    }

    /**
     * Toggle featured status (HTMX)
     */
    @HxRequest
    @PostMapping("/{id}/toggle-featured")
    public String toggleFeatured(@PathVariable Long id, Model model) {
        Book book = bookService.toggleFeatured(id);
        model.addAttribute("book", book);
        return "admin/books/fragments :: book-row";
    }

    /**
     * Change book status (HTMX)
     */
    @HxRequest
    @PostMapping("/{id}/status")
    public String changeStatus(
            @PathVariable Long id,
            @RequestParam BookStatus status,
            Model model) {

        Book book = bookService.changeBookStatus(id, status);
        model.addAttribute("book", book);
        return "admin/books/fragments :: book-row";
    }

    /*
     * AI features temporarily disabled
     * Uncomment when Spring AI is enabled

    @HxRequest
    @PostMapping("/generate-description")
    @ResponseBody
    public String generateDescription(
            @RequestParam String title,
            @RequestParam(required = false, defaultValue = "") String keywords) {

        return aiService.generateBookDescription(title, keywords);
    }

    @HxRequest
    @PostMapping("/improve-description")
    @ResponseBody
    public String improveDescription(@RequestParam String description) {
        return aiService.improveDescription(description);
    }
    */
}
