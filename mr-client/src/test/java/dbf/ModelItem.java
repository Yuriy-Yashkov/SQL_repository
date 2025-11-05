package dbf;

/**
 * @author Andy 27.04.2018 - 9:26.
 */
public class ModelItem {
    private int id;
    private String name;
    private int grade;
    private int size;

    public ModelItem(final String name, final int grade, final int size) {

        this.name = name;
        this.grade = grade;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(final int grade) {
        this.grade = grade;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return name + "\t" +
                "\t" + grade +
                "\t" + size;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }
}
