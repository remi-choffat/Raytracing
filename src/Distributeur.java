import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service qui permet d'enregistrer des nœuds et de fournir un nœud pour un calcul.
 */
public class Distributeur implements ServiceDistributeur {

    private final List<ServiceNoeud> nodes; // list des noeuds enregistres
    private int currentNodeIndex; // index du noeud courant

    public Distributeur() {
        this.nodes = new ArrayList<>();
        this.currentNodeIndex = 0; // commence avec le premier noeud
    }

    // methode pour enregistrer un nouveau noeud de calcul
    public synchronized void enregistrerNoeud(ServiceNoeud node) throws RemoteException {
        nodes.add(node);
        System.out.println("Nouveau noeud enregistre. Total: " + nodes.size());
    }

    @Override
    public ServiceNoeud donneMachine() throws RemoteException {
        if (nodes.isEmpty()) {
            throw new RemoteException("Aucun noeud de calcul disponible.");
        }

        int attempts = nodes.size();
        List<ServiceNoeud> nodesToRemove = new ArrayList<>();

        // chercher un noeud libre
        while (attempts > 0) {
            ServiceNoeud node = nodes.get(currentNodeIndex);
            try {
                if (node.estLibre()) {
                    currentNodeIndex = (currentNodeIndex + 1) % nodes.size();
                    return node;
                } else {
                    System.out.println("Noeud a l'index " + currentNodeIndex + " occupe, essayer le suivant.");
                    currentNodeIndex = (currentNodeIndex + 1) % nodes.size();
                    attempts--;
                }
            } catch (RemoteException e) {
                // noeud inaccessible, le marquer pour suppression
                nodesToRemove.add(node);
                System.out.println("Noeud inaccessible, marque pour suppression.");
                currentNodeIndex = (currentNodeIndex + 1) % nodes.size();
                attempts--;
            }
        }

        // delete les noeuds inaccessibles
        nodes.removeAll(nodesToRemove);
        if (nodes.isEmpty()) {
            throw new RemoteException("Aucun noeud disponible apres suppression des noeuds inaccessibles.");
        }

        // return le prochain noeud apres avoir epuise les attempts
        ServiceNoeud node = nodes.get(currentNodeIndex);
        currentNodeIndex = (currentNodeIndex + 1) % nodes.size();
        return node;
    }
}