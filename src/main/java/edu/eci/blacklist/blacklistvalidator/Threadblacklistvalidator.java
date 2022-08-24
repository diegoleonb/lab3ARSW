package edu.eci.blacklist.blacklistvalidator;
import edu.eci.blacklist.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;

public class Threadblacklistvalidator extends Thread{
    private int checkedListsCount =0,ocurrencesCount =0,a,b;
    private String ipaddress;
    private HostBlacklistsDataSourceFacade skds;
    private LinkedList<Integer> blackListOcurrences = new LinkedList<>();

    /**
     * Constructor of Threadblacklistvalidator
     * @param ipaddress
     * @param a
     * @param b
     * @param skds
     */
    public Threadblacklistvalidator(String ipaddress, int a, int b, HostBlacklistsDataSourceFacade skds){
        this.ipaddress = ipaddress;
        this.a = a;
        this.b = b;
        this.skds = skds;
    }

    public int getCheckedListsCount() {
        return this.checkedListsCount;
    }

    public void setCheckedListsCount(int checkedListsCount) {
        this.checkedListsCount = checkedListsCount;
    }

    public int getOcurrencesCount() {
        return this.ocurrencesCount;
    }

    public void setOcurrencesCount(int ocurrencesCount) {
        this.ocurrencesCount = ocurrencesCount;
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
    
    public void run(){
        for(int i=a;i<b;i++){
            checkedListsCount++;
            if(skds.isInBlackListServer(i, ipaddress)){
                blackListOcurrences.add(i);
                ocurrencesCount++;

            }
        }
    }
    


    
}
