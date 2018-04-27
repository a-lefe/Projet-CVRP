import java.util.ArrayList;

public class Camion {

    ArrayList<Client> tournee = new ArrayList<>();
    int capacite;

    public ArrayList<Client> getTournee() {
        return tournee;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setTournee(ArrayList<Client> tournee) {
        this.tournee = tournee;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public Camion(ArrayList<Client> tournee) {
        this.tournee = tournee;
        this.capacite = 100;
    }

    public Camion(){}
}
