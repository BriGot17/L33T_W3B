package at.brigot.l33t.bl;

import at.brigot.l33t.beans.Client;
import at.brigot.l33t.beans.Entry;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.Console;

import java.util.List;


public class GameCommandExecutor extends CommandExecutor {

    private Console console;

    public GameCommandExecutor(Console console){
        this.console = console;
    }

    public void ls(Client client){
        List<Entry> filesystem = client.getContent();
        for (Entry entry: filesystem) {
            console.log(entry.getEntryName() + "\n");
        }
    }
    public void cat(Client client, String entryName){
        List<Entry> entries = client.getContent();
        for (Entry entry: entries) {
            if(entry.getEntryName() == entryName){
                if(entry.isDirectory()){
                    console.log("Error: Target is a directory");
                    return;
                }
                console.log(entry.getContent() + "\n");
                return;
            }
        }
    }
    public void portmap(Client client){
        List<Integer> ports = client.openPorts;
        for (int port: ports) {
            console.log(port+"\n");
        }
    }

    public void cd(Client target, Client client, String targetDir){
        List<Entry> targetEntries = target.getContent();
        for (Entry entry: targetEntries
             ) {
            if(entry.getEntryName().equals(targetDir)){
                if(!entry.isDirectory()){
                    console.log("Error: Target is not a directory");
                    return;
                }
                client.setPosOnTarget(entry.getEntryName());
            }
        }
    }
    public void cpd(Client target, Client client, String targetEntry){
        List<Entry> targetEntries = target.getContent();
        for (Entry entry: targetEntries) {
            if(entry.isDirectory()){
                List<Entry> subdirectory = entry.getSubEntries();
                for (Entry subEntry: subdirectory) {
                    if(subEntry.getEntryName().equals(targetEntry)){
                        copyToClient(client, subEntry, targetEntry);
                        /**
                        for (Entry clientEntry: client.getContent()) {
                            if(clientEntry.isDirectory()){
                                for (Entry clientSubEntry: clientEntry.getSubEntries()) {
                                    if(clientSubEntry.getEntryName().equals(targetEntry)){
                                        List<Entry> clientSubEntries = clientEntry.getSubEntries();
                                        clientSubEntries.add(subEntry);
                                        clientEntry.setSubEntries(clientSubEntries);
                                    }
                                }
                            }
                            if(clientEntry.getEntryName().equals(entry.getEntryName()) && entry.isDirectory()){
                                List<Entry> clientEntries = client.getContent();
                                clientEntries.add(subEntry);
                                client.setContent(clientEntries);
                            }
                        }
                        */
                    }
                }
            }
            if(entry.getEntryName().equals(targetEntry)){
                copyToClient(client, entry, targetEntry);
                /**for (Entry clientEntry: client.getContent()) {
                    if(clientEntry.getEntryName().equals(entry.getEntryName()) && entry.isDirectory()){
                        List<Entry> clientEntries = client.getContent();
                        clientEntries.add(entry);
                        client.setContent(clientEntries);
                    }
                }
                 */
            }
        }
    }
    public void dad(Client client, Client target, String targetEntry){
        List<Entry> targetEntries = target.getContent();
        Entry deleteEntry = null;
        for (Entry entry: targetEntries) {
            if(entry.isDirectory()){
                List<Entry> subdirectory = entry.getSubEntries();
                for (Entry subEntry: subdirectory) {
                    if(subEntry.getEntryName().equals(targetEntry)){
                        copyToClient(client, subEntry, targetEntry);
                        deleteEntry = subEntry;
                        /**
                         for (Entry clientEntry: client.getContent()) {
                         if(clientEntry.isDirectory()){
                         for (Entry clientSubEntry: clientEntry.getSubEntries()) {
                         if(clientSubEntry.getEntryName().equals(targetEntry)){
                         List<Entry> clientSubEntries = clientEntry.getSubEntries();
                         clientSubEntries.add(subEntry);
                         clientEntry.setSubEntries(clientSubEntries);
                         }
                         }
                         }
                         if(clientEntry.getEntryName().equals(entry.getEntryName()) && entry.isDirectory()){
                         List<Entry> clientEntries = client.getContent();
                         clientEntries.add(subEntry);
                         client.setContent(clientEntries);
                         }
                         }
                         */
                    }
                }
            }
            if(entry.getEntryName().equals(targetEntry)){
                copyToClient(client, entry, targetEntry);
                /**for (Entry clientEntry: client.getContent()) {
                 if(clientEntry.getEntryName().equals(entry.getEntryName()) && entry.isDirectory()){
                 List<Entry> clientEntries = client.getContent();
                 clientEntries.add(entry);
                 client.setContent(clientEntries);
                 }
                 }
                 */
                deleteEntry = entry;
            }
        }
        targetEntries.remove(deleteEntry);
        target.setContent(targetEntries);
    }
    public void FTPoline(){

    }
    public void webCry(){

    }
    public void RNGCaller(){

    }
    public void mailBreak(){

    }
    public void SQLShot(){

    }
    public void DNSKiller(){

    }
    public void FTPGuard(){

    }
    public void webSafe(){

    }
    public void telEncrypt(){

    }
    public void safeQuery(){

    }
    public void protNS(){

    }
    public void secureMail(){

    }

    /**
     * Helper method for copying files to callers system
     * @param client The command caller
     * @param entry The entry to be copied
     * @param targetEntry The targetentries name
     */
    public void copyToClient(Client client, Entry entry, String targetEntry){
        for (Entry clientEntry: client.getContent()) {
            if(clientEntry.isDirectory()){
                for (Entry clientSubEntry: clientEntry.getSubEntries()) {
                    if(clientSubEntry.getEntryName().equals(targetEntry)){
                        List<Entry> clientSubEntries = clientEntry.getSubEntries();
                        clientSubEntries.add(entry);
                        clientEntry.setSubEntries(clientSubEntries);
                        console.log("Successfully copied");
                        return;
                    }
                }
            }
            if(clientEntry.getEntryName().equals(entry.getEntryName()) && entry.isDirectory()){
                List<Entry> clientEntries = client.getContent();
                clientEntries.add(entry);
                client.setContent(clientEntries);
                console.log("Successfully copied");
                return;
            }
        }
        console.log("Error copying target");
    }


}
