package greedy;

/**
 * Questa classe permette di memorizzare le statistiche sull'elaborazione degli
 * algoritmi
 *
 * @author Stefano Dalla Palma
 */
public class Statistiche {

    public double tempoDiEsecuzione;
    public int iter;
    public double meanTimeIterate;
    public double meanTimeRecuperoArchiConColoreMinimo;
    public double meanTimeInserimentoArchiSenzaCiclo;
    public double meanTimeRimozioneArchi;
    public double meanTimeDeterminazioneColorePiuRicorrente;
    public double meanTimeRimozioneColorePiuRicorrente;
    public int profonditaSoluzione;

    public Statistiche() {}

    public double convertiInSecondi (long valore) {
        return (double)valore/1000;
    }
}
