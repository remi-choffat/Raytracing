package src;

import java.time.Instant;
import java.time.Duration;

import src.raytracer.*;


public class LancerRaytracer {

    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [fichier-scène] [largeur] [hauteur]\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n";

    public static void main(String[] args) {

        // Le fichier de description de la scène si pas fournie
        String fichier_description = "simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 512, hauteur = 512;

        if (args.length > 0) {
            fichier_description = args[0];
            if (args.length > 1) {
                largeur = Integer.parseInt(args[1]);
                if (args.length > 2)
                    hauteur = Integer.parseInt(args[2]);
            }
        } else {
            System.out.println(aide);
        }


        // création d'une fenêtre 
        Disp disp = new Disp("Raytracer", largeur, hauteur);

        // Initialisation d'une scène depuis le modèle 
        Scene scene = new Scene(fichier_description, largeur, hauteur);

        // Calcul de l'image de la scène les paramètres : 
        // - x0 et y0 : correspondant au coin haut à gauche
        // - l et h : hauteur et largeur de l'image calculée
        // Ici on calcule toute l'image (0,0) -> (largeur, hauteur)

        int x0 = 0, y0 = 0;

        int demiLargeur = largeur / 2;
        int demiHauteur = hauteur / 2;

        Instant debut = Instant.now();

        // Quart haut-gauche
        Instant debut1 = Instant.now();
        System.out.println("Calcul de l'image :\n - Coordonnées : " + x0 + "," + y0
                + "\n - Taille " + demiLargeur + "x" + demiHauteur);
        Image imageHG = scene.compute(0, 0, demiLargeur, demiHauteur);
        Instant fin1 = Instant.now();
        System.out.println("Quart haut-gauche calculé en : " + Duration.between(debut1, fin1).toMillis() + " ms");
        disp.setImage(imageHG, 0, 0);

        // Quart bas-droite
        Instant debut2 = Instant.now();
        System.out.println("Calcul de l'image :\n - Coordonnées : " + x0 + "," + y0
                + "\n - Taille " + demiLargeur + "x" + demiHauteur);
        Image imageBD = scene.compute(demiLargeur, demiHauteur, demiLargeur, demiHauteur);
        Instant fin2 = Instant.now();
        System.out.println("Quart bas-droite calculé en : " + Duration.between(debut2, fin2).toMillis() + " ms");
        disp.setImage(imageBD, demiLargeur, demiHauteur);

        Instant fin = Instant.now();
        Duration duree = Duration.between(debut, fin);
        System.out.println("Image complète calculée en : " + duree + " ms");
    }
}
