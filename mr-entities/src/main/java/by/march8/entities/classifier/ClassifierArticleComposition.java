package by.march8.entities.classifier;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.TableHeader;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "ClassifierArticleComposition.findByModelNumber",
                query = "SELECT item FROM ClassifierArticleComposition item WHERE item.modelNumber = :modelNumber " +
                        "ORDER BY item.composition_1, item.composition_2, item.composition_3, item.composition_4 "
                        ),
        @NamedQuery(name = "ClassifierArticleComposition.findFromList",
                query = "SELECT item FROM ClassifierArticleComposition item WHERE item.modelNumber in :models " +
                        "ORDER BY  item.composition_1, item.composition_2, item.composition_3, item.composition_4 "
        )
})

/**
 * @author Andy 23.01.2019 - 11:52.
 */
@Entity
@Table(name="NSI_KLD_COMPOSITION")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class ClassifierArticleComposition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id ;
    @Column(name="MODEL_NUMBER")
    private int modelNumber;
    @Column(name="COMPOSITION_1")
    private String composition_1 ;
    @Column(name="COMPOSITION_2")
    private String composition_2 ;
    @Column(name="COMPOSITION_3")
    private String composition_3 ;
    @Column(name="COMPOSITION_4")
    private String composition_4 ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }
    @TableHeader(name = "Состав_1", sequence = 10)
    public String getComposition_1() {
        if(composition_1!=null) {
            return composition_1.trim();
        }
        return "";
    }

    public void setComposition_1(String composition_1) {
        this.composition_1 = composition_1;
    }
    @TableHeader(name = "Состав_2", sequence = 20)
    public String getComposition_2() {
        if(composition_2!=null) {
            return composition_2.trim();
        }
        return "";
    }

    public void setComposition_2(String composition_2) {
        this.composition_2 = composition_2;
    }
    @TableHeader(name = "Состав_3", sequence = 30)
    public String getComposition_3() {
        if(composition_3!=null) {
            return composition_3.trim();
        }
        return "";
    }

    public void setComposition_3(String composition_3) {
        this.composition_3 = composition_3;
    }
    @TableHeader(name = "Состав_4", sequence = 40)
    public String getComposition_4() {
        if(composition_4!=null) {
            return composition_4.trim();
        }
        return "";
    }

    public void setComposition_4(String composition_4) {
        this.composition_4 = composition_4;
    }

    public String getAllCompositions(){
        String result = "";

        if(composition_1!=null){
            if(!composition_1.trim().isEmpty()){
                result += composition_1.trim()+"_" ;
            }
        }

        if(composition_2!=null){
            if(!composition_2.trim().isEmpty()){
                result += composition_2.trim()+"_" ;
            }
        }

        if(composition_3!=null){
            if(!composition_3.trim().isEmpty()){
                result += composition_3.trim()+"_" ;
            }
        }

        if(composition_4!=null){
            if(!composition_4.trim().isEmpty()){
                result += composition_4.trim() ;
            }
        }

        return result.replace("_","\n");
    }
}
