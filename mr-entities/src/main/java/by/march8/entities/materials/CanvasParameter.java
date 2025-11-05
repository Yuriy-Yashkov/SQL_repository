package by.march8.entities.materials;

import by.march8.api.BaseEntity;
import by.march8.api.RedirectToEntityManager;
import by.march8.api.MarchDataSourceEnum;

import javax.persistence.*;

/**
 * The Class CanvasParams.
 *
 * @author andy-linux
 */
@Entity
@Table(name = "MAN_CANVAS_PARAMETERS")
@RedirectToEntityManager(redirectTo = MarchDataSourceEnum.DS_SQLMARCH8)
public class CanvasParameter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private int id;

    @Column(name = "WIDTH")
    private int width;

    @Column(name = "WEIGHT")
    private int weight;

    @Column(name = "SHRINKAGE_FACTOR")
    private float factorShrinkage;

    @Column(name = "CONVERSION_FACTOR")
    private float factorConversion;

    @Column(name = "LOSS_FACTOR")
    private float factorLoss;

    @Column(name = "STRANDS_QUANTITY")
    private int strandsQuantity;

    @Column(name = "STRANDS_PERCENTAGE")
    private float strandsPercentage;

    @Column(name = "TEMPLATES_LOSS")
    private float lossTemplates;

    @Column(name = "EDGE_LOSS")
    private float lossEdge;

    @Column(name = "FLAP_LOSS")
    private float lossFlap;

    @Column(name = "DEFECTS_LOSS")
    private float lossDefect;

    @Column(name = "KNITTING_LOSS")
    private float lossKnitting;

    @Column(name = "REWIND_LOSS")
    private float lossRewind;

    @Column(name = "PAINTING_LOSS")
    private float lossPainting;

    @Column(name = "NOTE")
    private String note;

    public CanvasParameter() {

    }

    public CanvasParameter(final CanvasParameter canvasParameter) {
        this.width = canvasParameter.getWidth();
        this.weight = canvasParameter.getWeight();
        this.factorShrinkage = canvasParameter.getFactorShrinkage();
        this.factorConversion = canvasParameter.getFactorConversion();
        this.factorLoss = canvasParameter.getFactorLoss();
        this.strandsQuantity = canvasParameter.getStrandsQuantity();
        this.strandsPercentage = canvasParameter.getStrandsPercentage();
        this.lossTemplates = canvasParameter.getLossTemplates();
        this.lossEdge = canvasParameter.getLossEdge();
        this.lossFlap = canvasParameter.getLossFlap();
        this.lossDefect = canvasParameter.getLossDefect();
        this.lossKnitting = canvasParameter.getLossKnitting();
        this.lossRewind = canvasParameter.getLossRewind();
        this.lossPainting = canvasParameter.getLossPainting();
        this.note = canvasParameter.getNote();
    }

    public String getNote() {
        return note;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public float getFactorShrinkage() {
        return factorShrinkage;
    }

    public void setFactorShrinkage(float coefficientContraction) {
        this.factorShrinkage = coefficientContraction;
    }

    public float getFactorConversion() {
        return factorConversion;
    }

    public void setFactorConversion(float coefficientRecalculate) {
        this.factorConversion = coefficientRecalculate;
    }

    public float getFactorLoss() {
        return factorLoss;
    }

    public void setFactorLoss(float coefficientLoss) {
        this.factorLoss = coefficientLoss;
    }

    public int getStrandsQuantity() {
        return strandsQuantity;
    }

    public void setStrandsQuantity(int threadsCount) {
        this.strandsQuantity = threadsCount;
    }

    public float getLossTemplates() {
        return lossTemplates;
    }

    public void setLossTemplates(float wasteStencil) {
        this.lossTemplates = wasteStencil;
    }

    public float getLossEdge() {
        return lossEdge;
    }

    public void setLossEdge(float wasteHem) {
        this.lossEdge = wasteHem;
    }

    public float getLossFlap() {
        return lossFlap;
    }

    public void setLossFlap(float wastePatch) {
        this.lossFlap = wastePatch;
    }

    public float getLossDefect() {
        return lossDefect;
    }

    public void setLossDefect(float wasteDefect) {
        this.lossDefect = wasteDefect;
    }

    public float getLossKnitting() {
        return lossKnitting;
    }

    public void setLossKnitting(float wasteKnitting) {
        this.lossKnitting = wasteKnitting;
    }

    public float getLossRewind() {
        return lossRewind;
    }

    public void setLossRewind(float wasteRewind) {
        this.lossRewind = wasteRewind;
    }

    public float getLossPainting() {
        return lossPainting;
    }

    public void setLossPainting(float wasteColoring) {
        this.lossPainting = wasteColoring;
    }

    public float getStrandsPercentage() {
        return strandsPercentage;
    }

    public void setStrandsPercentage(float threadPercent) {
        this.strandsPercentage = threadPercent;
    }

}
