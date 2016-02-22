package giava.menmath.ditloid.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by MenMath.Giava
 * <p/>
 * This class keep information of the user. It memorize Level and ditloid solved, number of credits
 * get and other data. It's implement Serializable to be saved by
 * {@link UserDao#serializza(UserInfo)} and retrieve by {@link UserDao#deserializza()}
 *
 * @see UserDao
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

    /**
     * Add a new solved ditloid to be saved. It keeps level and number of fragment in witch it's
     * displayed
     */
    public void addPassedDitloid(Integer level, Integer num) {

        ArrayList<Integer> positions;

        if (!passedLevel.containsKey(level)) {
            positions = new ArrayList<>();
            positions.add(num);
            passedLevel.put(level, positions);
        } else {
            positions = passedLevel.get(level);
            positions.add(num);
            passedLevel.put(level, positions);
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
            return 0;

        return passedLevel.size();

    }

    public List<Integer> getPassedDitloids(Integer level) {
        if (passedLevel != null && passedLevel.containsKey(level))
            return passedLevel.get(level);

        return null;
    }

    public Integer getPassedDitloidsSize() {

        Integer sum = 0;

        if (passedLevel == null || passedLevel.isEmpty())
            return sum;

        Set<Integer> keys = passedLevel.keySet();
        for (Integer key : keys) {
            List<Integer> ditloids = passedLevel.get(key);
            if (ditloids != null && !ditloids.isEmpty()) {
                for (Integer dit : ditloids) {
                    if (dit != null)
                        sum++;
                }
            }
        }

        return sum;
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
