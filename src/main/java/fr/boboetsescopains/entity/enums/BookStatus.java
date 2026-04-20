package fr.boboetsescopains.entity.enums;

public enum BookStatus {
    DRAFT("Brouillon"),
    PUBLISHED("Publié"),
    ARCHIVED("Archivé");

    private final String displayName;

    BookStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
