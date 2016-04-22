package produtor.consumidor;


public class ProdutorConsumidor {

        public static void main(String[] args) {
            Buffer bufferCompartilhado = new Buffer();
            Produtor produtor = new Produtor(bufferCompartilhado, 5);
            Consumidor consumidor1 = new Consumidor(1, bufferCompartilhado, 2);
            Consumidor consumidor2 = new Consumidor(2, bufferCompartilhado, 8);

            produtor.start();
            consumidor1.start();
            consumidor2.start();
    }
    
}

class Consumidor extends Thread {
    private int idConsumidor;
    private Buffer pilha;
    private int totalConsumir;
 
    public Consumidor(int id, Buffer p, int totalConsumir) {
        idConsumidor = id;
        pilha = p;
        this.totalConsumir = totalConsumir;
    }
 
    public void run() {
        for (int i = 0; i < totalConsumir; i++) {
            pilha.get(idConsumidor);
        }
        System.out.println("Consumidor #" + idConsumidor + " concluido!");
    }
}

class Produtor extends Thread {
    private Buffer pilha;
    private int producaoTotal;
 
    public Produtor(Buffer p, int producaoTotal) {
        pilha = p;
        this.producaoTotal = producaoTotal;
    }
 
    public void run() {
        for (int i = 0; i < producaoTotal; i++) {
            pilha.set(i);
        }
        System.out.println("Produtor concluido!");
    }
}

class Buffer {
 
    private int conteudo;
    private boolean disponivel;
 
    public synchronized void set(int valor) {
        while (disponivel == true) {
            try {
                System.out.println("Produtor esperando...");
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        conteudo = valor;
        System.out.println("Produtor colocou " + conteudo);
        disponivel = true;
        notifyAll();
    }
 
    public synchronized int get(int idConsumidor) {
        while (disponivel == false) {
            try {
                System.out.println("Consumidor #" + idConsumidor
                        + " esperado...");
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Consumidor #" + idConsumidor + " consumiu: "
                + conteudo);
        disponivel = false;
        notifyAll();
        return conteudo;
    }
}