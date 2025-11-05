package by.march8.entities.materials;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;
import by.march8.entities.classifier.EquipmentItem;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NamedQueries({
        @NamedQuery(
                name = "getAllItems",
                query = "SELECT c FROM CanvasItem c ")
})

@Entity
@Table(name = "MAN_CANVAS")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
//@EntityControl(control = CanvasControlPanel.class, viewEntity = VCanvasItem.class)
public class CanvasItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;

    @Column(name = "CIPHER")
    @TableHeader(name = "Шифр",  sequence = 0)
    private int cipher;

    @Column(name = "ARTICLE")
    @TableHeader(name = "Арикул",  sequence = 1)
    private String article;

    @OneToOne(fetch = FetchType.EAGER, optional=false)
    @JoinColumn(name = "PARENT_ID")
    private CanvasItem parent;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional=false)
    @JoinColumn(name = "MAN_CANVAS_PARAMETERS_ID")
    private CanvasParameter canvasParameter;

    @OneToOne(fetch = FetchType.EAGER, optional=false)
    @JoinColumn(name = "REF_CANVAS_WEAVE_ID")
    private CanvasWeave canvasWeave;

    @OneToOne(fetch = FetchType.EAGER, optional=false)
    @JoinColumn(name = "REF_CANVAS_MODIFIERS_ID")
    private CanvasModifier canvasModifier;

    @OneToOne(fetch = FetchType.EAGER, optional=false)
    @JoinColumn(name = "REF_EQUIPMENT_ID")
    private EquipmentItem equipment;

    @OneToMany(mappedBy = "canvas", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private Set<CanvasComponent> components;

    @OneToMany(mappedBy = "canvas", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private Set<CanvasMarkingComposition> markingComponents;

    @Column(name = "NOTE")
    private String note;

    @TableHeader(name = "Ширина",  sequence = 2)
    public int getCanvasWidth() {
        return canvasParameter.getWidth();
    }

    @TableHeader(name = "Вес", sequence = 3)
    public int getCanvasWeight() {
        return canvasParameter.getWeight();
    }

    @TableHeader(name = "Модификатор",  sequence = 4)
    public String getModifierAbbrev() {
        return canvasModifier.getAbbreviation();
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getCipher() {
        return cipher;
    }

    public void setCipher(final int cipher) {
        this.cipher = cipher;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(final String article) {
        this.article = article;
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }


    public CanvasModifier getCanvasModifier() {
        return canvasModifier;
    }

    public void setCanvasModifier(final CanvasModifier canvasModifier) {
        this.canvasModifier = canvasModifier;
    }

    public CanvasWeave getCanvasWeave() {
        return canvasWeave;
    }

    public void setCanvasWeave(final CanvasWeave canvasWeave) {
        this.canvasWeave = canvasWeave;
    }

    public CanvasItem getParent() {
        return parent;
    }

    public void setParent(final CanvasItem parent) {
        this.parent = parent;
    }

    public CanvasParameter getCanvasParameter() {
        return canvasParameter;
    }

    public void setCanvasParameter(final CanvasParameter canvasParameter) {
        this.canvasParameter = canvasParameter;
    }

    public Set<CanvasComponent> getComponents() {
        return components;
    }

    public void setComponents(final Set<CanvasComponent> components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return article;
    }

    public Set<CanvasMarkingComposition> getMarkingComponents() {
        return markingComponents;
    }

    public void setMarkingComponents(final Set<CanvasMarkingComposition> markingComponents) {
        this.markingComponents = markingComponents;
    }

    public CanvasItem() {

    }



    public CanvasItem(CanvasItem canvasItem) {
        this.setCipher(canvasItem.getCipher());
        this.setArticle(canvasItem.getArticle());
        this.setParent(canvasItem.getParent());
        this.setEquipment(canvasItem.getEquipment());

        //this.setCanvasParameters(null);
        this.setCanvasWeave(canvasItem.getCanvasWeave());
        this.setCanvasModifier(canvasItem.getCanvasModifier());
        this.setNote(canvasItem.getNote());

        Set<CanvasComponent> componentSet = canvasItem.getComponents();
        components = new HashSet<>();
        for (CanvasComponent item : componentSet) {
            CanvasComponent newComponent = new CanvasComponent(item) ;
            newComponent.setCanvas(this);
            components.add(newComponent);
        }

        Set<CanvasMarkingComposition> compositionSet = canvasItem.getMarkingComponents();
        markingComponents = new HashSet<>();
        for (CanvasMarkingComposition item : compositionSet) {
            CanvasMarkingComposition newComposition = new CanvasMarkingComposition(item);
            newComposition.setCanvas(this);
            markingComponents.add(newComposition);
        }

        CanvasParameter newParameters = new CanvasParameter(canvasItem.getCanvasParameter());
        this.setCanvasParameter(newParameters);
    }

    public EquipmentItem getEquipment() {
        return equipment;
    }

    public void setEquipment(final EquipmentItem equipment) {
        this.equipment = equipment;
    }
}
