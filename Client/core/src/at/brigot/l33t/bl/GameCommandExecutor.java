package at.brigot.l33t.bl;

import at.brigot.l33t.GameClient;
import at.brigot.l33t.beans.Filesystem;
import at.brigot.l33t.beans.Node;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.LogLevel;

import java.util.Locale;

public class GameCommandExecutor extends CommandExecutor {

    private Console console;
    private GameClient client;

    /**
     * Constructor for GameCommandExecutor when GCE is registered to the console
     * @param console The console object it is registered to
     * @param client The GameClient object for ui interaction
     */
    public GameCommandExecutor(Console console, GameClient client){
        this.console = console;
        this.client = client;
    }

    /**
     * Method to show the contents of a txt file
     * @param Dateiname -> The name of the txt file
     */
    public void cat(String Dateiname){
        if(!client.currentDir.equals("lib")){
            console.log("No txt File found in this Directory.",LogLevel.ERROR);
            return;
        }
        if(!client.currentFilesystem.getFilesystem().getLib().containsKey(Dateiname)){
            console.log("No File with this name found.",LogLevel.ERROR);
            return;
        }
        client.cat_area.setText(client.currentFilesystem.getFilesystem().getLib().get(Dateiname).toString());
        client.getCATTable().setVisible(true);
        console.setVisible(false);
    }

    /**
     * Method to show and allow editing a txt file
     * @param Dateiname -> The name of the txt file
     */
    public void edit(String Dateiname){
        if(!client.currentDir.equals("lib")){
            console.log("No txt File found in this Directory.",LogLevel.ERROR);
            return;
        }
        if(!client.currentFilesystem.getFilesystem().getLib().containsKey(Dateiname)){
            console.log("No File with this name found.",LogLevel.ERROR);
            return;
        }
        client.editor_area.setText(client.currentFilesystem.getFilesystem().getLib().get(Dateiname).toString());
        client.currentFile = Dateiname;
        client.getEditorTable().setVisible(true);
        console.setVisible(false);
    }

    /**
     * Method for disconnecting from a file system
     */
    public void disconnect(){
        client.currentFilesystem = null;
        client.getFileSystemTable().setVisible(false);
        client.currentTable.setVisible(true);
        client.connected = false;
    }

    /**
     * Console command to connect to the local or an other filesystem
     * @param param console params
     */
    public void connect(String param){
        if(client.connected){
            console.log("It is not possible to connect to 2 Devices at once!", LogLevel.ERROR);
            return;
        }
        if(param.equals("local")){
            client.currentDir = "root";
            client.currentFilesystem = client.getFilesystem();
            try {
                System.out.println(client.getJson_parser().parseNodeToJSON(client.currentFilesystem, "dummy"));
            }catch (Exception e){
                e.printStackTrace();
            }
            cd("root");
            client.connected = true;
        }else if(client.getPossibleHosts().contains(param)){
            client.getJson_communicator().sendNodeReq(param);
            Node fs = (Node) client.getJson_communicator().receive();
            System.out.println(fs + "< filesystem");

            client.currentFilesystem = fs;
            cd("root");
            client.connected = true;
        }else{
            console.log("Not an valid connection!", LogLevel.ERROR);
            return;
        }
        client.currentTable.setVisible(false);
        client.getFileSystemTable().setVisible(true);
    }

    /**
     * Method to move between directories
     * @param Directoryname -> The name of the next Directory
     */
    public void cd(String Directoryname){
        if(client.currentFilesystem == null){
            console.log("Not connected to any Filesystem", LogLevel.ERROR);
            return;
        }
        if(client.currentDir.equals("root") && Directoryname.equals("..")){
            console.log("Directory not found!",LogLevel.ERROR);
            return;
        }
        if(client.currentFilesystem.getFilesystem().getRoot().keySet().contains(Directoryname)&&
                client.currentFilesystem.getFilesystem().getRoot().keySet().contains(client.currentDir)){
            console.log("Directory not found!",LogLevel.ERROR);
            return;
        }
        switch(Directoryname){
            case "root":
            case "..":
                client.filelist.setItems(new String[] {"att","def","lib"});
                client.currentDir = "root";
                break;
            case "att":
                client.filelist.setItems(client.currentFilesystem.getFilesystem().getAttacks().toArray());
                client.currentDir = "att";
                break;
            case "def":
                client.filelist.setItems(client.currentFilesystem.getFilesystem().getDefenses().toArray());
                client.currentDir = "def";
                break;
            case "lib":
                client.filelist.setItems(client.currentFilesystem.getFilesystem().getLib().keySet().toArray());
                client.currentDir = "lib";
                break;
            default:
                console.log("Directory not found!",LogLevel.ERROR);
        }
    }
}
