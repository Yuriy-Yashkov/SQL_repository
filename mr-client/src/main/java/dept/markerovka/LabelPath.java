package dept.markerovka;

/**
 *
 * @author Администратор
 * @date 27.03.2012
 */
public class LabelPath {
    int id;
    String path;
    String name;

    public LabelPath(int id, String path, String name) {
        this.id = id;
        this.path = path;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return name;
    }

}
