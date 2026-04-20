package fr.boboetsescopains.entity.enums;

public enum RoleName {
    ROLE_USER("Utilisateur"),
    ROLE_ADMIN("Administrateur");

    private final String displayName;

    RoleName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
