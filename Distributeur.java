import java.rmi.RemoteException;
import java.util.List;

public class Distributeur implements ServiceDistributeur {

    private List<ServiceNoeud> nodes; // list des noeuds enregistres
    private int currentNodeIndex; // index du noeud courant

    // methode pour enregistrer un nouveau noeud de calcul
    public synchronized void enregistrerNoeud(ServiceNoeud node) throws RemoteException {
        nodes.add(node);
        System.out.println("Registered new node. Total: " + nodes.size());
    }

    @Override
    public ServiceNoeud donneMachine() throws RemoteException {
        if (nodes.isEmpty()) {
            throw new RemoteException("Aucun noeud de calcul disponible.");
        }

        return null; //to do
    }
}