import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.Serializable;
import raytracer.Scene;
import raytracer.Image;
import java.rmi.server.UnicastRemoteObject;


/**
 * Noeud
 */
public class Noeud implements ServiceNoeud, Serializable {

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
		if (args.length < 4) {
			System.err.println("Missing parameter, Usage -> java Noeud [Local Registry] [Local Port] [Distibuteur IP] [Distibuteur Port]");
			return;
		} 
		Registry reg;
		try {
			reg = LocateRegistry.getRegistry(args[2], Integer.parseInt(args[3]));
		} catch (Exception e) {
			System.err.println("Erreur, registry du distributeur pas trouve a l'adresse' " + args[2] + ":" + args[3]);
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
		

		Registry loc;
		try {
			loc = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
		} catch (Exception e) {
			System.err.println("Erreur, registry local pas trouve a l'adresse' " + args[0] + ":" + args[1]);
			e.printStackTrace();
			return;
		}

		
		ServiceNoeud sn = new Noeud();

		try {
			loc.bind("NoeudCalcul", (ServiceNoeud)UnicastRemoteObject.exportObject(sn, 0));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}




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
