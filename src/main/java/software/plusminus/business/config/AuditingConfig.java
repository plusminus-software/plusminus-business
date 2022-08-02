package software.plusminus.business.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import software.plusminus.security.configs.SecurityAuditorAware;

import java.time.ZonedDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider", 
        auditorAwareRef = "auditorProvider")
public class AuditingConfig {

    @Bean
    public DateTimeProvider dateTimeProvider() {
        /* Makes ZonedDateTime compatible with auditing fields */
        return () -> Optional.of(ZonedDateTime.now());
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SecurityAuditorAware();
    }
}
