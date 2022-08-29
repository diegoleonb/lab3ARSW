package edu.eci.blacklist.blacklistvalidator;
import edu.eci.blacklist.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;

     /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress){
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        int ocurrencesCount=0;
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        int checkedListsCount=0;
        for (int i=0;i<skds.getRegisteredServersCount() && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)){
                blackListOcurrences.add(i);
                ocurrencesCount++;
            }
        }
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        return blackListOcurrences;
    }

    /**
     * Crea los hilos a partir de el numero de hilos, los grupos, la clase HostBlacklistsDataSourceFacade  y la direccion ip
     * @param N
     * @param secciones
     * @param skds
     * @param ipaddress
     * @return la lista de hilos
     */
    private ArrayList<Threadblacklistvalidator> createThreads(int N,int grupos, HostBlacklistsDataSourceFacade skds, String ipaddress){
        ArrayList<Threadblacklistvalidator> blackThread = new ArrayList<Threadblacklistvalidator>();
        for(int i = 0; i< grupos;i++){
            if(i<grupos){
                blackThread.add(new Threadblacklistvalidator(ipaddress,(i*grupos),((i+1)*grupos)-1,skds));
            }
            else if(grupos == N && skds.getRegisteredServersCount()%N != 0){
                 blackThread.add(new Threadblacklistvalidator(ipaddress,(i*grupos),skds.getRegisteredServersCount(),skds));
            }
            blackThread.get(i).start();
        }
        return blackThread;
    }

    /**
     * Metodo que verifica si una direccion se encuentra en las listas negras con la diferencia sobre el otro metodo que
     * se maneja hilos en esta funcion
     * @param ipaddress
     * @param N hilos
     * @return
     * @throws InterruptedException
     */
     public List<Integer> checkHost(String ipaddress,int N) throws InterruptedException{
         LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        int ocurrencesCount=0;
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();        
        int checkedListsCount=0;        
        int grupos = skds.getRegisteredServersCount()/N;        
        ArrayList<Threadblacklistvalidator> blackThread  = createThreads(N, grupos, skds, ipaddress);
        synchronized(blackListOcurrences){
            for(Threadblacklistvalidator thread:blackThread){
            ocurrencesCount += thread.getOcurrencesCount();
            blackListOcurrences.addAll(thread.getBlackListOcurrences());
            checkedListsCount += thread.getCheckedListsCount();
            if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
            blackListOcurrences.wait();}
            else{
                skds.reportAsTrustworthy(ipaddress);
            }      
           }
        }
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        return blackListOcurrences;
    }

    /**
     * Getter de BLACK_LIST_ALARM_COUNT
     * @return
     */
    public static int getBlackListAlarmCount(){
        return BLACK_LIST_ALARM_COUNT;
    }

    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());   
}
