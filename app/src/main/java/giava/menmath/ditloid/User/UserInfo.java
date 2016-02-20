package giava.menmath.ditloid.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import giava.menmath.ditloid.Ditloid;

/**
 * Created by emanuele on 15/02/16.
 */
public class UserInfo implements Serializable {

    private static UserInfo ourInstance = new UserInfo();

    /**
     * HashMap keeps track of solved ditloids, those for witch user asked for category or hekp
     * In each record, the key represents the level, the value is the position of the ditloid
     * in the fragments list.
     */
    private HashMap<Integer, ArrayList<Integer>> passedLevel;
    private HashMap<Integer, ArrayList<Integer>> categoryGet;
    private HashMap<Integer, ArrayList<Integer>> hintGet;

    private Integer credit;

    public static UserInfo getInstance() {
        return ourInstance;
    }

    private UserInfo() {
        passedLevel = new HashMap<>();
        categoryGet = new HashMap<>();
        hintGet = new HashMap<>();

        credit = 20;
    }

    public void addPassedDitloid(Integer level, Integer num) {

        ArrayList<Integer> positions;

        if (!passedLevel.containsKey(level)) {
            positions = new ArrayList<>();
            positions.add(num);
            passedLevel.put(level,positions);
        } else{
            positions = passedLevel.get(level);
            positions.add(num);
            passedLevel.put(level,positions);
        }

    }

    public void addCategoryGet(Integer level, Integer num) {

        ArrayList<Integer> positions;

        if (!categoryGet.containsKey(level)) {
            positions = new ArrayList<>();
            positions.add(num);
            categoryGet.put(level, positions);

        } else {
            positions = categoryGet.get(level);
            positions.add(num);
            categoryGet.put(level, positions);
        }
    }

    public void addHintGet(Integer level, Integer num) {
        ArrayList<Integer> positions;

        if (!hintGet.containsKey(level)) {
            positions = new ArrayList<>();
            positions.add(num);
            hintGet.put(level, positions);

        } else {
            positions = hintGet.get(level);
            positions.add(num);
            hintGet.put(level, positions);
        }

    }

    public Integer getLastPassedLevel() {

        if (passedLevel.isEmpty())
            return 1;

        return passedLevel.size();

    }

    public List<Integer> getPassedDitloids(Integer level) {
        if (passedLevel != null && passedLevel.containsKey(level))
            return passedLevel.get(level);

        return null;
    }

    public Integer addCredit(Integer num) {
        return credit += num;
    }

    public Integer subCredit(Integer num) {
        return credit -= num;
    }

    public Integer getCredit() {
        return credit;
    }

    public HashMap<Integer, ArrayList<Integer>> getPassedLevel() {
        return passedLevel;
    }

    public HashMap<Integer, ArrayList<Integer>> getCategoryGet() {
        return categoryGet;
    }

    public HashMap<Integer, ArrayList<Integer>> getHintGet() {
        return hintGet;
    }
}
