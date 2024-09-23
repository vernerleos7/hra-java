import java.util.Random;
import java.util.Scanner;

abstract class Postava {
    protected String povolani;
    protected int zivoty;
    protected int sekundarniZdroj;
    
    public Postava(String povolani, int zivoty, int sekundarniZdroj) {
        this.povolani = povolani;
        this.zivoty = zivoty;
        this.sekundarniZdroj = sekundarniZdroj;
    }
    
    public abstract void schopnost1(Postava nepritel);
    public abstract void schopnost2(Postava nepritel);
    public abstract void schopnost3(Postava nepritel);

    public boolean jeZivy() {
        return zivoty > 0;
    }

    public void utrpZraneni(int zraneni) {
        zivoty -= zraneni;
        if (zivoty < 0) {
            zivoty = 0;
        }
    }

    public String getPovolani() {
        return povolani;
    }

    public int getZivoty() {
        return zivoty;
    }
}

class Hrac extends Postava {
    public Hrac(String povolani, int zivoty, int sekundarniZdroj) {
        super(povolani, zivoty, sekundarniZdroj);
    }

    @Override
    public void schopnost1(Postava nepritel) {
        System.out.println("Použil jsi útok mečem!");
        nepritel.utrpZraneni(10);
    }

    @Override
    public void schopnost2(Postava nepritel) {
        System.out.println("Použil jsi magický útok!");
        nepritel.utrpZraneni(15);
        sekundarniZdroj -= 10; 
    }

    @Override
    public void schopnost3(Postava nepritel) {
        System.out.println("Léčíš se!");
        zivoty += 10;
        sekundarniZdroj -= 5;  
    }
}

class Nepritel extends Postava {
    private Random random = new Random();

    public Nepritel(String povolani, int zivoty, int sekundarniZdroj) {
        super(povolani, zivoty, sekundarniZdroj);
    }

    @Override
    public void schopnost1(Postava nepritel) {
        System.out.println("Nepřítel použil útok drápy!");
        nepritel.utrpZraneni(8);
    }

    @Override
    public void schopnost2(Postava nepritel) {
        System.out.println("Nepřítel použil ohnivý dech!");
        nepritel.utrpZraneni(12);
        sekundarniZdroj -= 5;
    }

    @Override
    public void schopnost3(Postava nepritel) {
        System.out.println("Nepřítel se léčí!");
        zivoty += 5;
        sekundarniZdroj -= 3;
    }

    public void rozhodniAkci(Postava hrac) {
        int akce = random.nextInt(3) + 1;
        switch (akce) {
            case 1:
                schopnost1(hrac);
                break;
            case 2:
                schopnost2(hrac);
                break;
            case 3:
                schopnost3(hrac);
                break;
        }
    }
}

class GameState {
    private Hrac hrac;
    private Nepritel nepritel;
    private int skore = 0;
    private boolean jeNaTahuHrac = true;

    public GameState(Hrac hrac, Nepritel nepritel) {
        this.hrac = hrac;
        this.nepritel = nepritel;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        
        while (hrac.jeZivy() && nepritel.jeZivy()) {
            System.out.println("\n--- Nové kolo ---");
            System.out.println("Hráč: " + hrac.getZivoty() + " životů, " + hrac.sekundarniZdroj + " mana/výdrž");
            System.out.println("Nepřítel: " + nepritel.getZivoty() + " životů");

            if (jeNaTahuHrac) {
                System.out.println("\nTvůj tah! Vyber schopnost:");
                System.out.println("1. Útok mečem");
                System.out.println("2. Magický útok");
                System.out.println("3. Léčení");

                int volba = scanner.nextInt();
                switch (volba) {
                    case 1:
                        hrac.schopnost1(nepritel);
                        break;
                    case 2:
                        hrac.schopnost2(nepritel);
                        break;
                    case 3:
                        hrac.schopnost3(nepritel);
                        break;
                    default:
                        System.out.println("Neplatná volba!");
                        continue;
                }
            } else {
                System.out.println("\nTah nepřítele!");
                nepritel.rozhodniAkci(hrac);
            }

            jeNaTahuHrac = !jeNaTahuHrac;

            if (!nepritel.jeZivy()) {
                System.out.println("Nepřítel byl poražen!");
                skore++;
                break;
            } else if (!hrac.jeZivy()) {
                System.out.println("Byl jsi poražen!");
                break;
            }
        }

        System.out.println("Konec hry. Skóre: " + skore);
        scanner.close();
    }
}

public class Main {
    public static void main(String[] args) {
        Hrac hrac = new Hrac("Válečník", 50, 30);
        Nepritel nepritel = new Nepritel("Drak", 40, 20);
        
        GameState hra = new GameState(hrac, nepritel);
        hra.start();
    }
}
