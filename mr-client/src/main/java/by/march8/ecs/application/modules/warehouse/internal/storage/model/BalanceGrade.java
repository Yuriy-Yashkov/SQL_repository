package by.march8.ecs.application.modules.warehouse.internal.storage.model;

import by.march8.entities.storage.BalanceItem;

import java.util.ArrayList;
import java.util.List;

public class BalanceGrade {
    private int grade;
    private List<BalanceSize> sizes = null;


    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<BalanceSize> getSizes() {
        return sizes;
    }

    public void setSizes(List<BalanceSize> sizes) {
        this.sizes = sizes;
    }

    public void attachToBalance(BalanceItem item) {
        // Получаем росторазмер по цене
        getBalanceSizeByPrice(item);
    }


    private BalanceSize getBalanceSizeByPrice(BalanceItem item) {
        if (sizes == null) {
            sizes = new ArrayList<>();
        }

        for (BalanceSize size : sizes) {
            if (Double.compare(item.getPrice(), size.getPrice()) == 0) {
                // Заполняем в найденом
                size.attach(item);
                return size;
            }
        }

        BalanceSize size = new BalanceSize(item);
        sizes.add(size);
        return size;
    }


    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("\t").append(grade).append("\n");
        for (BalanceSize size : sizes) {
            out.append("\t").append("\t").append(size.toString()).append("\n");
        }
        return out.toString();
    }

    public BalanceSize getSizeGroupByPrice(double price) {
        if (sizes != null) {
            for (BalanceSize size : sizes) {
                if (Double.compare(price, size.getPrice()) == 0) {
                    return size;
                }
            }
        }
        return null;
    }
}
