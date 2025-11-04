package fr.boboetsescopains.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle resource not found exceptions
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request,
            Model model) {

        log.error("Resource not found: {} at {}", ex.getMessage(), request.getRequestURI());

        model.addAttribute("error", "Ressource non trouvée");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("path", request.getRequestURI());

        return "error/404";
    }

    /**
     * Handle no handler found (404)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFound(
            NoHandlerFoundException ex,
            HttpServletRequest request,
            Model model) {

        log.error("No handler found for: {}", request.getRequestURI());

        model.addAttribute("error", "Page non trouvée");
        model.addAttribute("message", "La page que vous recherchez n'existe pas");
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("path", request.getRequestURI());

        return "error/404";
    }

    /**
     * Handle access denied exceptions
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request,
            Model model) {

        log.error("Access denied: {} at {}", ex.getMessage(), request.getRequestURI());

        model.addAttribute("error", "Accès refusé");
        model.addAttribute("message", "Vous n'avez pas les permissions nécessaires pour accéder à cette ressource");
        model.addAttribute("status", HttpStatus.FORBIDDEN.value());
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("path", request.getRequestURI());

        return "error/403";
    }

    /**
     * Handle authentication exceptions
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request,
            Model model) {

        log.error("Authentication error: {} at {}", ex.getMessage(), request.getRequestURI());

        model.addAttribute("error", "Authentification requise");
        model.addAttribute("message", "Vous devez être connecté pour accéder à cette ressource");
        model.addAttribute("status", HttpStatus.UNAUTHORIZED.value());
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("path", request.getRequestURI());

        return "error/401";
    }

    /**
     * Handle file upload size exceeded
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public String handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request,
            Model model) {

        log.error("File upload size exceeded at: {}", request.getRequestURI());

        model.addAttribute("error", "Fichier trop volumineux");
        model.addAttribute("message", "La taille du fichier dépasse la limite autorisée (max 10MB)");
        model.addAttribute("status", HttpStatus.PAYLOAD_TOO_LARGE.value());
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("path", request.getRequestURI());

        return "error/error";
    }

    /**
     * Handle illegal argument exceptions
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request,
            Model model) {

        log.error("Illegal argument: {} at {}", ex.getMessage(), request.getRequestURI());

        model.addAttribute("error", "Argument invalide");
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("path", request.getRequestURI());

        return "error/error";
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGlobalException(
            Exception ex,
            HttpServletRequest request,
            Model model) {

        log.error("Internal server error at {}: ", request.getRequestURI(), ex);

        model.addAttribute("error", "Erreur interne du serveur");
        model.addAttribute("message", "Une erreur inattendue s'est produite. Veuillez réessayer plus tard.");
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("path", request.getRequestURI());

        // Only show detailed error in development
        if (log.isDebugEnabled()) {
            model.addAttribute("debug", ex.getMessage());
        }

        return "error/error";
    }
}
