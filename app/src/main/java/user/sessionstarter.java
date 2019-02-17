package user;

public class sessionstarter {
    String activeuser1;
    String activeuser2;
    String status;

    public sessionstarter(String activeuser1, String activeuser2, String status) {
        this.activeuser1 = activeuser1;
        this.activeuser2 = activeuser2;
        this.status = status;
    }

    public sessionstarter() {
    }

    public String getActiveuser1() {
        return activeuser1;
    }

    public void setActiveuser1(String activeuser1) {
        this.activeuser1 = activeuser1;
    }

    public String getActiveuser2() {
        return activeuser2;
    }

    public void setActiveuser2(String activeuser2) {
        this.activeuser2 = activeuser2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
