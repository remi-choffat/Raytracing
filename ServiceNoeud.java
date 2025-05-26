import raytracer.Image;
import raytracer.Scene;

import java.rmi.Remote;
import java.rmi.RemoteException;

<<<<<<< HEAD
public interface ServiceNoeud  extends Remote{
	public Image calculer(Scene scene, int x, int y, int w, int h) throws RemoteException;
	public boolean estLibre() throws RemoteException;
=======
/**
 * Interface pour le service de calcul d'image par raytracing.
 * Cette interface définit la méthode pour calculer une image à partir d'une scène.
 */
public interface ServiceNoeud extends Remote {
    Image calculer(Scene scene, int x, int y, int w, int h) throws RemoteException;
>>>>>>> 846deb1eb00b8076833ed48a4c39e379b15df2b3
}
