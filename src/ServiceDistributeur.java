package src;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface pour le service distributeur de machines de raytracing.
 * Cette interface définit la méthode pour obtenir une machine de calcul.
 */
public interface ServiceDistributeur extends Remote {
    ServiceNoeud donneMachine() throws RemoteException;
}
