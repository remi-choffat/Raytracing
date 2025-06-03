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


    /**
     * Enregistre un nouveau nœud de calcul dans le distributeur.
     *
     * @param node Le nœud à enregistrer.
     */
    public synchronized void enregistrerNoeud(ServiceNoeud node) throws RemoteException {
        noeuds.put(node, Map.entry(EtatNoeud.LIBRE, System.currentTimeMillis()));
        System.out.println("Nouveau noeud enregistré. Total : " + noeuds.size());
    }


    /**
     * Fournit un nœud de calcul libre pour effectuer un calcul.
     *
     * @return Un nœud de calcul libre.
     * @throws RemoteException            Si une erreur de communication RMI se produit.
     * @throws NoeudIndisponibleException Si aucun nœud de calcul n'est disponible.
     */
    @Override
    public synchronized ServiceNoeud donneMachine() throws RemoteException, NoeudIndisponibleException {

        if (noeuds.isEmpty()) {
            throw new NoeudIndisponibleException("Aucun noeud de calcul enregistré.");
        }

        // Cherche un noeud libre
        for (ServiceNoeud node : noeuds.keySet()) {
            if (noeuds.get(node).getKey() == EtatNoeud.LIBRE) {
                if (System.currentTimeMillis() - noeuds.get(node).getValue() > timeout) {
                    try {
                        node.jeSuisLa(); // vérifie si le noeud est toujours actif
                    } catch (RemoteException e) {
                        System.out.println("Un noeud inactif a été retiré de la liste.");
                        noeuds.remove(node);
                        continue; // passe au noeud suivant
                    }
                }
                noeuds.put(node, Map.entry(EtatNoeud.OCCUPE, System.currentTimeMillis()));
                return node;
            }
        }

        throw new NoeudIndisponibleException("Aucun noeud disponible actuellement.");
    }


    /**
     * Avertit le distributeur qu'un nœud de calcul est en train d'effectuer un calcul.
     *
     * @param n le nœud de calcul qui effectue le calcul
     * @throws RemoteException Si une erreur de communication RMI se produit.
     */
    @Override
    public void noeudAvertirCalcul(ServiceNoeud n) throws RemoteException {
        if (noeuds.containsKey(n)) {
            noeuds.put(n, Map.entry(EtatNoeud.OCCUPE, System.currentTimeMillis()));
        } else {
            throw new RemoteException("Noeud non enregistré.");
        }
    }


    /**
     * Avertit le distributeur qu'un nœud de calcul est maintenant libre.
     *
     * @param n le nœud de calcul qui est maintenant libre
     * @throws RemoteException Si une erreur de communication RMI se produit.
     */
    @Override
    public void noeudAvertirLibre(ServiceNoeud n) throws RemoteException {
        if (noeuds.containsKey(n)) {
            noeuds.put(n, Map.entry(EtatNoeud.LIBRE, System.currentTimeMillis()));
        } else {
            throw new RemoteException("Noeud non enregistré.");
        }
    }
}
