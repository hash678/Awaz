package user;

/**
 * Created by Anjum on 7/22/2016.
 */
public class GetFriendRequests {
    String recusername;
    String sendusername;
    String status;
String sendersphotourl;

    public GetFriendRequests() {
    }
    public String getRecusername(){
        return recusername;
    }

    public String getSendusername(){
        return sendusername;
    }
    public String getSendersphotourl(){
        return sendersphotourl;
    }
    public void setSendersphotourl(String sendersphotourl){
        this.sendersphotourl=sendersphotourl;
    }
    public String getStatus(){
        return status;
    }
    public GetFriendRequests(String recusername, String sendusername, String status, String sendersphotourl) {
        this.recusername = recusername;
        this.sendusername = sendusername;
        this.status = status;
        this.sendersphotourl = sendersphotourl;
    }

    public void setRecusername(String recusername){
        this.recusername =recusername;
    }
    public void setSendusername(String sendusername){
        this.sendusername =sendusername;
    }

  public void setStatus(String status){
        this.status =status;
    }
}
