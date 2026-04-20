# GitHub Actions CI/CD Pipeline

Ce projet utilise GitHub Actions pour l'intégration continue et le déploiement continu (CI/CD).

## 🚀 Workflows Disponibles

### CI/CD Pipeline (`.github/workflows/ci.yml`)

Ce workflow s'exécute automatiquement sur :
- Push vers `main`, `develop`, ou toute branche `claude/**`
- Pull requests vers `main` ou `develop`

## 📋 Jobs du Pipeline

### 1. **Build and Test**
- ✅ Checkout du code
- ✅ Setup de JDK 21 (Temurin)
- ✅ Cache Maven et Node.js pour accélérer les builds
- ✅ Compilation du projet
- ✅ Exécution des tests unitaires
- ✅ Build du package JAR
- ✅ Upload des artifacts (JAR et rapports de tests)

### 2. **Code Quality**
- ✅ Analyse Checkstyle
- ✅ Vérification du code

### 3. **Security Scan**
- ✅ Scan OWASP Dependency Check
- ✅ Rapport de vulnérabilités

### 4. **Build Docker** (uniquement sur `main` et `develop`)
- ✅ Build de l'image Docker avec Jib
- ✅ Upload de l'image Docker

## 🔧 Configuration Requise

### Secrets GitHub (optionnels pour le build de base)

Aucun secret n'est requis pour le build et les tests de base. Cependant, pour un déploiement complet, vous pouvez configurer :

#### Pour AWS S3 (optionnel)
```
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_S3_BUCKET_NAME
AWS_S3_REGION
```

#### Pour Spring AI avec OpenAI (optionnel)
```
OPENAI_API_KEY
```

#### Pour Email (optionnel)
```
MAIL_HOST
MAIL_PORT
MAIL_USERNAME
MAIL_PASSWORD
```

### Configuration des Secrets

1. Allez dans **Settings** → **Secrets and variables** → **Actions**
2. Cliquez sur **New repository secret**
3. Ajoutez chaque secret avec son nom et sa valeur

## 📊 Rapports et Artifacts

Le workflow génère plusieurs artifacts téléchargeables :

| Artifact | Description | Rétention |
|----------|-------------|-----------|
| `boboetsescopains-webapp` | JAR de l'application | 7 jours |
| `test-results` | Rapports de tests Surefire | 7 jours |
| `security-report` | Rapport OWASP Dependency Check | 7 jours |
| `docker-image` | Image Docker compressée | 7 jours |

## 🎯 Statut du Build

Vous pouvez ajouter un badge de statut dans votre README principal :

```markdown
![CI/CD Pipeline](https://github.com/fhabumugisha/boboetsescopains.fr/workflows/CI%2FCD%20Pipeline/badge.svg)
```

## 🔄 Workflow Local

Pour tester le workflow localement avant de push :

### Compilation
```bash
mvn clean compile
```

### Tests
```bash
mvn test
```

### Package
```bash
mvn package
```

### Build Docker
```bash
mvn compile jib:dockerBuild
```

## 📦 Cache Strategy

Le workflow utilise deux niveaux de cache :

1. **Maven Dependencies** : Cache de `~/.m2/repository`
2. **Node.js Modules** : Cache de `node` et `node_modules`

Cela accélère significativement les builds successifs.

## ⚙️ Configuration du Projet pour Tests

Le projet utilise un profil Spring Boot `test` avec une base de données H2 in-memory :

```yaml
# application-test.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
  flyway:
    enabled: false
```

Cela permet de :
- ✅ Tester sans PostgreSQL
- ✅ Tester sans AWS S3
- ✅ Tester sans serveur SMTP
- ✅ Build rapide sur GitHub Actions

## 🐛 Troubleshooting

### Le build échoue sur la compilation

Vérifiez que :
- Java 21 est bien utilisé
- Les dépendances Maven sont accessibles
- Le repository `spring-milestones` est configuré pour Spring Boot 3.5.7

### Les tests échouent

- Vérifiez `application-test.yml`
- Assurez-vous que H2 est dans les dépendances de test
- Les tests utilisent `@ActiveProfiles("test")`

### Le build Docker échoue

- Vérifiez la configuration Jib dans `pom.xml`
- Assurez-vous que Docker est disponible sur le runner

## 📝 Personnalisation

### Ajouter un nouveau job

Ajoutez dans `.github/workflows/ci.yml` :

```yaml
mon-nouveau-job:
  name: Mon Nouveau Job
  runs-on: ubuntu-latest
  needs: build-and-test  # Dépend du job build-and-test

  steps:
    - name: Checkout
      uses: actions/checkout@v4

    - name: Ma tâche
      run: echo "Hello World"
```

### Changer les branches déclencheurs

Modifiez la section `on:` :

```yaml
on:
  push:
    branches: [ main, develop, ma-branche ]
  pull_request:
    branches: [ main ]
```

## 🚀 Déploiement

Pour activer le déploiement automatique, ajoutez un job `deploy` :

```yaml
deploy:
  name: Deploy to Production
  runs-on: ubuntu-latest
  needs: [build-and-test, code-quality, security-scan]
  if: github.ref == 'refs/heads/main'

  steps:
    - name: Deploy to GCP Cloud Run
      run: |
        # Vos commandes de déploiement
```

## 📚 Ressources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Spring Boot CI/CD](https://spring.io/guides/gs/spring-boot-kubernetes/)
- [Jib Maven Plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin)
