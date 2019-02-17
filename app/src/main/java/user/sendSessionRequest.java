package user;

public class sendSessionRequest {

    public String getPhotourl() {
        return photourl;
    }

    public String getSessionname() {
        return sessionname;
    }

    public String getUsernameofsessionuser() {
        return usernameofsessionuser;
    }

    public String getStatus() {
        return status;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public void setSessionname(String sessionname) {
        this.sessionname = sessionname;
    }

    public void setUsernameofsessionuser(String usernameofsessionuser) {
        this.usernameofsessionuser = usernameofsessionuser;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String photourl;

    public sendSessionRequest(String photourl, String sessionname, String usernameofsessionuser, String status) {
        this.photourl = photourl;
        this.sessionname = sessionname;
        this.usernameofsessionuser = usernameofsessionuser;
        this.status = status;
    }

    String sessionname;
    String usernameofsessionuser;
String status;

    public sendSessionRequest() {
    }
}
