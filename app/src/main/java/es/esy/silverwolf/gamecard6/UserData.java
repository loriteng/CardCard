package es.esy.silverwolf.gamecard6;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/7.
 */
//玩家資料庫
public class UserData {

    private String username;
    private Boolean animal;
    private long id;

    public UserData(){
    }

    public UserData(long id, String username){
        this.id = id;
        this.username = username;

    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getusername() {
        return username;
    }
    public void setusername(String username) {
        this.username = username;
    }
    public Boolean getAnimal() {
        return animal;
    }
    public void setanimal(Boolean animal) {
        this.animal = animal;
    }


}
