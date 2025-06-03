import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service qui permet d'enregistrer des nœuds et de fournir un nœud pour un calcul.
 */
public class Distributeur implements ServiceDistributeur {

    /**
     * Liste des noeuds enregistrés avec leur état et le timestamp de la dernière activité.
     */
    private final Map<ServiceNoeud, Map.Entry<EtatNoeud, Long>> noeuds; // list des noeuds enregistres

    /**
     * Délai d'attente avant de demander une nouvelle confirmation d'état d'un nœud.
     */
    private static final long timeout = 30000;

    public Distributeur() {
        this.noeuds = new HashMap<>();
    }

    // methode pour enregistrer un nouveau noeud de calcul
    public synchronized void enregistrerNoeud(ServiceNoeud node) throws RemoteException {
        noeuds.put(node, Map.entry(EtatNoeud.LIBRE, System.currentTimeMillis()));
        System.out.println("Nouveau noeud enregistré. Total : " + noeuds.size());
    }

    @Override
    public synchronized ServiceNoeud donneMachine() throws RemoteException, NoeudIndisponibleException {

        if (noeuds.isEmpty()) {
            throw new NoeudIndisponibleException("Aucun noeud de calcul enregistré.");
        }

        // chercher un noeud libre
        for (ServiceNoeud node : noeuds.keySet()) {
            if (noeuds.get(node).getKey() == EtatNoeud.LIBRE) {
                if (System.currentTimeMillis() - noeuds.get(node).getValue() > timeout) {
                    try {
                        System.out.println("Appel JE SUIS LA");
                        node.jeSuisLa(); // vérifier si le noeud est toujours actif
                    } catch (RemoteException e) {
                        System.out.println("Un noeud inactif a été retiré de la liste.");
                        noeuds.remove(node);
                        continue; // passer au noeud suivant
                    }
                }
                noeuds.put(node, Map.entry(EtatNoeud.OCCUPE, System.currentTimeMillis()));
                return node;
            }
        }

        throw new NoeudIndisponibleException("Aucun noeud disponible actuellement.");
    }

    @Override
    public void noeudAvertirCalcul(ServiceNoeud n) throws RemoteException {
        if (noeuds.containsKey(n)) {
            noeuds.put(n, Map.entry(EtatNoeud.OCCUPE, System.currentTimeMillis()));
        } else {
            throw new RemoteException("Noeud non enregistré.");
        }
    }

    @Override
    public void noeudAvertirLibre(ServiceNoeud n) throws RemoteException {
        if (noeuds.containsKey(n)) {
            noeuds.put(n, Map.entry(EtatNoeud.LIBRE, System.currentTimeMillis()));
        } else {
            throw new RemoteException("Noeud non enregistré.");
        }
    }
}