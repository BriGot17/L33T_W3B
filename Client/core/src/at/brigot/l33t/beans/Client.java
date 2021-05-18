package at.brigot.l33t.beans;

import java.util.List;

public class Client {

    public String hostname;
    public String ip;
    public List<Entry> content;
    public List<Integer> openPorts;
    public String posOnTarget;

    public Client(String hostname, String ip, List<Entry> content, List<Integer> openPorts) {
        this.hostname = hostname;
        this.ip = ip;
        this.content = content;
        this.openPorts = openPorts;
        posOnTarget = "";
    }
    public Client(String hostname, String ip, List<Integer> openPorts) {
        this.hostname = hostname;
        this.ip = ip;
        this.openPorts = openPorts;
        posOnTarget = "";
    }


    public String getPosOnTarget() {
        return posOnTarget;
    }

    public void setPosOnTarget(String posOnTarget) {
        this.posOnTarget = posOnTarget;
    }

    public String getHostname() {
        return hostname;
    }

    public List<Integer> getOpenPorts() {
        return openPorts;
    }

    public void setOpenPorts(List<Integer> openPorts) {
        this.openPorts = openPorts;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<Entry> getContent() {
        return content;
    }

    public void setContent(List<Entry> content) {
        this.content = content;
    }
}
