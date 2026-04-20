package fr.boboetsescopains.service;

import fr.boboetsescopains.entity.Book;
import fr.boboetsescopains.entity.enums.BookStatus;
import fr.boboetsescopains.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final S3Service s3Service;

    /**
     * Get all books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Get all published books
     */
    public List<Book> getAllPublishedBooks() {
        return bookRepository.findAllPublishedBooks();
    }

    /**
     * Get featured published books
     */
    public List<Book> getFeaturedBooks() {
        return bookRepository.findAllFeaturedPublishedBooks();
    }

    /**
     * Get book by ID
     */
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Get book by slug
     */
    public Optional<Book> getBookBySlug(String slug) {
        return bookRepository.findBySlug(slug);
    }

    /**
     * Create a new book
     */
    public Book createBook(Book book, MultipartFile coverImage) throws IOException {
        // Generate slug from title if not provided
        if (book.getSlug() == null || book.getSlug().isEmpty()) {
            book.setSlug(generateSlug(book.getTitle()));
        }

        // Ensure unique slug
        String originalSlug = book.getSlug();
        int counter = 1;
        while (bookRepository.existsBySlug(book.getSlug())) {
            book.setSlug(originalSlug + "-" + counter);
            counter++;
        }

        // Upload cover image if provided
        if (coverImage != null && !coverImage.isEmpty()) {
            String s3Key = s3Service.uploadFile(coverImage, "books/covers");
            book.setCoverImageS3Key(s3Key);
            book.setCoverImageUrl(s3Service.getPublicUrl(s3Key));
        }

        book.setViewsCount(0L);
        Book savedBook = bookRepository.save(book);
        log.info("Book created: {}", savedBook.getTitle());
        return savedBook;
    }

    /**
     * Update an existing book
     */
    public Book updateBook(Long id, Book bookDetails, MultipartFile coverImage) throws IOException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'ID: " + id));

        // Update basic fields
        book.setTitle(bookDetails.getTitle());
        book.setShortDescription(bookDetails.getShortDescription());
        book.setFullDescription(bookDetails.getFullDescription());
        book.setPrice(bookDetails.getPrice());
        book.setAmazonLink(bookDetails.getAmazonLink());
        book.setEbookLink(bookDetails.getEbookLink());
        book.setPublishedDate(bookDetails.getPublishedDate());
        book.setStatus(bookDetails.getStatus());
        book.setDisplayOrder(bookDetails.getDisplayOrder());
        book.setFeatured(bookDetails.isFeatured());

        // Update slug if title changed
        if (!book.getSlug().equals(bookDetails.getSlug())) {
            String newSlug = bookDetails.getSlug();
            if (newSlug == null || newSlug.isEmpty()) {
                newSlug = generateSlug(bookDetails.getTitle());
            }

            // Ensure unique slug
            String originalSlug = newSlug;
            int counter = 1;
            while (bookRepository.existsBySlug(newSlug) && !newSlug.equals(book.getSlug())) {
                newSlug = originalSlug + "-" + counter;
                counter++;
            }
            book.setSlug(newSlug);
        }

        // Update cover image if provided
        if (coverImage != null && !coverImage.isEmpty()) {
            // Delete old image if exists
            if (book.getCoverImageS3Key() != null) {
                try {
                    s3Service.deleteFile(book.getCoverImageS3Key());
                } catch (Exception e) {
                    log.warn("Failed to delete old cover image: {}", e.getMessage());
                }
            }

            // Upload new image
            String s3Key = s3Service.uploadFile(coverImage, "books/covers");
            book.setCoverImageS3Key(s3Key);
            book.setCoverImageUrl(s3Service.getPublicUrl(s3Key));
        }

        Book updatedBook = bookRepository.save(book);
        log.info("Book updated: {}", updatedBook.getTitle());
        return updatedBook;
    }

    /**
     * Delete a book
     */
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'ID: " + id));

        // Delete cover image from S3 if exists
        if (book.getCoverImageS3Key() != null) {
            try {
                s3Service.deleteFile(book.getCoverImageS3Key());
            } catch (Exception e) {
                log.warn("Failed to delete cover image from S3: {}", e.getMessage());
            }
        }

        bookRepository.delete(book);
        log.info("Book deleted: {}", book.getTitle());
    }

    /**
     * Increment book views
     */
    public void incrementBookViews(Long id) {
        bookRepository.findById(id).ifPresent(book -> {
            book.incrementViews();
            bookRepository.save(book);
        });
    }

    /**
     * Change book status
     */
    public Book changeBookStatus(Long id, BookStatus newStatus) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'ID: " + id));

        book.setStatus(newStatus);
        Book updatedBook = bookRepository.save(book);
        log.info("Book status changed: {} -> {}", book.getTitle(), newStatus);
        return updatedBook;
    }

    /**
     * Toggle featured status
     */
    public Book toggleFeatured(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'ID: " + id));

        book.setFeatured(!book.isFeatured());
        Book updatedBook = bookRepository.save(book);
        log.info("Book featured status toggled: {} -> {}", book.getTitle(), book.isFeatured());
        return updatedBook;
    }

    /**
     * Get book count by status
     */
    public Long countByStatus(BookStatus status) {
        return bookRepository.countByStatus(status);
    }

    /**
     * Generate slug from title
     */
    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[àáâãäå]", "a")
                .replaceAll("[èéêë]", "e")
                .replaceAll("[ìíîï]", "i")
                .replaceAll("[òóôõö]", "o")
                .replaceAll("[ùúûü]", "u")
                .replaceAll("[ýÿ]", "y")
                .replaceAll("[ç]", "c")
                .replaceAll("[ñ]", "n")
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}
