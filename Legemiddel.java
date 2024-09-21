import javax.print.attribute.standard.MediaSize.NA;

abstract class Legemiddel{
    public final int ID;
    private static int idTeller = 0;
    public final String navn;
    public int pris;
    public final double virkestoff;
    public Legemiddel(String navn, int pris, double virkestoff){
        this.navn = navn;
        this.pris = pris;
        this.virkestoff = virkestoff;
        ID = idTeller;
        idTeller++;
    }

    public int hentPris(){
        return pris;
    }

    public String hentNavn(){
        return navn;
    }

    public void settNyPris(int ny_pris){
        pris = ny_pris;
    }



public String toString(){
    return "Navn: " + navn + " Pris: " + pris + " Virkestoff: " + virkestoff + " ID: " + ID;
}
}


class Narkotisk extends Legemiddel{
    public final int styrke;

    public Narkotisk(String navn, int pris, double virkestoff, int styrke){
        super(navn, pris, virkestoff);
        this.styrke = styrke;
}

    @Override
    public String toString(){
        return "Navn: " + navn + " Pris: " + pris + " Virkestoff: " + virkestoff + " ID: " + ID + " Styrke: " + styrke;
    }
}

class Vanedannende extends Legemiddel{
    public final int gradAvVanedannende;

    public Vanedannende(String navn, int pris, double virkestoff, int gradAvVanedannende){
        super(navn, pris, virkestoff);
        this.gradAvVanedannende = gradAvVanedannende;
    }

    @Override
    public String toString(){
        return "Navn: " + navn + " Pris: " + pris + " Virkestoff: " + virkestoff + " ID: " + ID + " Grad av vanedannende: " + gradAvVanedannende;
    }
}

class Vanlig extends Legemiddel{
    public Vanlig(String navn, int pris, double virkestoff){
        super(navn, pris, virkestoff);
    }

    @Override
    public String toString(){
        return "Navn: " + navn + " Pris: " + pris + " Virkestoff: " + virkestoff + " ID: " + ID;
    }
}



class TestLegemiddel {
    public static void main(String[] args) {
        // Opprett et objekt av hver subklasse
        Vanlig legemiddel1 = new Vanlig("Paracetamol", 50, 500);
        Narkotisk narkotisk1 = new Narkotisk("Morfine", 100, 50, 5);
        Vanedannende vanedannende1 = new Vanedannende("Valium", 80, 20, 3);

        // Tester ID-ene
        testLegemiddelId(legemiddel1, 1);
        testLegemiddelId(narkotisk1, 2);
        testLegemiddelId(vanedannende1, 3);
    }

    private static boolean testLegemiddelId(Legemiddel legemiddel, int forventetLegemiddelId) {
        boolean testResultat = legemiddel.ID == forventetLegemiddelId;
        return testResultat;
    }
}


