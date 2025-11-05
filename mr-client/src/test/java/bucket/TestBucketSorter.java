package bucket;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Andy 07.12.2018 - 10:51.
 */
public class TestBucketSorter {


    @Test
    @Ignore
    public void testCreateBucketSort() {
        List<Item> result = prepare();
        sorting(result);
        for (Item item : result) {
            System.out.println(item);
        }
    }

    @Test
    @Ignore
    public void testCreateBucket() {
        List<Item> result = prepare();
        sorting(result);
        List<Bucket> buckets = new ArrayList<>();

        for (Item item : result) {
            // Если айтем еще не используется в бакетах, создаем новый бакет
            if (!item.isUsing()) {
                // Передаем бакету состав Айтема
                Bucket bucket = new Bucket(item);
                // Поиск подходящих по составу изделий
                searchSimilar(bucket, result);
                buckets.add(bucket);
                //System.out.println(item);
            }
        }

        for (Bucket bucket : buckets) {
            System.out.println(bucket);
        }
    }

    private void searchSimilar(Bucket bucket, List<Item> list) {
        int countInBucket = bucket.getSlotCount();

        for (Item item : list) {
            // Если айтем еще не используется в бакетах, создаем новый бакет
            if (!item.isUsing()) {
                if (bucket.addToBucket(item)) {
                    // System.out.println("Добавлен");
                }
            }
        }

    }


    private void sorting(List<Item> result) {
        for (Item item : result) {
            Collections.sort(item.getList(), new Comparator<ItemComponentValue>() {
                @Override
                public int compare(ItemComponentValue item2, ItemComponentValue item1) {
                    return Integer.valueOf(item2.getComponent().getId())
                            .compareTo(item1.getComponent().getId());
                }
            });
        }
    }

    public List<Item> prepare() {
        List<Item> result = new ArrayList<>();

        ItemComponent component = new ItemComponent(1, "БП-1");
        ItemComponent component1 = new ItemComponent(2, "БП-2");
        ItemComponent component2 = new ItemComponent(3, "БП-3");
        ItemComponent component3 = new ItemComponent(4, "БП-4");
        ItemComponent component4 = new ItemComponent(5, "БП-5");
        ItemComponent component5 = new ItemComponent(6, "БП-6");
        ItemComponent component6 = new ItemComponent(7, "БП-7");
        ItemComponent component7 = new ItemComponent(8, "БП-8");
        ItemComponent component8 = new ItemComponent(9, "БП-9");
        ItemComponent component9 = new ItemComponent(10, "БП-10");


        Item item = new Item(1, "Чулки");
        Item item1 = new Item(2, "Чулки1");
        Item item2 = new Item(3, "Чулки2");
        Item item3 = new Item(4, "Чулки3");
        Item item4 = new Item(5, "Чулки4");
        Item item5 = new Item(6, "Чулки5");
        Item item6 = new Item(7, "Чулки6");
        Item item7 = new Item(8, "Чулки7");
        Item item8 = new Item(9, "Чулки8");

        //item.addComponent(new ItemComponentValue(component, 0));
        //item.addComponent(new ItemComponentValue(component1, 0));
        item.addComponent(new ItemComponentValue(component2, 0));
        //item.addComponent(new ItemComponentValue(component3, 0));
        //item.addComponent(new ItemComponentValue(component4, 0));

        item1.addComponent(new ItemComponentValue(component, 1));
        item1.addComponent(new ItemComponentValue(component8, 1));
        item1.addComponent(new ItemComponentValue(component6, 1));
        item1.addComponent(new ItemComponentValue(component7, 1));
        item1.addComponent(new ItemComponentValue(component4, 1));

        item2.addComponent(new ItemComponentValue(component, 2));
        item2.addComponent(new ItemComponentValue(component2, 2));
        item2.addComponent(new ItemComponentValue(component1, 2));
        //item2.addComponent(new ItemComponentValue(component3, 2));
        item2.addComponent(new ItemComponentValue(component4, 2));

        item3.addComponent(new ItemComponentValue(component, 3));
        item3.addComponent(new ItemComponentValue(component2, 3));
        //item3.addComponent(new ItemComponentValue(component1, 3));
        item3.addComponent(new ItemComponentValue(component4, 3));
        item3.addComponent(new ItemComponentValue(component3, 3));

        item4.addComponent(new ItemComponentValue(component, 4));
        //item4.addComponent(new ItemComponentValue(component2, 4));
        //item4.addComponent(new ItemComponentValue(component1, 4));
        /// item4.addComponent(new ItemComponentValue(component3, 4));
        // item4.addComponent(new ItemComponentValue(component4, 4));

        item5.addComponent(new ItemComponentValue(component6, 5));
        item5.addComponent(new ItemComponentValue(component2, 5));
        item5.addComponent(new ItemComponentValue(component8, 5));
        item5.addComponent(new ItemComponentValue(component7, 5));
        item5.addComponent(new ItemComponentValue(component3, 5));

        //item6.addComponent(new ItemComponentValue(component2, 6));
        item6.addComponent(new ItemComponentValue(component, 6));
        item6.addComponent(new ItemComponentValue(component1, 6));
        item6.addComponent(new ItemComponentValue(component4, 6));
        item6.addComponent(new ItemComponentValue(component3, 6));

        item7.addComponent(new ItemComponentValue(component2, 7));
        item7.addComponent(new ItemComponentValue(component, 7));
        item7.addComponent(new ItemComponentValue(component3, 7));
        item7.addComponent(new ItemComponentValue(component9, 7));
        item7.addComponent(new ItemComponentValue(component4, 7));

        item8.addComponent(new ItemComponentValue(component, 8));
        item8.addComponent(new ItemComponentValue(component5, 8));
        item8.addComponent(new ItemComponentValue(component9, 8));
        item8.addComponent(new ItemComponentValue(component4, 8));
        item8.addComponent(new ItemComponentValue(component2, 8));


        result.add(item);
        result.add(item1);
        result.add(item2);
        result.add(item3);
        result.add(item4);
        result.add(item5);
        result.add(item6);
        result.add(item7);
        result.add(item8);

        return result;
    }

    private class Bucket {
        int id;
        List<ItemComponent> slots;
        List<Item> items;

        public Bucket(Item item_) {
            slots = new ArrayList<>();
            for (ItemComponentValue component : item_.getList()) {
                slots.add(component.getComponent());
            }
            items = new ArrayList<>();
            items.add(item_);
            item_.setUsing(true);
        }

        private boolean isComponentExist(ItemComponent item) {
            for (ItemComponent value : slots) {
                // если Компонента изделия нету в бакете - добавляем в темповый
                if (value.getId() == item.getId()) {
                    return true;
                }
            }
            return false;
        }

        private boolean addToBucket(Item item) {
            List<ItemComponent> tempList = new ArrayList<>();
            // просмотр компонентов изделия
            for (ItemComponentValue component : item.getList()) {
                // Просмотр компонентов в бакете
                if (!isComponentExist(component.getComponent())) {
                    tempList.add(component.getComponent());
                    //System.out.println("ADDED: "+component.getComponent().getName());
                }
            }

            if ((getSlotCount() + tempList.size()) <= 5) {
                for (ItemComponent component : tempList) {
                    slots.add(component);
                }
                item.setUsing(true);
                items.add(item);
                return true;
            } else {
                return false;
            }
        }


        @Override
        public String toString() {
            String title = "";
            for (ItemComponent item : slots) {
                title += item.getName() + "     \t";
            }
            String s = "ID \tNAME  \t" + title + "\n";
            s += "******************************************************************\n";

            for (Item item : items) {
                String row = item.getId() + " " + item.getName() + " \t";
                for (ItemComponent component : slots) {
                    String comp = "";
                    ItemComponentValue value = item.getComponentValue(component);
                    if (value != null) {
                        comp = value.getComponent().getName() + "(" + value.getValue() + ") \t";
                    } else {
                        comp = "          \t";
                    }
                    row += comp;
                }
                s += row + "\n";
            }
            return s;
        }


        public int getSlotCount() {
            return slots.size();
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ItemComponent> getSlots() {
            return slots;
        }

        public void setSlots(List<ItemComponent> slots) {
            this.slots = slots;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

    }


    public class Item {
        int id;
        String name;
        List<ItemComponentValue> list;
        boolean using;

        public Item(int id, String name) {
            this.id = id;
            this.name = name;
            list = new ArrayList<>();
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ItemComponentValue> getList() {
            return list;
        }

        public void setList(List<ItemComponentValue> list) {
            this.list = list;
        }

        public void addComponent(ItemComponentValue component) {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(component);
        }

        public boolean isUsing() {
            return using;
        }

        public void setUsing(boolean using) {
            this.using = using;
        }

        @Override
        public String toString() {
            String result = id + " " + name + " \t";
            for (ItemComponentValue item : list) {
                result += item.getComponent().getName() + "\t";
            }

            return result;
        }

        public ItemComponentValue getComponentValue(ItemComponent component) {
            for (ItemComponentValue item : list) {
                if (component.getId() == item.getComponent().getId()) {
                    return item;
                }
            }
            return null;
        }
    }

    public class ItemComponentValue {
        ItemComponent component;
        int value;

        public ItemComponentValue(ItemComponent component, int value) {
            this.component = component;
            this.value = value;
        }

        public ItemComponent getComponent() {
            return component;
        }

        public void setComponent(ItemComponent component) {
            this.component = component;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public class ItemComponent {
        int id;
        String name;

        public ItemComponent(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
