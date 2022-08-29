package edu.eci.blacklist.blacklistvalidator;
import edu.eci.blacklist.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;

public class Threadblacklistvalidator extends Thread{
    private int checkedListsCount,a,b;
    private static int ocurrencesCount;
    private static Integer countBlacks = 0;
    private String ipaddress;
    private HostBlacklistsDataSourceFacade skds;
    private LinkedList<Integer> blackListOcurrences = new LinkedList<>();

    /**
     * Constructor de Threadblacklistvalidator
     * @param ipaddress ipaddress
     * @param a minimo
     * @param b maximo
     * @param skds Clase HostBlacklistsDataSourceFacade para la verificacion en las listas negras
     */
    public Threadblacklistvalidator(String ipaddress, int a, int b, HostBlacklistsDataSourceFacade skds){
        this.ipaddress = ipaddress;
        this.a = a;
        this.b = b;
        this.skds = skds;
    }

    /**
     * Getters y Setters de los atributos de la clase Threadblacklistvalidator
     */

    public int getCheckedListsCount() {
        return this.checkedListsCount;
    }

    public void setCheckedListsCount(int checkedListsCount) {
        this.checkedListsCount = checkedListsCount;
    }

    public int getOcurrencesCount() {
        return ocurrencesCount;
    }

    public int getA() {
        return this.a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return this.b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public String getIpaddress() {
        return this.ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public HostBlacklistsDataSourceFacade getSkds() {
        return this.skds;
    }

    public void setSkds(HostBlacklistsDataSourceFacade skds) {
        this.skds = skds;
    }

    public LinkedList<Integer> getBlackListOcurrences() {
        return this.blackListOcurrences;
    }

    public void setBlackListOcurrences(LinkedList<Integer> blackListOcurrences) {
        this.blackListOcurrences = blackListOcurrences;
    }
    
    /**
     * Funcion que se encarga de la verificacion de si una Ipaddress esta en la lista negra
     */
    public void run(){
        synchronized(blackListOcurrences){
            synchronized(countBlacks){
                for(int i=a;i<=b;i++){
                    checkedListsCount++;
                    if(skds.isInBlackListServer(i, ipaddress)){
                        blackListOcurrences.add(i);
                        ocurrencesCount++;
                        countBlacks =+ 1;
                    }
                }
                try {
                    if(countBlacks == HostBlackListsValidator.getBlackListAlarmCount()){
                        blackListOcurrences.wait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
