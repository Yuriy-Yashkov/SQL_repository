package by.march8.ecs.application.modules.warehouse.internal.storage.model;

import by.march8.entities.storage.BalanceItem;

import java.util.ArrayList;
import java.util.List;

public class BalanceArticle {
    List<BalanceGrade> grades = null;
    private int articleCode;
    private String itemName;
    private int modelNumber;
    private String articleNumber;

    public BalanceArticle(BalanceItem item) {
        articleCode = item.getArticleCode();
        modelNumber = item.getModelNumber();
        articleNumber = item.getArticleNumber();
        itemName = item.getItemName();
    }

    public int getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(int articleCode) {
        this.articleCode = articleCode;
    }

    public List<BalanceGrade> getGrades() {
        return grades;
    }

    public void setGrades(List<BalanceGrade> grades) {
        this.grades = grades;
    }

    public BalanceGrade getGradeById(int gradeId) {
        if (grades == null) {
            grades = new ArrayList<>();
        }

        for (BalanceGrade grade : grades) {
            if (grade.getGrade() == gradeId) {
                return grade;
            }
        }

        BalanceGrade grade = new BalanceGrade();
        grade.setGrade(gradeId);
        grades.add(grade);
        return grade;
    }


    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(articleCode).append("\n");
        for (BalanceGrade grade : grades) {
            out.append("\t").append("\t").append(grade.toString());
        }
        return out.toString();
    }

    public String getItemName() {
        if (itemName == null) {
            return "";
        }
        return itemName.trim();
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(int modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public int getTotalAmount() {
        int amount = 0;
        for (BalanceGrade grade : getGrades()) {
            if (grade != null) {
                for (BalanceSize size : grade.getSizes()) {
                    amount += size.getAmount();
                }
            }
        }
        return amount;
    }

    public double getTotalCost() {
        double cost = 0;
        for (BalanceGrade grade : getGrades()) {
            if (grade != null) {
                for (BalanceSize size : grade.getSizes()) {
                    cost += size.getCost();
                }
            }
        }
        return cost;
    }

    public double getTotalAmountAccess() {
        int amount = 0;
        for (BalanceGrade grade : getGrades()) {
            if (grade != null) {
                for (BalanceSize size : grade.getSizes()) {
                    amount += size.getAmountAccess();
                }
            }
        }
        return amount;
    }

    public double getTotalCostAccess() {
        double cost = 0;
        for (BalanceGrade grade : getGrades()) {
            if (grade != null) {
                for (BalanceSize size : grade.getSizes()) {
                    cost += size.getCostAccess();
                }
            }
        }
        return cost;
    }

    public void fillCost(BalanceArticle item, BalanceGrade grade, BalanceSize size) {
        BalanceGrade bGrade = getGradeById(grade.getGrade());
        if (bGrade != null) {
            BalanceSize size_ = bGrade.getSizeGroupByPrice(size.getPrice());
            if (size_ != null) {
                size.setAmountAccess(size_.getAmount());
                size.setPriceAccess(size_.getPrice());
                size.setCostAccess(size_.getCost());
            }
        }
    }
}
