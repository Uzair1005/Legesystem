class Lege implements Comparable<Lege>{
    protected String navn;
    private IndeksertListe<Resept> utskrevneResepter;

    public Lege(String navn) {
        this.navn = navn;
        this.utskrevneResepter = new IndeksertListe<Resept>();
    }

    public String hentNavn() {
        return navn;
    }

    public IndeksertListe<Resept> hentUtskrevneResepter() {
        return utskrevneResepter;
    }

    public void skrivResept(Resept resept) {
        utskrevneResepter.leggTil(resept.hentId(), resept);
    }

    public Resept skrivResept(Legemiddel legemiddel, Pasient pasient, int reit) throws UlovligUtskrift {
    HvitResept resept = new HvitResept(legemiddel, this, pasient, reit);
    this.utskrevneResepter.leggTil(resept);
    return resept;
}

public class UlovligUtskrift extends Exception {
    public UlovligUtskrift(Lege lege, Legemiddel legemiddel) {
        super("Legen " + lege.hentNavn() + " har ikke lov til å skrive ut legemiddelet " + legemiddel.hentNavn());
    }
}




    public String toString(){
        return "Navn: " + navn;
    }

    @Override
    public int compareTo(Lege annenLege) {
        return this.navn.compareTo(annenLege.navn);
    }
}

class Spesialist extends Lege implements Godkjenningsfritak {
    private String kontrollkode;

    public Spesialist(String navn, String kontrollkode) {
        super(navn);
        this.kontrollkode = kontrollkode;
    }

    @Override
    public String hentKontrollkode() {
        return kontrollkode;
    }

    @Override
    public String toString(){
        return "Navn: " + hentNavn() + " Kontrollkode: " + kontrollkode;
    }
}




