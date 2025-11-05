package by.march8.ecs.application.modules.references.classifier.model;

import by.march8.ecs.application.modules.references.classifier.ui.ClassifierTree;

public class ClassifierParser {


    public static void prepareSetInformation(ProductCategoryItem item) {
        String name = item.getRawName().toLowerCase();
        boolean isKit = false;

        if (name.contains("компл")) {
            isKit = true;
        }

        if (isKit) {
            item.setSet(true);
            String amount = name.replaceAll("\\D+", "");
            int size = -1;
            if (!amount.isEmpty()) {
                size = Integer.valueOf(amount);
            }
            item.setSetSize(size);

            // разберем, что это за комплект
            item.setCategory(getSetCategory(item));

        } else {
            String amount = name.replaceAll("\\D+", "");
            int size = -1;
            if (!amount.isEmpty()) {
                size = Integer.valueOf(amount);
                item.setSetSize(size);
                item.setSet(true);

                // разберем, что это за комплект
                item.setCategory(getSetCategory(item));
            }
        }
    }

    public static String getSetCategory(ProductCategoryItem item) {
        String[] kit = item.getRawName().toLowerCase().replace("  ", " ").split(" ");
        if (kit.length > 1) {
            if (kit[1].contains("бель")) {
                return "Белье";
            }

            if (kit[1].contains("спорт")) {
                return "Спортивный";
            }

            return item.getClassifierGroup();
        }

        return null;
    }

    private void prepareItemCategory(ProductCategoryItem item) {
        item.setName(ItemNameReplacer.transform(item.getRawName()));
        String name = item.getRawName().toLowerCase().replace("  ", " ");
        String[] kit = item.getRawName().toLowerCase().replace("  ", " ").split(" ");
        if (kit.length > 0) {
            if (!item.isSet()) {
                if (name.contains("фартук")) {
                    item.setCategory("Фартук");
                } else if (name.contains("фуфайка")) {
                    item.setCategory("Фуфайка");
                } else if (name.contains("брюки")) {
                    if (name.contains("ниж")) {
                        item.setCategory("Брюки нижние");
                    } else if (name.contains("спор")) {
                        item.setCategory("Брюки спортивные");
                    } else if (name.contains("пижам")) {
                        item.setCategory("Брюки пижамные");
                    } else {
                        item.setCategory("Брюки");
                    }
                } else if (name.contains("джемп")) {
                    if (name.contains("спор")) {
                        item.setCategory("Джемпер спортивный");
                    } else {
                        item.setCategory("Джемпер");
                    }

                } else if (name.contains("костюм")) {
                    if (name.contains("купальн")) {
                        item.setCategory("Костюм купальный");
                    } else if (name.contains("спорт")) {
                        item.setCategory("Костюм спортивный");
                    } else {
                        item.setCategory("Костюм");
                    }
                } else if (name.contains("куртка")) {
                    if (name.contains("спорт")) {
                        item.setCategory("Куртка спортивная");
                    } else {
                        item.setCategory("Куртка");
                    }
                } else if (name.contains("майк")) {
                    if (name.contains("спорт")) {
                        item.setCategory("Майка спортивная");
                    } else if (name.contains("-")) {
                        item.setCategory("Майка");
                    } else {
                        item.setCategory("Майка");
                    }
                } else if (name.contains("получулк")) {
                    if (name.contains("спорт")) {
                        item.setCategory("Получулки спортивные");
                    } else {
                        item.setCategory("Получулки");
                    }
                } else if (name.contains("сорочк")) {
                    if (name.contains("ниж")) {
                        item.setCategory("Сорочка нижняя");
                    } else if (name.contains("ночн")) {
                        item.setCategory("Сорочка ночная");
                    } else {
                        item.setCategory("Сорочка");
                    }
                } else if (name.contains("трусы")) {
                    if (name.contains("купаль")) {
                        item.setCategory("Трусы купальные");
                    } else if (name.contains("спор")) {
                        item.setCategory("Трусы спортивные");
                    } else {
                        item.setCategory("Трусы");
                    }
                } else if (name.contains("футболка")) {
                    if (name.contains("спорт")) {
                        item.setCategory("Футболка спортивная");
                    } else {
                        item.setCategory("Футболка");
                    }
                } else if (name.contains("юбка")) {
                    if (name.contains("ниж")) {
                        item.setCategory("Юбка нижняя");
                    } else {
                        item.setCategory("Юбка");
                    }
                } else if (name.contains("шорты")) {
                    if (name.contains("спорт")) {
                        item.setCategory("Шорты спортивные");
                    } else {
                        item.setCategory("Шорты");
                    }
                } else {
                    item.setCategory(kit[0].substring(0, 1).toUpperCase() + kit[0].substring(1));

                    if (name.contains("ясел")) {
                        item.setCategory(item.getCategory() + ", ясельный");
                    }
                }
            }
        }

    }

    private String getGender(String rawName, int code) {
        String name = rawName.toLowerCase();
        String gender = null;

        if (name.contains("муж")) {
            gender = ClassifierTree.GENDER_MAN;
        }

        if (name.contains("жен")) {
            gender = ClassifierTree.GENDER_WOMAN;
        }

        if (name.contains("дев")) {
            gender = ClassifierTree.GENDER_GIRL;
        }

        if (name.contains("мал")) {
            gender = ClassifierTree.GENDER_BOY;
        }

        if (name.contains("дет") || name.contains("ясе")) {
            gender = ClassifierTree.GENDER_UNKNOW;
        }

        if (gender == null) {
            gender = ClassifierTree.getGenderByArticleSegment(code);
        }

        return gender;
    }
}
