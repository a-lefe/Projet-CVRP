public class Client {

    private int id;
    private int x;
    private int y;
    private int qte;

    public Client(int id, int x, int y, int qte) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.qte = qte;
    }

    public int getId() {
        return id;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getQte() {
        return qte;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setQte(int qte) {
        this.qte = qte;
    }

    
    public String toString(){
        return "Client : " + this.id + " Localisé à : " + this.x + ";" + this.y + " - Quantité livrée : " + this.qte;
    }
}
