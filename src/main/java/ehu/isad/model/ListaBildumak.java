package ehu.isad.model;

import java.util.ArrayList;
import java.util.List;

public class ListaBildumak {
    private static ListaBildumak nireBilduma = null;
    private ArrayList<Bilduma> lista = null;

    private ListaBildumak(){
        lista = new ArrayList<Bilduma>();
    }

    public static ListaBildumak getNireBilduma() {
        if(nireBilduma==null){
            nireBilduma = new ListaBildumak();
        }
        return nireBilduma;
    }

    public void bildumaEzabatu(Bilduma zein){
        lista.remove(zein);
    }

    public void bildumaSartu(String bildumaIzena){
        Bilduma berria = new Bilduma(bildumaIzena);
        lista.add(berria);
    }

    public void argazkiakSartu(ArrayList<Argazkia> argazkiLista, Bilduma non){
        for (Argazkia argazki : argazkiLista){
            non.argazkiaSartu(argazki);
        }
    }

    public List<Bilduma> lortuBildumak(){
        List<Bilduma> emaitza = new ArrayList<>();
        for(Bilduma bil:lista){
            emaitza.add(bil);
        }
        return emaitza;
    }
}
