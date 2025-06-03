import raytracer.Image;
import raytracer.Scene;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface pour le service de calcul d'image par raytracing.
 * Cette interface définit la méthode pour calculer une image à partir d'une scène.
 */
public interface ServiceNoeud extends Remote {

    /**
     * Calcule une image pour une scène donnée dans une zone spécifiée.
     *
     * @param scene La scène à calculer.
     * @param x     La coordonnée x du coin supérieur gauche de la zone.
     * @param y     La coordonnée y du coin supérieur gauche de la zone.
     * @param w     La largeur de la zone à calculer.
     * @param h     La hauteur de la zone à calculer.
     * @return L'image calculée pour la zone spécifiée.
     * @throws RemoteException Si une erreur de communication RMI se produit.
     */
    Image calculer(Scene scene, int x, int y, int w, int h) throws RemoteException;

    /**
     * Indique au distributeur que ce noeud est toujours actif.
     * Cette méthode est utilisée pour vérifier la disponibilité du noeud.
     *
     * @throws RemoteException Si une erreur de communication RMI se produit.
     */
    void jeSuisLa() throws RemoteException;
}
