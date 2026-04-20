package fr.boboetsescopains.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIService {

    private final ChatModel chatModel;

    /**
     * Generate book description using AI
     */
    public String generateBookDescription(String title, String keywords) {
        try {
            String prompt = String.format("""
                Génère une description engageante et professionnelle pour un livre pour enfants intitulé "%s".

                Mots-clés: %s

                La description doit :
                - Être concise (2-3 phrases)
                - Être adaptée aux enfants et leurs parents
                - Être en français
                - Mettre en valeur les valeurs éducatives et morales
                - Être enthousiaste et positive

                Ne pas inclure de formatage HTML, seulement du texte brut.
                """, title, keywords);

            ChatClient chatClient = ChatClient.builder(chatModel)
                    .defaultAdvisors(new SimpleLoggerAdvisor())
                    .build();

            String description = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            log.info("Generated description for book: {}", title);
            return description;
        } catch (Exception e) {
            log.error("Failed to generate description with AI: {}", e.getMessage());
            return "Description à compléter...";
        }
    }

    /**
     * Generate SEO-friendly meta description
     */
    public String generateMetaDescription(String title, String shortDescription) {
        try {
            String prompt = String.format("""
                Génère une méta-description SEO pour un livre pour enfants.

                Titre: %s
                Description courte: %s

                La méta-description doit :
                - Faire entre 150-160 caractères
                - Inclure un appel à l'action
                - Être optimisée pour le SEO
                - Être en français

                Retourne uniquement la méta-description, sans formatage.
                """, title, shortDescription);

            ChatClient chatClient = ChatClient.builder(chatModel)
                    .defaultAdvisors(new SimpleLoggerAdvisor())
                    .build();

            String metaDescription = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            log.info("Generated meta description for book: {}", title);
            return metaDescription;
        } catch (Exception e) {
            log.error("Failed to generate meta description with AI: {}", e.getMessage());
            return shortDescription;
        }
    }

    /**
     * Improve existing description
     */
    public String improveDescription(String currentDescription) {
        try {
            String prompt = String.format("""
                Améliore cette description de livre pour enfants en la rendant plus engageante et professionnelle :

                "%s"

                Instructions :
                - Garde le sens général
                - Améliore le style et la fluidité
                - Rends-la plus captivante
                - Reste concis (2-3 phrases maximum)
                - En français

                Retourne uniquement la description améliorée.
                """, currentDescription);

            ChatClient chatClient = ChatClient.builder(chatModel)
                    .defaultAdvisors(new SimpleLoggerAdvisor())
                    .build();

            String improvedDescription = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            log.info("Improved description using AI");
            return improvedDescription;
        } catch (Exception e) {
            log.error("Failed to improve description with AI: {}", e.getMessage());
            return currentDescription;
        }
    }
}
