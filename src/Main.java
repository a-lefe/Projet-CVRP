import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        ArrayList<Client> clients = new ArrayList<>();
        clients = chargerClients();

        Camion camion = new Camion();

    public static ArrayList<Client> chargerClients() throws IOException {
        // open file input stream;
        BufferedReader reader = new BufferedReader(new FileReader("data/data01.csv"));

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

        return  clientsList;
    }

    public static int nbCamions (ArrayList<Client> clients){
        int qteTotale = 0;
        for(Client unClient : clients){
            qteTotale+= unClient.getQte();
        }
        if(qteTotale%100 == 0)
            return qteTotale / 100;
        else
            return qteTotale / 100 + 1;
    }

    public  static void initialSolution(ArrayList<Client> clients){
        ArrayList<Client> clientsALivrer = new ArrayList<>(clients);
        ArrayList<Client> clientsLivres = new ArrayList<>();

        int nbCamions = nbCamions(clients);



        while(clientsALivrer.size() != 0) {



        }
    }


}
