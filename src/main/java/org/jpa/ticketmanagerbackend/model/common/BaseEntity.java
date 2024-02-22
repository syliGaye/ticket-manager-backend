package org.jpa.ticketmanagerbackend.model.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Optional;

/**
 * Classe abstraite représentant une entité de base.
 * Les classes entités concrètes doivent étendre cette classe.
 */
@MappedSuperclass
public abstract class BaseEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseEntity.class);

    /**
     * Identifiant unique de l'entité.
     */
    @Column(name = "id", nullable = false)
    private long id;

    /**
     * Indicateur d'activité de l'entité.
     */
    @Column(name = "active")
    private boolean active;

    /**
     * Constructeur par défaut de l'entité de base.
     */
    public BaseEntity() {
    }

    /**
     * Obtient l'identifiant unique de l'entité.
     *
     * @return L'identifiant unique de l'entité.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    /**
     * Définit l'identifiant unique de l'entité.
     *
     * @param id Le nouvel identifiant unique de l'entité.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Obtient l'indicateur d'activité de l'entité.
     *
     * @return L'indicateur d'activité de l'entité.
     */
    public boolean getActive() {
        return active;
    }

    /**
     * Définit l'indicateur d'activité de l'entité.
     *
     * @param active Le nouvel indicateur d'activité de l'entité.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    protected String toJSON() {
        try {
            final String result = new ObjectMapper().writeValueAsString(this);
            return result.replace("'", "\\\'");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "";
    }

    @PrePersist
    protected void onPrePersist() {
        try {
            Optional.of(
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest()
            );

        } catch (IllegalStateException e) {
            LOGGER.error(String.format("IllegalStateException --> %s", Arrays.toString(e.getStackTrace())));
        }

    }

    @PreUpdate
    protected void onPreUpdate() {
        try {
            Optional.of(
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                            .getRequest()
            );
        } catch (IllegalStateException e) {
            LOGGER.error("IllegalStateException / onPreUpdate", Arrays.toString(e.getStackTrace()));
        }
    }
}
