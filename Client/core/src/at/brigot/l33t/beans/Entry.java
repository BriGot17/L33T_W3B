package at.brigot.l33t.beans;

import java.util.List;

@Deprecated
public class Entry {

    String entryName;
    Boolean directory;
    List<Entry> subEntries;
    String content;

    public Entry(String entryName, Boolean directory, List<Entry> subEntries) {
        this.entryName = entryName;
        this.directory = directory;
        this.subEntries = subEntries;
    }

    public Entry(String entryName, Boolean directory, String content){
        this.entryName = entryName;
        this.directory = directory;
        this.content = content;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public Boolean isDirectory() {
        return directory;
    }

    public void setDirectory(Boolean directory) {
        directory = directory;
    }

    public List<Entry> getSubEntries() {
        return subEntries;
    }

    public void setSubEntries(List<Entry> subEntries) {
        this.subEntries = subEntries;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
