package fr.boboetsescopains.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Le slug est obligatoire")
    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String fullDescription;

    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl;

    @Column(name = "cover_image_s3_key", length = 500)
    private String coverImageS3Key;

    @Positive(message = "Le prix doit être positif")
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "amazon_link", length = 500)
    private String amazonLink;

    @Column(name = "ebook_link", length = 500)
    private String ebookLink;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private BookStatus status = BookStatus.DRAFT;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(nullable = false)
    @Builder.Default
    private boolean featured = false;

    @Column(name = "views_count")
    @Builder.Default
    private Long viewsCount = 0L;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum BookStatus {
        DRAFT,
        PUBLISHED,
        ARCHIVED
    }

    public void incrementViews() {
        this.viewsCount = (this.viewsCount == null ? 0 : this.viewsCount) + 1;
    }

    public boolean isPublished() {
        return this.status == BookStatus.PUBLISHED;
    }
}
