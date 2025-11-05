package by.march8.entities.materials;

import by.march8.api.BaseEntity;
import by.march8.api.EntityControl;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class Yarn.
 *
 * @author andy-linux
 */
@Entity
@Table(name = "MAN_YARN")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
@EntityControl(viewEntity = YarnItemView.class)
public class YarnItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REF_YARN_CATEGORY_ID")
    private YarnCategory category;
    @Column(name = "CODE")
    private int code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "VISIBLE")
    private boolean visible;
    @Column(name="COMPOSITE")
    private boolean composite ;

    @OneToMany(mappedBy = "yarn", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<YarnComponent> components = new HashSet<>();

    public String getYarnCategory() {
        return category.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public YarnCategory getCategory() {
        return category;
    }

    public void setCategory(final YarnCategory category) {
        this.category = category;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public Set<YarnComponent> getComponents() {
        return components;
    }

    public void setComponents(final Set<YarnComponent> component) {
        this.components = component;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        if (name==null){
            return "";
        }else {
            return name + '[' + code + ']';
        }
    }

    public String getComponentsAsHTML(){
        String componentList="<html>";
        for (YarnComponent item: getComponents()){
            componentList+="<p>"+item.getComponentName()+" - "+item.getComponentPercent()+"%</p>";
        }
        return componentList+"</html>";
    }

    public String getCategoryAsHTML(){
        String componentList="<html>";
        String[] array = getCategory().getCategoryParams().split(";");
        for (String item: array){
            componentList+="<p>"+item+"</p>";
        }
        return componentList+"</html>";
    }

    public boolean isComposite() {
        return composite;
    }

    public void setComposite(final boolean composite) {
        this.composite = composite;
    }
}
