package fr.boboetsescopains.repository;

import fr.boboetsescopains.entity.Book;
import fr.boboetsescopains.entity.enums.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findBySlug(String slug);

    List<Book> findByStatusOrderByDisplayOrderAsc(BookStatus status);

    List<Book> findByFeaturedTrueAndStatusOrderByDisplayOrderAsc(BookStatus status);

    @Query("SELECT b FROM Book b WHERE b.status = 'PUBLISHED' ORDER BY b.displayOrder ASC, b.createdAt DESC")
    List<Book> findAllPublishedBooks();

    @Query("SELECT b FROM Book b WHERE b.status = 'PUBLISHED' AND b.featured = true ORDER BY b.displayOrder ASC")
    List<Book> findAllFeaturedPublishedBooks();

    boolean existsBySlug(String slug);

    Long countByStatus(BookStatus status);
}
