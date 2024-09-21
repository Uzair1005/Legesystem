import java.util.ArrayList;

class Pasient{
    public String navn;
    public String fodselsnummer;
    public int ID;
    public static int idTeller = 0;
    ArrayList<Resept> resepter = new ArrayList<>();

    public Pasient(String navn, String fodselsnummer){
        this.navn = navn;
        this.fodselsnummer = fodselsnummer;
        ID = idTeller;
        idTeller++;
    }

    public void leggTilResept(Resept resept){
        resepter.add(resept);

    }


    public int hentID(){
        return ID;
    }

    public String hentFodselsnummer(){
        return fodselsnummer;
    }

    public String hentNavn(){
        return navn;
    }

    public ArrayList<Resept> hentResepter(){
        return resepter;
    }
}