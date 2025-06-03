import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.Serializable;

import raytracer.Scene;
import raytracer.Image;

import java.rmi.server.UnicastRemoteObject;


/**
 * Noeud de calcul
 */
public class Noeud implements ServiceNoeud, Serializable {

    /**
     * Service distributeur auquel ce noeud est enregistré.
     */
    private final ServiceDistributeur distributeur;

    public Noeud(ServiceDistributeur distributeur) {
        this.distributeur = distributeur;
    }


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
    public synchronized Image calculer(Scene scene, int x, int y, int w, int h) throws RemoteException {
        // Avertir le distributeur que ce noeud est en train de faire un calcul
        distributeur.noeudAvertirCalcul(this);
        // Calcul de l'image
        System.out.format(">> Calcul de dimension (w=%d, h=%d) en (x=%d, y=%d)\n", w, h, x, h);
        Image r = scene.compute(x, y, w, h);
        System.out.format("<< Calcul terminé\n");
        // Avertir le distributeur que ce noeud est libre
        distributeur.noeudAvertirLibre(this);
        // Retourner l'image calculée
        return r;
    }


    /**
     * Indique au distributeur que ce noeud est toujours actif.
     *
     * @throws RemoteException Si une erreur de communication RMI se produit.
     */
    public void jeSuisLa() throws RemoteException {
    }

}
