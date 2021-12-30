package bgu.spl.net.srv;

import java.time.LocalDate;
import java.time.Period;

public class ClientInfo {

    private String username;
    private String password;
    private String birthday;
    private int age;
    private int numOfPosts;
    private int numOfFollowers;
    private int numOfFollowing;

    public ClientInfo(String _username, String _password, String _birthday){
        username = _username;
        password = _password;
        birthday = _birthday;
        age = birthdayToAge(birthday);
        numOfPosts = 0;
        numOfFollowers = 0;
        numOfFollowing = 0;
    }

    private int birthdayToAge(String birthday){
        int day = Integer.parseInt(birthday.substring(0,1));
        int month = Integer.parseInt(birthday.substring(2,3));
        int year = Integer.parseInt(birthday.substring(4,7));
        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.of(year,month,day);
        return Period.between(birthDate, today).getYears();
    }
}
