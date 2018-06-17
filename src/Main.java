import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    //Modifier cette valeur pour changer le nombre d'itérations de l'algorithme
    private static int NB_ITERATIONS = 100;
    //Modifier cette valeur pour changer la taille de la liste tabou
    private static int TAILLE_LISTE_TABOU = 10;
    //Modifier le numéro du jeu de données pour changer le fichier de départ.
    private static String FICHIER_DATA = "data/data01.csv";
    //Modifier cette valeur pour modifier le nombre de camion
    private static int NB_CAMIONS = 5;

    public static ArrayList<Camion> solutionActuelle;
    public static double fitnessActuelle;
    public static double meilleureFitnessTrouvee = 10000;
    public static ArrayList<ArrayList<Camion>> listeDeSolutions = new ArrayList<ArrayList<Camion>>();
    public static ArrayList<ArrayList<Camion>> listeTabou = new ArrayList<ArrayList<Camion>>();


    public static void main(String[] args) throws IOException {
        ArrayList<Client> clients = new ArrayList<>();
        clients = chargerClients();

        if(NB_CAMIONS < nbCamions(clients)){
            System.out.println("Nombre de camions minimum : " +nbCamions(clients));
            return;
        }
        System.out.println("SOLUTION INITIALE :");
        solutionActuelle = initialSolution(clients);
        fitnessActuelle = fitness(solutionActuelle);

        System.out.println(verifierSolution(solutionActuelle));

        //Inverser le commentaire des deux lignes pour passer d'une méthode à l'autre.
        for(int i = 0; i<=NB_ITERATIONS;i++){
            genererVoisinTabou(cloneList(solutionActuelle));
            //genererVoisin(cloneList(solutionActuelle));
        }

        System.out.println("MEILLEURE SOLUTION TROUVEE");
        afficherTournee(solutionActuelle);
        System.out.println(verifierSolution(solutionActuelle));
        System.out.println("Meilleure fitness trouvee : " + meilleureFitnessTrouvee);


    }

    /**
     * Permet de charger les clients depuis les différents fichier texte
     * @return La liste de tous les clients
     * @throws IOException
     */
    public static ArrayList<Client> chargerClients() throws IOException {
        // open file input stream;
        BufferedReader reader = new BufferedReader(new FileReader(FICHIER_DATA));

        // read file line by line
        String line = null;
        Scanner scanner = null;
        int index = 0;
        ArrayList<Client> clientsList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            Client client = new Client();
            scanner = new Scanner(line);
            scanner.useDelimiter(";");
            while (scanner.hasNext()) {
                String data = scanner.next();
                if (index == 0)
                    client.setId(Integer.parseInt(data));
                else if (index == 1)
                    client.setX(Integer.parseInt(data));
                else if (index == 2)
                    client.setY(Integer.parseInt(data));
                else if (index == 3)
                    client.setQte(Integer.parseInt(data));
                else
                    System.out.println("invalid data::" + data);
                index++;
            }
            index = 0;
            client.computeEntrepot();
            clientsList.add(client);
        }

        //close reader
        reader.close();

        /*for(Client unClient : clientsList){
            System.out.println(unClient.toString());
        }*/

        return clientsList;
    }

    /**
     * Permet de déterminer le nombre minimal de camion pour livrer tous les clients
     * @param clients : liste de tous les clients
     * @return le nombre de camions nécessaire
     */
    public static int nbCamions(ArrayList<Client> clients) {
        int qteTotale = 0;
        for (Client unClient : clients) {
            qteTotale += unClient.getQte();
        }
        if (qteTotale % 100 == 0)
            return qteTotale / 100;
        else
            return qteTotale / 100 + 1;
    }

    /**
     * Génère une solution initale aléatoire
     * @param clients
     * @return une liste de camion
     */
    public static ArrayList<Camion> initialSolution(ArrayList<Client> clients) {
        ArrayList<Client> clientsALivrer = new ArrayList<>(clients);
        ArrayList<Client> clientsLivres = new ArrayList<>();

        ArrayList<Camion> camions = new ArrayList<>();

        int nbCamions = nbCamions(clients);
        for (int i = 0; i < NB_CAMIONS; i++) {
            camions.add(new Camion());
        }

        double distanceMin = 1000000;
        Client clientLivrable = new Client();
        Client entrepot = clientsALivrer.get(0);
        boolean arret = false;
        clientsALivrer.remove(entrepot);

        Collections.shuffle(camions);

        for (Camion c : camions){
            c.getTournee().add(entrepot);
        }

        Random r = new Random();
        boolean next = false;
        for (Client client : clientsALivrer) {
            next = false;
            while (!next) {
                Camion camion = camions.get(r.nextInt(NB_CAMIONS));
                if (camion.getCapacite() > client.getQte()) {
                    camion.getTournee().add(client);
                    camion.setCapacite(camion.getCapacite() - client.getQte());
                    next = true;
                }
            }
        }

        for (Camion c : camions){
            c.getTournee().add(entrepot);
        }

//        for (Camion camion : camions) {
//            arret = false;
//            camion.getTournee().add(entrepot);
//            clientsALivrer.remove(entrepot);
//            Collections.shuffle(clientsALivrer);
//
//            for(Client client : new ArrayList<>(clientsALivrer)){
//                if(camion.getCapacite() > client.getQte()){
//                    camion.getTournee().add(client);
//                    camion.setCapacite(camion.getCapacite() - client.getQte());
//                    clientsALivrer.remove(client);
//                }
//            }
//            camion.getTournee().add(entrepot);
//        }

        afficherTournee(camions);
        return camions;
    }

    /**
     * Cette méthode récupère le meilleur voisin a chaque itération. Pas de gestion de liste tabou ici.
     * @param listCamionInitiale
     */
    public static void genererVoisin(List<Camion> listCamionInitiale){

        ArrayList<Camion> listCamionClone;

        for(int i =0;i<listCamionInitiale.size();i++){
            Camion c = listCamionInitiale.get(i);

            for(int j=1 ; j < c.getTournee().size()-1;j++){
                listCamionClone = cloneList(listCamionInitiale);
                Client client = c.getTournee().get(j);
                Camion camionClone = listCamionClone.get(i);
                camionClone.getTournee().remove(client);
                camionClone.setCapacite(camionClone.getCapacite() + client.getQte());

                for(Camion camion : listCamionClone){
                    for(int k=1 ; k<camion.getTournee().size()-1;k++){
                        Double sommeTournee = new Double(100 - camion.getCapacite());
                        if(sommeTournee + client.getQte() <= 100) {
                            camion.getTournee().add(k, client);
                            camion.setCapacite(camion.getCapacite() - client.getQte());
                            double fitnessPossible = fitness(listCamionClone);
                            if(fitnessPossible < fitnessActuelle){
                                fitnessActuelle = fitnessPossible;
                                solutionActuelle = cloneList(listCamionClone);

                            }
                            camion.getTournee().remove(client);
                            camion.setCapacite(camion.getCapacite() + client.getQte());
                        }
                    }
                }
            }
        }
    }

    /**
     * Cette méthode récupère le meilleur voisin et implémente le système de liste Tabou.
     * @param listCamionInitiale
     */
    public static void genererVoisinTabou(List<Camion> listCamionInitiale){

        listeDeSolutions.clear();
        boolean meilleureSolutionTrouvee = false;
        ArrayList<Camion> listCamionClone;

        for(int i =0;i<listCamionInitiale.size();i++){
            Camion c = listCamionInitiale.get(i);

            for(int j=1 ; j < c.getTournee().size()-1;j++){
                listCamionClone = cloneList(listCamionInitiale);
                Client client = c.getTournee().get(j);
                Camion camionClone = listCamionClone.get(i);
                camionClone.getTournee().remove(client);
                camionClone.setCapacite(camionClone.getCapacite() + client.getQte());

                for(Camion camion : listCamionClone){
                    for(int k=1 ; k<camion.getTournee().size()-1;k++){
                        Double sommeTournee = new Double(100 - camion.getCapacite());
                        if(sommeTournee + client.getQte() <= 100) {
                            camion.getTournee().add(k, client);
                            camion.setCapacite(camion.getCapacite() - client.getQte());
                            double fitnessPossible = fitness(listCamionClone);

                            listeDeSolutions.add(cloneList(listCamionClone));
                            if(fitnessPossible < fitnessActuelle && !(listeTabou.contains(listCamionClone))){
                                fitnessActuelle = fitnessPossible;
                                solutionActuelle = cloneList(listCamionClone);
                                meilleureSolutionTrouvee = true;
                            }
                            camion.getTournee().remove(client);
                            camion.setCapacite(camion.getCapacite() + client.getQte());
                        }
                    }
                }
                camionClone.getTournee().add(j, client);
            }
        }

        if(!meilleureSolutionTrouvee){
            ArrayList<Camion> meilleureSolution = meilleureSolution(listeDeSolutions);
            if(listeTabou.size() == TAILLE_LISTE_TABOU){
                listeTabou.remove(0);
            }
            listeTabou.add(solutionActuelle);
            solutionActuelle = cloneList(meilleureSolution);
            fitnessActuelle = fitness(meilleureSolution);
        }

        if(fitnessActuelle<meilleureFitnessTrouvee) {meilleureFitnessTrouvee = new Double(fitnessActuelle);}
    }

    /**
     * Dans le cas ou aucune solution de fitness inférieure n'est trouvée, on prends celle qui présente la meilleure
     * seconde fitness en prenant en compte la liste tabou.
     * @param solutions
     * @return
     */
    public static ArrayList<Camion> meilleureSolution(ArrayList<ArrayList<Camion>> solutions){
        double fitnessMin = 100000;
        ArrayList<Camion> meilleureSolution = new ArrayList<>();
        for(ArrayList<Camion> tournee : solutions){
            double fitnessTournee = fitness(tournee);
            if(fitnessTournee<fitnessMin && !listeTabou.contains(tournee) && !solutionActuelle.equals(tournee)){
                fitnessMin = fitnessTournee;
                meilleureSolution = tournee;
            }
        }
        return meilleureSolution;
    }

    /**
     * Permet simplement de vérifier qu'une solution reste faisable en terme de Capacite des camions.
     * @param camions
     * @return
     */
    public static boolean verifierSolution(ArrayList<Camion> camions){
        int nbClient = 0;
        for(Camion camion : camions){
            double sommeTournee = new Double(0);
            for(Client client : camion.getTournee()){
                sommeTournee += client.getQte();
                nbClient +=1;
                if(sommeTournee > 100){
                    return false;
                }
            }
        }

        System.out.println("\n" + (nbClient - NB_CAMIONS*2));
        return true;
    }

    /**
     * Calcule la distance entre deux clients
     * @param a
     * @param b
     * @return
     */
    public static double distance(Client a, Client b) {
        return Math.sqrt(Math.pow((b.getX() - a.getX()), 2) + Math.pow((b.getY() - a.getY()), 2));
    }

    /**
     * Affiche le contenu des tournees de la liste de camions passee en paramètre.
     * @param camions
     */
    public static void afficherTournee(ArrayList<Camion> camions){
        for(Camion camion : camions){
            System.out.println("\n Camion :" );
            for(Client client : camion.getTournee()){
                System.out.print("\t" + client.getId());
            }
        }
    }

    /**
     * Calcule la fitness d'une solution
     * @param camions
     * @return
     */
    public static double fitness(ArrayList<Camion> camions){
        double distanceTotale = 0;
        for (Camion camion : camions){
            distanceTotale += fitnessSeul(camion);
        }
        //System.out.println(distanceTotale);
        return distanceTotale;
    }

    /**
     * Calcule la fitness d'une tournee d'un seul camion
     * @param camion
     * @return
     */
    public static double fitnessSeul(Camion camion) {
        double distanceTournee = 0;
        int i = 0;
        while(i<camion.getTournee().size()-1){
            distanceTournee += distance(camion.getTournee().get(i), camion.getTournee().get(i+1));
            i++;
        }
        //System.out.println(distanceTournee);
        return distanceTournee;
    }

    /**
     * Permet de cloner une solution en clonant les camions et le cotnenu de leur tournee. Cela permet d'éviter les
     * problèmes de références.
     * @param list
     * @return
     */
    private static ArrayList<Camion> cloneList(List<Camion> list) {
        try{
            ArrayList<Camion> clone = new ArrayList<>(list.size());
            for (Camion item : list) clone.add((Camion) item.clone());
            return clone;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
