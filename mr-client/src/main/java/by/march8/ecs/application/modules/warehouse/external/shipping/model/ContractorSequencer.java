package by.march8.ecs.application.modules.warehouse.external.shipping.model;

public class ContractorSequencer {
    private int id;
    private int code;
    private String name;
    private String address;
    private boolean subCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        if (name != null) {
            return name.trim();
        }
        return null;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        if (address != null) {
            return address.trim();
        }
        return null;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSubCode() {
        return subCode;
    }

    public void setSubCode(boolean subCode) {
        this.subCode = subCode;
    }
}
