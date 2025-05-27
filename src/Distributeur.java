import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service qui permet d'enregistrer des nœuds et de fournir un nœud pour un calcul.
 */
public class Distributeur implements ServiceDistributeur {

    private final List<ServiceNoeud> nodes; // list des noeuds enregistres

    public Distributeur() {
        this.nodes = new ArrayList<>();
    }

    // methode pour enregistrer un nouveau noeud de calcul
    public synchronized void enregistrerNoeud(ServiceNoeud node) throws RemoteException {
        nodes.add(node);
        System.out.println("Nouveau noeud enregistre. Total : " + nodes.size());
    }

    @Override
    public synchronized ServiceNoeud donneMachine() throws RemoteException {

        if (nodes.isEmpty()) {
            throw new RemoteException("Aucun noeud de calcul enregistré.");
        }

        List<ServiceNoeud> nodesToRemove = new ArrayList<>();

        // chercher un noeud libre
        for (ServiceNoeud node : nodes) {
            try {
                if (node.estLibre()) {
                    return node;
                }
            } catch (RemoteException e) {
                // noeud inaccessible, le marquer pour suppression
                nodesToRemove.add(node);
                System.out.println("Noeud inaccessible, marque pour suppression.");
            }
        }

        // delete les noeuds inaccessibles
        nodes.removeAll(nodesToRemove);
        if (nodes.isEmpty()) {
            throw new RemoteException("Aucun noeud disponible.");
        }

        return donneMachine();
    }
}