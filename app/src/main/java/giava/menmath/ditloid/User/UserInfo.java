package giava.menmath.ditloid.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import giava.menmath.ditloid.Ditloid;

/**
 * Created by emanuele on 15/02/16.
 */
public  class UserInfo implements Serializable{

    private static UserInfo ourInstance = new UserInfo();

    private HashMap<Integer, ArrayList<Integer>> passedLevel;
    private Integer credit;

    public static UserInfo getInstance() {
        return ourInstance;
    }

    private UserInfo() {
        passedLevel = new HashMap<>();
        credit=4;
    }

    public HashMap<Integer, ArrayList<Integer>> addPassedDitloid(Integer level, Integer id)
            throws Exception {

        if(!passedLevel.containsKey(level)){
            ArrayList<Integer> ditloidsId = new ArrayList<>();
            passedLevel.put(level, ditloidsId);
        }

        ArrayList<Integer> ids = passedLevel.get(level);
        if(!ids.contains(id)) {
            if(ids.add(id))
                passedLevel.put(level, ids);
            else
                throw new Exception();
        }

        return passedLevel;

    }

    public HashMap<Integer, ArrayList<Integer>> addPassedDitloid(Ditloid ditloid)
            throws Exception {

        return addPassedDitloid(ditloid.getLevel(), ditloid.getId() );

    }

    public Integer getLastPassedLevel(){

        if(passedLevel.isEmpty())
            return 1;

        return passedLevel.size();

    }

    public List<Integer> getPassedDitloids(Integer level){
        if(passedLevel.containsKey(level))
            return passedLevel.get(level);

        return null;
    }

    public Integer addCredit(Integer num){
        return credit += num;
    }

    public Integer subCredit(Integer num){
        return credit -= num;
    }

    public Integer getCredit() {
        return credit;
    }

}
