package at.brigot.l33t.beans;

import java.util.List;

public class Client {

    public String hostname;
    public String ip;
    public List<Integer> openPorts;
    public String posOnTarget;

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

}
