package src;

import src.raytracer.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface pour le service de calcul d'image par raytracing.
 * Cette interface définit la méthode pour calculer une image à partir d'une scène.
 */
public interface ServiceNoeud extends Remote {
    Image calculer(Scene scene, int x, int y, int w, int h) throws RemoteException;

    boolean estLibre() throws RemoteException;
}
