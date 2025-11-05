package by.march8.ecs.application.modules.warehouse.external.shipping.model;

import by.march8.api.TableHeader;

public class MatrixContractor {
    private int id;
    private int code;
    private String name;
    private boolean orderByAddress;

    @TableHeader(name = "Код", width = -50, sequence = 10)
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @TableHeader(name = "Наименование", sequence = 20)
    public String getName() {
        if (name != null) {
            return name.trim();
        }

        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOrderByAddress() {
        return orderByAddress;
    }

    public void setOrderByAddress(boolean orderByAddress) {
        this.orderByAddress = orderByAddress;
    }

    @TableHeader(name = "п/п", width = -30, sequence = 1)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
