/**
 * Enumération des états possibles d'un nœud
 */
public enum EtatNoeud {

    /**
     * Le nœud est libre et peut accepter des tâches.
     */
    LIBRE,

    /**
     * Le nœud est réservé par un client pour une tâche future, mais n'est pas encore occupé.
     */
    RESERVE,

    /**
     * Le nœud est actuellement occupé par une tâche en cours de traitement.
     */
    OCCUPE

}
