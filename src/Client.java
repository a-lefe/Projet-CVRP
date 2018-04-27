public class Client {

    private int id;
    private int x;
    private int y;
    private int qte;
    boolean entrepot;

    public Client(){}

    public Client(int id, int x, int y, int qte) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.qte = qte;
        computeEntrepot();
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
        String msg = "";
        if(this.entrepot == true){
            msg += "Entrepot : Localisé à : " + this.getX() + ";" + this.getY();
        }
        else{
            msg += "Client : " + this.getId() + " Localisé à : " + this.getX() + ";" +this.getY()
                    + " - Quantité livrée : " + this.getQte();
        }
        return msg;
    }

    public void computeEntrepot(){
        if(this.id == 0 && this.getQte() == 0){
            this.entrepot = true;
        }
        else{
            this.entrepot = false;
        }
    }
}
