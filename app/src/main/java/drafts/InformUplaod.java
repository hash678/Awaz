package drafts;

/**
 * Created by Anjum on 7/28/2016.
 */
public class InformUplaod {
    String username;
    int numofsongs;
    String song1;
    String song2;
    String song3;
    String song4;
    String recusername;

    public InformUplaod(String username,String recusername ,int numofsongs, String song1, String song2, String song3, String song4) {
        this.username = username;
        this.numofsongs = numofsongs;
        this.song1 = song1;
        this.song2 = song2;
        this.song3 = song3;
        this.song4 = song4;
        this.recusername= recusername;
    }

    public String getRecusername() {
        return recusername;
    }

    public void setRecusername(String recusername) {
        this.recusername = recusername;
    }

    public InformUplaod() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumofsongs() {
        return numofsongs;
    }

    public void setNumofsongs(int numofsongs) {
        this.numofsongs = numofsongs;
    }

    public String getSong1() {
        return song1;
    }

    public void setSong1(String song1) {
        this.song1 = song1;
    }

    public String getSong2() {
        return song2;
    }

    public void setSong2(String song2) {
        this.song2 = song2;
    }

    public String getSong3() {
        return song3;
    }

    public void setSong3(String song3) {
        this.song3 = song3;
    }

    public String getSong4() {
        return song4;
    }

    public void setSong4(String song4) {
        this.song4 = song4;
    }
}
