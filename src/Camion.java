import java.util.ArrayList;

public class Camion implements Cloneable{

    ArrayList<Client> tournee = new ArrayList<>();
    int capacite = 100;

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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Camion c = (Camion) super.clone();
        c.tournee = (ArrayList<Client>)tournee.clone();
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Camion camion = (Camion) o;

        if (tournee != null ? !tournee.equals(camion.tournee) : camion.tournee != null) return false;
        if(!(capacite == camion.capacite)){
            return false;
        }
        if(!(Main.fitnessSeul(this) == Main.fitnessSeul(camion))){
            return false;
        }
        return true;
    }
}
