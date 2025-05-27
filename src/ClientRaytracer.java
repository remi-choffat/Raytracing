import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.atomic.AtomicInteger;

import raytracer.*;

/**
 * Client qui utilise le service de distribution pour calculer une image.
 */
public class ClientRaytracer {

    public static void main(String[] args) {

        String host;
        int port = 1099;

        String fichier_description; // fichier de description de la scène par défaut
        int largeur, hauteur; // dimensions par défaut de l'image
        int nx = 2, ny = 2; // divisions par défaut : 2x2

        // Si des arguments sont fournis, on les utilise pour configurer le client
        if (args.length > 0) {
            host = args[0];
            if (args.length > 1) port = Integer.parseInt(args[1]);
            if (args.length > 2) fichier_description = args[2];
            else {
                fichier_description = "scenes/simple.txt";
            }
            if (args.length > 3) largeur = Integer.parseInt(args[3]);
            else {
                largeur = 512;
            }
            if (args.length > 4) hauteur = Integer.parseInt(args[4]);
            else {
                hauteur = 512;
            }
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

        Thread[][] threads = new Thread[nx][ny];
        AtomicInteger compteur = new AtomicInteger(0);
        int total = nx * ny;

        // Pour chaque sous-image, on demande à un nœud de calcul de calculer l'image
        for (int ix = 0; ix < nx; ix++) {
            for (int iy = 0; iy < ny; iy++) {
                final int x = ix * w;
                final int y = iy * h;
                final int ww = (ix == nx - 1) ? largeur - x : w;
                final int hh = (iy == ny - 1) ? hauteur - y : h;

                threads[ix][iy] = new Thread(() -> {
                    try {
                        ServiceNoeud noeud = distributeur.donneMachine();
                        Scene scene = new Scene(fichier_description, largeur, hauteur);
                        Image img = noeud.calculer(scene, x, y, ww, hh);
                        synchronized (disp) {
                            disp.setImage(img, x, y);
                        }
                        int done = compteur.incrementAndGet();
                        int percent = (int) ((done * 100.0) / total);
                        System.out.print("\rCalcul de l'image en cours (" + percent + " %)...");
                    } catch (RemoteException e) {
                        System.err.println("Erreur lors du calcul d'une sous-image : " + e.getMessage());
                    }
                });
                threads[ix][iy].start();

            }
        }

        // Attend la fin de tous les threads
        for (int ix = 0; ix < nx; ix++) {
            for (int iy = 0; iy < ny; iy++) {
                try {
                    threads[ix][iy].join();
                } catch (InterruptedException e) {
                    System.err.println("Thread interrompu : " + e.getMessage());
                }
            }
        }

        // Affiche le message de succès uniquement si toute l'image est calculée
        if (compteur.get() == total) {
            System.out.println("\nImage calculée et affichée avec succès !");
        } else {
            System.err.println("\nErreur : l'image n'a pas été entièrement calculée (" + compteur.get() + "/" + total + ").");
        }
    }

}
