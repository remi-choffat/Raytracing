import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerNoeud {

    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("Missing parameter, Usage -> java LancerNoeud [Local Registry] [Local Port] [Distibuteur IP] [Distibuteur Port]");
            return;
        }
        Registry reg;
        try {
            reg = LocateRegistry.getRegistry(args[2], Integer.parseInt(args[3]));
        } catch (Exception e) {
            System.err.println("Erreur, registry du distributeur pas trouve a l'adresse' " + args[2] + ":" + args[3]);
            return;
        }

        ServiceDistributeur sd;

        try {
            sd = (ServiceDistributeur) reg.lookup("DistributeurRaytracer");
        } catch (NotBoundException e) {
            System.err.println("Erreur, ServiceDistributeur pas trouve dans le registry");
            return;
        } catch (Exception e) {
            return;
        }


        Registry loc;
        try {
            loc = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
        } catch (Exception e) {
            System.err.println("Erreur, registry local pas trouve a l'adresse' " + args[0] + ":" + args[1]);
            return;
        }


        ServiceNoeud sn = new Noeud(sd);

        try {
            loc.rebind("NoeudCalcul", (ServiceNoeud) UnicastRemoteObject.exportObject(sn, 0));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }


        try {
            sd.enregistrerNoeud(sn);
            System.out.println("Noeud Opérationnel");
        } catch (RemoteException e) {
            System.err.println("Erreur a l'enregistrement du noeud dans le distributeur'");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
