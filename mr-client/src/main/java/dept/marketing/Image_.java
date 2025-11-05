package dept.marketing;

public class Image_ {
    private int id;
    private int sar;
    private byte[] image;

    public Image_() {
    }

    public Image_(int id, int sar, byte[] image) {
        this.id = id;
        this.sar = sar;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getSar() {
        return sar;
    }

    public void setSar(int sar) {
        this.sar = sar;
    }
}
