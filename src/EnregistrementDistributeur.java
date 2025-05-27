import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Enregistre le service de distribution des nœuds de calcul.
 */
public class EnregistrementDistributeur {

    public static void main(String[] args) {

        try {
            Distributeur distributeurNoeudsCalcul = new Distributeur();
            ServiceDistributeur sd = (ServiceDistributeur) UnicastRemoteObject.exportObject(distributeurNoeudsCalcul, 0);
            Registry reg_local = LocateRegistry.getRegistry("localhost");
            reg_local.rebind("DistributeurRaytracer", sd);
            System.out.println("Service de distribution des noeuds de calcul enregistré.");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'enregistrement du service de distribution des noeuds de calcul : " + e.getMessage());
        }

    }

}
