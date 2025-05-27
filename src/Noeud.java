import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import raytracer.Scene;
import raytracer.Image;

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


	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Missing parameter, Usage -> java Noeud [Distibuteur IP] [Distibuteur Port]");
			return;
		} 
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
		} catch (Exception e) {
			System.err.println("Erreur, registry pas trouve a l'adresse' " + args[0] + ":" + args[1]);
			e.printStackTrace();
			return;
		}

		ServiceDistributeur sd;

		try {
			sd = (ServiceDistributeur) reg.lookup("DistributeurRaytracer");
		} catch (NotBoundException e) {
			System.err.println("Erreur, ServiceDistributeur pas trouve dans le registry");
			e.printStackTrace();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		ServiceNoeud sn = new Noeud();

		try {
			sd.enregistrerNoeud(sn);
		} catch (RemoteException e) {
			System.err.println("Erreur a l'enregistrement du noeud dans le distributeur'");
			e.printStackTrace();
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	
}
