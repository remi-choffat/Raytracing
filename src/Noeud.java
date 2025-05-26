package src;

import java.rmi.RemoteException;
import src.raytracer.*;

/**
 * Noeud
 */
public class Noeud implements ServiceNoeud {

	private boolean libre = true;

	public Image calculer(Scene scene, int x, int y, int w, int h) throws RemoteException {
		if (this.libre) {
			this.libre = false;
			Image r = scene.compute(x, y, w, h);
			this.libre = true;
			return r;
		} else {
			throw new RemoteException("MACHINE OCCUPEE");
		}
	}

	public boolean estLibre() throws RemoteException {
		return this.libre;
	}

	
}
