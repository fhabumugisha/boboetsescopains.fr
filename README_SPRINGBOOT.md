# Bobo et ses Copains - Application Spring Boot

Application web moderne pour "Bobo et ses copains" construite avec Spring Boot 3.5.7, Thymeleaf, HTMX, Tailwind CSS 4 et Alpine.js.

## 🛠️ Stack Technique

### Backend
- **Spring Boot 3.5.7** avec Java 21
- **Thymeleaf** pour le templating
- **Spring Security** pour l'authentification et l'autorisation
- **Spring Data JPA** avec PostgreSQL (Neon)
- **AWS SDK S3** pour le stockage des images
- **Spring Mail** pour les emails (reset password)
- **Lombok** pour réduire le boilerplate

### Frontend
- **Tailwind CSS 4** (beta) pour le styling
- **DaisyUI** pour les composants UI
- **HTMX 2.0** pour les interactions dynamiques
- **Alpine.js 3.x** pour l'interactivité JavaScript
- **WebJars** pour la gestion des dépendances frontend

### DevOps
- **Maven** avec Frontend Maven Plugin
- **Jib** pour la conteneurisation (GCP Cloud Run)
- **PostgreSQL** (Neon) comme base de données

## 📋 Prérequis

- Java 21 JDK
- Maven 3.8+
- PostgreSQL (ou compte Neon)
- Compte AWS S3 (pour le stockage des images)
- Compte GCP (pour le déploiement Cloud Run)

## 🚀 Installation et Configuration

### 1. Cloner le projet

```bash
git clone https://github.com/fhabumugisha/boboetsescopains.fr.git
cd boboetsescopains.fr
```

### 2. Configuration de la Base de Données (Neon)

Créez un compte sur [Neon](https://neon.tech) et créez une base de données PostgreSQL.

Mettez à jour `application.yml` ou créez un fichier `.env`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://your-neon-host.neon.tech:5432/your-database
    username: your-username
    password: your-password
```

Ou via variables d'environnement:

```bash
export DATABASE_URL=jdbc:postgresql://your-neon-host.neon.tech:5432/your-database
export DATABASE_USERNAME=your-username
export DATABASE_PASSWORD=your-password
```

### 3. Configuration AWS S3

Créez un bucket S3 et configurez les credentials:

```bash
export AWS_S3_BUCKET_NAME=boboetsescopains-images
export AWS_S3_REGION=eu-west-3
export AWS_ACCESS_KEY_ID=your-access-key
export AWS_SECRET_ACCESS_KEY=your-secret-key
```

### 4. Configuration Email (pour reset password)

Configurez un serveur SMTP (Gmail, SendGrid, etc.):

```bash
export MAIL_HOST=smtp.gmail.com
export MAIL_PORT=587
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password
```

### 5. Installation des dépendances et Build

Le Frontend Maven Plugin va automatiquement installer Node.js et npm, puis builder Tailwind CSS:

```bash
# Installation et build complet
mvn clean install

# Ou en mode développement
mvn spring-boot:run
```

L'application sera disponible sur: `http://localhost:8080`

## 🎨 Développement Frontend

### Watch mode pour Tailwind CSS

En mode développement, vous pouvez lancer Tailwind en watch mode:

```bash
npm run watch:css
```

### Build production CSS

```bash
npm run build:css
```

## 🏗️ Structure du Projet

```
boboetsescopains.fr/
├── src/
│   ├── main/
│   │   ├── java/fr/boboetsescopains/
│   │   │   ├── config/              # Configurations (Security, S3, etc.)
│   │   │   ├── controller/          # Controllers Spring MVC
│   │   │   ├── entity/              # Entités JPA
│   │   │   ├── repository/          # Repositories Spring Data
│   │   │   ├── service/             # Services métier
│   │   │   └── BoboEtSesCopains Application.java
│   │   │
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   ├── input.css    # Source Tailwind
│   │       │   │   └── output.css   # CSS compilé
│   │       │   ├── js/
│   │       │   └── images/
│   │       │
│   │       ├── templates/
│   │       │   ├── layout/
│   │       │   │   └── base.html
│   │       │   ├── fragments/
│   │       │   │   ├── header.html
│   │       │   │   └── footer.html
│   │       │   └── index.html
│   │       │
│   │       └── application.yml
│   │
│   └── test/
│       └── java/fr/boboetsescopains/
│
├── pom.xml
├── package.json
├── tailwind.config.js
└── README_SPRINGBOOT.md
```

## 🔐 Fonctionnalités de Sécurité

- ✅ Authentification avec Spring Security
- ✅ Gestion des rôles (USER, ADMIN)
- ✅ Inscription des utilisateurs
- ✅ Reset de mot de passe par email
- ✅ Protection CSRF
- ✅ Validation des entrées

## 🐳 Conteneurisation avec Jib

### Build et push vers GCP Container Registry

```bash
# Configuration du projet GCP
export GCP_PROJECT_ID=your-project-id

# Build et push l'image
mvn compile jib:build \
  -Djib.to.image=gcr.io/${GCP_PROJECT_ID}/boboetsescopains:latest
```

### Build vers Docker local

```bash
mvn compile jib:dockerBuild
```

## ☁️ Déploiement sur GCP Cloud Run

### Prérequis
- Google Cloud SDK installé
- Projet GCP créé
- Cloud Run API activée

### Déploiement

```bash
# 1. Build et push l'image
mvn compile jib:build -Djib.to.image=gcr.io/${GCP_PROJECT_ID}/boboetsescopains:latest

# 2. Déployer sur Cloud Run
gcloud run deploy boboetsescopains \
  --image gcr.io/${GCP_PROJECT_ID}/boboetsescopains:latest \
  --platform managed \
  --region europe-west1 \
  --allow-unauthenticated \
  --set-env-vars="DATABASE_URL=...,AWS_S3_BUCKET_NAME=...,AWS_ACCESS_KEY_ID=...,AWS_SECRET_ACCESS_KEY=..."
```

### Variables d'environnement Cloud Run

Configurez les variables d'environnement dans Cloud Run:

- `DATABASE_URL`
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`
- `AWS_S3_BUCKET_NAME`
- `AWS_S3_REGION`
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `MAIL_HOST`
- `MAIL_USERNAME`
- `MAIL_PASSWORD`

## 📦 WebJars

Les dépendances frontend sont gérées via WebJars:

- HTMX 2.0.4: `/webjars/htmx.org/2.0.4/dist/htmx.min.js`
- Alpine.js 3.14.1: `/webjars/alpinejs/3.14.1/dist/cdn.min.js`

## 🧪 Tests

```bash
# Lancer tous les tests
mvn test

# Tests avec couverture
mvn test jacoco:report
```

## 📝 Tailwind CSS 4

Le projet utilise Tailwind CSS 4 (beta) avec configuration CSS:

```css
@import "tailwindcss";

@theme {
  --color-primary: #0d6efd;
  --color-secondary: #6c757d;
}
```

## 🎯 Routes Principales

- `/` - Page d'accueil (liste des livres)
- `/admin` - Dashboard admin (authentification requise)
- `/login` - Page de connexion
- `/register` - Inscription
- `/forgot-password` - Reset de mot de passe

## 🔧 Configuration de Développement

### application.yml (dev)

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  thymeleaf:
    cache: false
  devtools:
    restart:
      enabled: true
```

## 📚 Documentation Utile

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Tailwind CSS 4 Beta](https://tailwindcss.com/blog/tailwindcss-v4-beta)
- [HTMX Documentation](https://htmx.org/docs/)
- [Alpine.js Documentation](https://alpinejs.dev/)
- [DaisyUI Components](https://daisyui.com/components/)
- [Jib Documentation](https://github.com/GoogleContainerTools/jib)
- [GCP Cloud Run Documentation](https://cloud.google.com/run/docs)

## 🤝 Contribution

1. Fork le projet
2. Créez votre branche (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## 📄 Licence

Ce projet est sous licence privée.

## 👥 Auteurs

- **F. Habumugisha** - *Initial work*

## 🙏 Remerciements

- Spring Boot team
- Tailwind CSS team
- HTMX team
- Alpine.js team
