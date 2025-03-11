package data;

import com.poiji.bind.Poiji;
import models.Item;

import java.io.File;
import java.util.List;

public class ExcelReader {
    private static final String excelPath = "src/test/resources/data/Items.csv";

    public static List<Item> readItemList() {
        return Poiji.fromExcel(new File(excelPath), Item.class);
    }
}
