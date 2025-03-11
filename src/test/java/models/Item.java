package models;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelSheet;

@ExcelSheet("Items")
public class Item {
    @ExcelCellName("Item Name")
    private String name;
    @ExcelCellName("Price")
    private int price;

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
