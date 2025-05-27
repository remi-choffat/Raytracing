import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface pour le service distributeur de machines de raytracing.
 * Cette interface définit la méthode pour obtenir une machine de calcul.
 */
public interface ServiceDistributeur extends Remote {

    /**
     * Enregistre un nœud de calcul dans le service distributeur.
     *
     * @param noeud le nœud de calcul à enregistrer
     * @throws RemoteException si une erreur de communication RMI se produit
     */
    void enregistrerNoeud(ServiceNoeud noeud) throws RemoteException;

    /**
     * Donne un nœud de calcul disponible pour effectuer un raytracing.
     *
     * @return un objet ServiceNoeud représentant le nœud de calcul
     * @throws RemoteException si une erreur de communication RMI se produit
     */
    ServiceNoeud donneMachine() throws RemoteException;
}
