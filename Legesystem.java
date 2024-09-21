import java.io.*;
import java.util.*;

public class Legesystem {
    private List<Pasient> pasienter = new ArrayList<>();
    private List<Lege> leger = new ArrayList<>();
    private List<Legemiddel> legemidler = new ArrayList<>();
    private List<Resept> resepter = new ArrayList<>();

    public void lesFraFil(String filnavn) {
        try (BufferedReader br = new BufferedReader(new FileReader(filnavn))) {
            String linje;
            String seksjon = "";

            while ((linje = br.readLine()) != null) {
                linje = linje.trim();
                
                // Skip tomme linjer
                if (linje.isEmpty()) continue;

                // Sjekk om vi er i en ny seksjon
                if (linje.startsWith("#")) {
                    if (linje.contains("Pasienter")) {
                        seksjon = "Pasienter";
                    } else if (linje.contains("Legemidler")) {
                        seksjon = "Legemidler";
                    } else if (linje.contains("Leger")) {
                        seksjon = "Leger";
                    } else if (linje.contains("Resepter")) {
                        seksjon = "Resepter";
                    }
                    continue;
                }

                // Avhengig av seksjon, parse linjen
                switch (seksjon) {
                    case "Pasienter":
                        lesPasient(linje);
                        break;
                    case "Legemidler":
                        lesLegemiddel(linje);
                        break;
                    case "Leger":
                        lesLege(linje);
                        break;
                    case "Resepter":
                        lesResept(linje);
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Feil ved lesing av fil: " + e.getMessage());
        }
    }

    private void lesPasient(String linje) {
        try {
            String[] deler = linje.split(",");
            String navn = deler[0];
            String fodselsnummer = deler[1];
            pasienter.add(new Pasient(navn, fodselsnummer));
        } catch (Exception e) {
            System.err.println("Feil ved parsing av pasient: " + linje);
        }
    }

    private void lesLegemiddel(String linje) {
        try {
            String[] deler = linje.split(",");
            String navn = deler[0];
            String type = deler[1];
            int pris = Integer.parseInt(deler[2]);
            double virkestoff = Double.parseDouble(deler[3]);

            if (type.equals("narkotisk")) {
                int styrke = Integer.parseInt(deler[4]);
                legemidler.add(new Narkotisk(navn, pris, virkestoff, styrke));
            } else if (type.equals("vanedannende")) {
                int gradAvVanedannende = Integer.parseInt(deler[4]);
                legemidler.add(new Vanedannende(navn, pris, virkestoff, gradAvVanedannende));
            } else if (type.equals("vanlig")) {
                legemidler.add(new Vanlig(navn, pris, virkestoff));
            }
        } catch (Exception e) {
            System.err.println("Feil ved parsing av legemiddel: " + linje);
        }
    }

    private void lesLege(String linje) {
        try {
            String[] deler = linje.split(",");
            String navn = deler[0];
            int kontrollid = Integer.parseInt(deler[1]);

            if (kontrollid == 0) {
                leger.add(new Lege(navn));
            } else {
                leger.add(new Spesialist(navn, String.valueOf(kontrollid)));
            }
        } catch (Exception e) {
            System.err.println("Feil ved parsing av lege: " + linje);
        }
    }

    private void lesResept(String linje) {
        try {
            String[] deler = linje.split(",");
            int legemiddelNummer = Integer.parseInt(deler[0]);
            String legeNavn = deler[1];
            int pasientID = Integer.parseInt(deler[2]);
            String type = deler[3];

            Legemiddel legemiddel = legemidler.get(legemiddelNummer);
            Lege lege = finnLege(legeNavn);
            Pasient pasient = pasienter.get(pasientID);
            int reit = deler.length > 4 ? Integer.parseInt(deler[4]) : 0;

            Resept resept = null;
            if (type.equals("hvit")) {
                resept = lege.skrivResept(legemiddel, pasient, reit);
            } else if (type.equals("blaa")) {
                resept = new BlaaResept(legemiddel, lege, pasient, reit);
            } else if (type.equals("militaer")) {
                resept = new MilResept(legemiddel, lege, pasient);
            } else if (type.equals("p")) {
                resept = new Presept(legemiddel, lege, pasient);
            }

            if (resept != null) {
                resepter.add(resept);
            }
        } catch (Exception e) {
            System.err.println("Feil ved parsing av resept: " + linje);
        }
    }

    private Lege finnLege(String navn) {
        for (Lege lege : leger) {
            if (lege.hentNavn().equals(navn)) {
                return lege;
            }
        }
        return null;
    }



    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean fortsett = true;

        while (fortsett) {
            System.out.println("\n--- Legesystem ---");
            System.out.println("1. Skriv ut fullstendig oversikt");
            System.out.println("2. Opprett og legg til nye elementer");
            System.out.println("3. Bruk en resept");
            System.out.println("4. Skriv ut statistikk");
            System.out.println("5. Avslutt");
            System.out.print("Velg en operasjon: ");

            int valg = scanner.nextInt();
            scanner.nextLine(); // Konsumér linjeskift

            switch (valg) {
                case 1:
                    skrivUtOversikt();  // E3
                    break;
                case 2:
                    opprettNyttElement();  // E4
                    break;
                case 3:
                    brukResept();  // E5
                    break;
                case 4:
                    skrivUtStatistikk();  // E6
                    break;
                case 5:
                    fortsett = false;
                    System.out.println("Programmet avsluttes.");
                    break;
                default:
                    System.out.println("Ugyldig valg. Vennligst prøv igjen.");
            }
        }
    }

    // Metode for å skrive ut fullstendig oversikt (E3)
    public void skrivUtOversikt() {
        System.out.println("\n--- Fullstendig oversikt ---");

        System.out.println("\nPasienter:");
        for (Pasient pasient : pasienter) {
            System.out.println(pasient.hentNavn());
        }

        System.out.println("\nLeger (sortert alfabetisk):");
        List<Lege> sorterteLeger = new ArrayList<>(leger);
        Collections.sort(sorterteLeger);  // Sorterer leger alfabetisk
        for (Lege lege : sorterteLeger) {
            System.out.println(lege);
        }

        System.out.println("\nLegemidler:");
        for (Legemiddel legemiddel : legemidler) {
            System.out.println(legemiddel);
        }

        System.out.println("\nResepter:");
        for (Resept resept : resepter) {
            System.out.println(resept);
        }
    }

    // Metode for å opprette og legge til nye elementer (E4)
    public void opprettNyttElement() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- Opprett nytt element ---");
        System.out.println("1. Legg til pasient");
        System.out.println("2. Legg til lege");
        System.out.println("3. Legg til legemiddel");
        System.out.println("4. Legg til resept");
        System.out.print("Velg en operasjon: ");
    
        int valg = scanner.nextInt();
        scanner.nextLine(); // Konsumér linjeskift
    
        switch (valg) {
            case 1:
                leggTilPasient();
                break;
            case 2:
                leggTilLege();
                break;
            case 3:
                leggTilLegemiddel();
                break;
            case 4:
                leggTilResept();
                break;
            default:
                System.out.println("Ugyldig valg. Vennligst prøv igjen.");
        }

        

    }

    private void leggTilPasient() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Skriv inn pasientens navn: ");
        String navn = scanner.nextLine();
    
        System.out.print("Skriv inn pasientens fødselsnummer: ");
        String fodselsnummer = scanner.nextLine();
    
        // Opprett pasienten og legg til listen
        Pasient nyPasient = new Pasient(navn, fodselsnummer);
        pasienter.add(nyPasient);
        
        System.out.println("Pasient lagt til: " + nyPasient);
    }


    private void leggTilLege() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Skriv inn legens navn: ");
        String navn = scanner.nextLine();
    
        System.out.print("Skriv inn legens kontrollID (0 hvis vanlig lege): ");
        int kontrollID = scanner.nextInt();
        scanner.nextLine(); // Konsumér linjeskift
    
        Lege nyLege;
        if (kontrollID == 0) {
            nyLege = new Lege(navn);
        } else {
            nyLege = new Spesialist(navn, String.valueOf(kontrollID));
        }
    
        // Legg til legen i listen over leger
        leger.add(nyLege);
        
        System.out.println("Lege lagt til: " + nyLege);
    }

    
    private void leggTilLegemiddel() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Skriv inn navnet på legemiddelet: ");
        String navn = scanner.nextLine();
    
        System.out.print("Skriv inn type (narkotisk, vanedannende, vanlig): ");
        String type = scanner.nextLine().toLowerCase();
    
        System.out.print("Skriv inn prisen på legemiddelet: ");
        int pris = scanner.nextInt();
    
        System.out.print("Skriv inn mengden virkestoff: ");
        double virkestoff = scanner.nextDouble();
    
        Legemiddel nyttLegemiddel = null;
        if (type.equals("narkotisk")) {
            System.out.print("Skriv inn narkotisk styrke: ");
            int styrke = scanner.nextInt();
            nyttLegemiddel = new Narkotisk(navn, pris, virkestoff, styrke);
        } else if (type.equals("vanedannende")) {
            System.out.print("Skriv inn graden av vanedannende: ");
            int gradAvVanedannende = scanner.nextInt();
            nyttLegemiddel = new Vanedannende(navn, pris, virkestoff, gradAvVanedannende);
        } else if (type.equals("vanlig")) {
            nyttLegemiddel = new Vanlig(navn, pris, virkestoff);
        } else {
            System.out.println("Ugyldig legemiddeltype.");
            return;
        }
    
        legemidler.add(nyttLegemiddel);
        System.out.println("Legemiddel lagt til: " + nyttLegemiddel);
    }

    
    private void leggTilResept() {
        Scanner scanner = new Scanner(System.in);
    
        // Finn legemiddel
        System.out.print("Skriv inn legemiddelnummer: ");
        int legemiddelNummer = scanner.nextInt();
        scanner.nextLine(); // Konsumér linjeskift
    
        if (legemiddelNummer < 0 || legemiddelNummer >= legemidler.size()) {
            System.out.println("Ugyldig legemiddelnummer.");
            return;
        }
        Legemiddel legemiddel = legemidler.get(legemiddelNummer);
    
        // Finn lege
        System.out.print("Skriv inn legens navn: ");
        String legeNavn = scanner.nextLine();
        Lege lege = finnLege(legeNavn);
    
        if (lege == null) {
            System.out.println("Ugyldig lege.");
            return;
        }
    
        // Finn pasient
        System.out.print("Skriv inn pasient-ID: ");
        int pasientID = scanner.nextInt();
        scanner.nextLine(); // Konsumér linjeskift
    
        if (pasientID < 0 || pasientID >= pasienter.size()) {
            System.out.println("Ugyldig pasient-ID.");
            return;
        }
        Pasient pasient = pasienter.get(pasientID);
    
        // Skriv inn type resept
        System.out.print("Skriv inn type resept (hvit, blaa, militaer, p): ");
        String type = scanner.nextLine().toLowerCase();
    
        // Skriv inn reit (kun for hvit, blaa og p)
        int reit = 0;
        if (type.equals("hvit") || type.equals("blaa") || type.equals("p")) {
            System.out.print("Skriv inn reit: ");
            reit = scanner.nextInt();
            scanner.nextLine(); // Konsumér linjeskift
        }
    
        // Opprett resept
        Resept resept = null;
        try {
            if (type.equals("hvit")) {
                resept = lege.skrivResept(legemiddel, pasient, reit);
            } else if (type.equals("blaa")) {
                resept = new BlaaResept(legemiddel, lege, pasient, reit);
            } else if (type.equals("militaer")) {
                resept = new MilResept(legemiddel, lege, pasient);
            } else if (type.equals("p")) {
                resept = new Presept(legemiddel, lege, pasient);
            } else {
                System.out.println("Ugyldig resepttype.");
                return;
            }
    
            if (resept != null) {
                resepter.add(resept);
                System.out.println("Resept lagt til: " + resept);
            }
        } catch (Lege.UlovligUtskrift e) {
            System.out.println("Feil ved opprettelse av resept: " + e.getMessage());
        }

    
    }
    
    
    

    // Metode for å bruke en resept (E5)
         // Legger til metode for å bruke en resept
    public void brukResept() {
        Scanner scanner = new Scanner(System.in);

        // Trinn 1: Vis en liste over pasienter
        System.out.println("Hvilken pasient vil du se resepter for?");
        for (int i = 0; i < pasienter.size(); i++) {
            Pasient pasient = pasienter.get(i);
            System.out.println(i + ": " + pasient.hentNavn() + " (fnr " + pasient.hentFodselsnummer() + ")");
        }
        
        // Brukeren velger pasient
        int pasientIndeks = Integer.parseInt(scanner.nextLine());
        Pasient valgtPasient = pasienter.get(pasientIndeks);
        System.out.println("Valgt pasient: " + valgtPasient.hentNavn() + " (fnr " + valgtPasient.hentFodselsnummer() + ").");

        // Trinn 2: Vis en liste over resepter for valgt pasient
        System.out.println("Hvilken resept vil du bruke?");
        for (int i = 0; i < valgtPasient.hentResepter().size(); i++) {
            Resept resept = valgtPasient.hentResepter().get(i);
            System.out.println(i + ": " + resept.hentLegemiddel().hentNavn() + " (" + resept.hentReit() + " reit)");
        }
        
        // Brukeren velger resept
        int reseptIndeks = Integer.parseInt(scanner.nextLine());
        Resept valgtResept = valgtPasient.hentResepter().get(reseptIndeks);

        // Trinn 3: Forsøk å bruke resepten
        if (valgtResept.bruk()) {
            System.out.println("Brukte resept på " + valgtResept.hentLegemiddel().hentNavn() + ". Antall gjenstående reit: " + valgtResept.hentReit());
        } else {
            System.out.println("Kunne ikke bruke resept på " + valgtResept.hentLegemiddel().hentNavn() + " (ingen gjenstående reit).");
        }
    }
    

    // Metode for å skrive ut statistikk (E6)
        public void skrivUtStatistikk() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Velg statistikk:");
            System.out.println("1: Totalt antall utskrevne resepter på vanedannende legemidler");
            System.out.println("2: Totalt antall utskrevne resepter på narkotiske legemidler");
            System.out.println("3: Statistikk om mulig misbruk av narkotika");
            int valg = Integer.parseInt(scanner.nextLine());
    
            switch (valg) {
                case 1:
                    visVanedannendeStatistikk();
                    break;
                case 2:
                    visNarkotiskeStatistikk();
                    break;
                case 3:
                    visMisbrukStatistikk();
                    break;
                default:
                    System.out.println("Ugyldig valg.");
            }
        }
    
        // Statistikk: Totalt antall utskrevne resepter på vanedannende legemidler
        private void visVanedannendeStatistikk() {
            int antallVanedannendeResepter = 0;
    
            for (Resept resept : resepter) {
                if (resept.hentLegemiddel() instanceof Vanedannende) {
                    antallVanedannendeResepter++;
                }
            }
    
            System.out.println("Totalt antall utskrevne resepter på vanedannende legemidler: " + antallVanedannendeResepter);
        }
    
        // Statistikk: Totalt antall utskrevne resepter på narkotiske legemidler
        private void visNarkotiskeStatistikk() {
            int antallNarkotiskeResepter = 0;
    
            for (Resept resept : resepter) {
                if (resept.hentLegemiddel() instanceof Narkotisk) {
                    antallNarkotiskeResepter++;
                }
            }
    
            System.out.println("Totalt antall utskrevne resepter på narkotiske legemidler: " + antallNarkotiskeResepter);
        }
    
        // Statistikk: Mulig misbruk av narkotika
        private void visMisbrukStatistikk() {
            // Leger med minst en utskrevet resept på narkotiske legemidler
            Map<String, Integer> legeNarkotiskeResepter = new TreeMap<>();
            
            for (Lege lege : leger) {
                int antallNarkotiske = 0;
                for (Resept resept : lege.hentUtskrevneResepter()) {
                    if (resept.hentLegemiddel() instanceof Narkotisk) {
                        antallNarkotiske++;
                    }
                }
                if (antallNarkotiske > 0) {
                    legeNarkotiskeResepter.put(lege.hentNavn(), antallNarkotiske);
                }
            }
    
            System.out.println("Leger som har skrevet ut minst én resept på narkotiske legemidler:");
            for (Map.Entry<String, Integer> entry : legeNarkotiskeResepter.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " narkotiske resepter");
            }
    
            // Pasienter med minst én gyldig resept på narkotiske legemidler
            Map<String, Integer> pasientNarkotiskeResepter = new TreeMap<>();
    
            for (Pasient pasient : pasienter) {
                int antallNarkotiske = 0;
                for (Resept resept : pasient.hentResepter()) {
                    if (resept.hentLegemiddel() instanceof Narkotisk && resept.hentReit() > 0) {
                        antallNarkotiske++;
                    }
                }
                if (antallNarkotiske > 0) {
                    pasientNarkotiskeResepter.put(pasient.hentNavn(), antallNarkotiske);
                }
            }
    
            System.out.println("Pasienter som har minst én gyldig resept på narkotiske legemidler:");
            for (Map.Entry<String, Integer> entry : pasientNarkotiskeResepter.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " narkotiske resepter");
            }
        }


            // Metode for å skrive alle data til fil (E7)
    public void skrivTilFil() {
        System.out.println("Funksjonalitet for å skrive data til fil kommer her.");
    }

    // Hovedmetode for å starte programmet
    public static void main(String[] args) {
        Legesystem system = new Legesystem();
        system.start();
    }


    }




    






