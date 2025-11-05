package by.march8.entities.warehouse;

/**
 * @author Andy 13.07.2016.
 */
public class CargoSpaceItem {
    private int id ;
    private int documentId ;
    private int cargoSpace ;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final int documentId) {
        this.documentId = documentId;
    }

    public int getCargoSpace() {
        return cargoSpace;
    }

    public void setCargoSpace(final int cargoSpace) {
        this.cargoSpace = cargoSpace;
    }
}
