package org.jpa.ticketmanagerbackend.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseDao <T, A extends Serializable> {
    /**
     * Rend persistant un nouvel objet.
     * <p>
     * Dans le cas des identifiants auto-incrémentés et après l'appel à cette méthode, l'objet aura automatiquement son identifiant affecté.
     * </p>
     * @param model Nouvel objet à rendre persistant (requis).
     */
    T save(T model);

    /**
     * Recherche et retourne un objet persistant suivant son <u>identifiant technique</u>.
     * @param id Identifiant technique de l'objet persistant recherché (requis).
     * @return Objet persistant trouvé ou <code>NULL</code> s'il n'a pas été trouvé.
     */
    Optional<T> findById(A id);

    /**
     * Recherche et retourne <u>tous</u> les objets persistants gérés par la DAO.
     * <p>
     * Cette fonction est <u>à utiliser avec prudence</u>, pour des objets persistants dont on sait qu'ils ne seront pas nombreux et que leurs nombre n'augmentera
     * pas avec le temps.
     * </p>
     * @return Liste des objets persistants trouvés (jamais <code>NULL</code>).
     */
    List<T> findAll();

    List<T> findAll(int startPosition, int maxResult);

    /**
     * rend inactif un objet persistant à partir de identifiant.
     * @param id Identifiant de l'objet persistant (requis).
     */
    void deleteById(A id);

    /**
     * Rend inactif un objet persistent.
     * @param model Object persistent à rendre inactif (requis).
     */
    void delete(T model);

    /**
     * Compte et retourne le nombre de <u>tous</u> les objets persistants gérés par la DAO.
     * @return Nombre d'objets persistants trouvés.
     */
    long count();
}
