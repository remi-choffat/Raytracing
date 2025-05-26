import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import raytracer.*;

/**
 * Client qui utilise le service de distribution pour calculer une image.
 */
public class ClientRaytracer {

    public static void main(String[] args) {

        String host;
        int port = 1099;

        String fichier_description = "scenes/simple.txt"; // fichier de description de la scène par défaut
        int largeur = 512, hauteur = 512; // dimensions par défaut de l'image
        int nx = 2, ny = 2; // divisions par défaut : 2x2

        // Si des arguments sont fournis, on les utilise pour configurer le client
        if (args.length > 0) {
            host = args[0];
            if (args.length > 1) port = Integer.parseInt(args[1]);
            if (args.length > 2) fichier_description = args[2];
            if (args.length > 3) largeur = Integer.parseInt(args[3]);
            if (args.length > 4) hauteur = Integer.parseInt(args[4]);
            if (args.length > 5) nx = Integer.parseInt(args[5]);
            if (args.length > 6) ny = Integer.parseInt(args[6]);
        } else {
            System.err.println("Usage: java ClientRaytracer [host] [port] [fichier_description] [largeur] [hauteur] [division_x] [division_y]");
            // Exemple : java ClientRaytracer localhost 1099 scenes/simple.txt 512 512 2 2
            return;
        }

        // Récupère le service de distribution de nœuds de calcul
        ServiceDistributeur distributeur;
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            distributeur = (ServiceDistributeur) registry.lookup("DistributeurRaytracer");
        } catch (RemoteException re) {
            System.err.println("Impossible de communiquer avec le service distant (" + host + ":" + port + ")");
            return;
        } catch (NotBoundException nbe) {
            System.err.println("Impossible d'accéder au registre RMI sur " + host + ":" + port);
            return;
        }

        // Crée une fenêtre pour afficher l'image
        Disp disp = new Disp("Raytracer", largeur, hauteur);

        // Taille de chaque sous-image
        int w = largeur / nx;
        int h = hauteur / ny;

        // Pour chaque sous-image, on demande à un nœud de calcul de calculer l'image
        for (int ix = 0; ix < nx; ix++) {
            for (int iy = 0; iy < ny; iy++) {

                // Calcul des coordonnées et dimensions de la sous-image
                int x = ix * w;
                int y = iy * h;
                int ww = (ix == nx - 1) ? largeur - x : w;
                int hh = (iy == ny - 1) ? hauteur - y : h;

                try {
                    // Demande à un nœud de calcul de produire l'image pour cette sous-image
                    distributeur.donneMachine();
                    ServiceNoeud noeud = distributeur.donneMachine();
                    Scene scene = new Scene(fichier_description, largeur, hauteur);
                    Image img = noeud.calculer(scene, x, y, ww, hh);

                    // Affiche l'image dans la fenêtre
                    disp.setImage(img, x, y);
                } catch (RemoteException e) {
                    System.err.println("Erreur lors de la demande de machine pour le calcul : " + e.getMessage());
                }
            }
        }

    }

}