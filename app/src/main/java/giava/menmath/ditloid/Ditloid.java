package giava.menmath.ditloid;

/**
 * Created by emanuele on 08/02/16.
 */
public class Ditloid {

    private Integer id;

    private String enigma;

    private String solution;

    private String category;

    private String hint;

    private int difficulty;

    public Ditloid() {
    }

    public Ditloid(String enigma, String solution, String category, String hint, int difficulty) {
        this.enigma = enigma;
        this.solution = solution;
        this.category = category;
        this.hint = hint;
        this.difficulty = difficulty;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnigma() {
        return enigma;
    }

    public void setEnigma(String enigma) {
        this.enigma = enigma;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
