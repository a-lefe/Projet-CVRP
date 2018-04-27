import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        chargerClients();
    }

    public static void chargerClients() throws IOException {
        // open file input stream;
        BufferedReader reader = new BufferedReader(new FileReader("data/data01.csv"));

        // read file line by line
        String line = null;
        Scanner scanner = null;
        int index = 0;
        List<Client> clientsList = new ArrayList<>();

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

        for(Client unClient : clientsList){
            System.out.println(unClient.toString());
        }
    }
}
