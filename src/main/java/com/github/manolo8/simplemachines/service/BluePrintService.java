package com.github.manolo8.simplemachines.service;

import com.github.manolo8.simplemachines.database.dao.BluePrintDao;
import com.github.manolo8.simplemachines.model.BluePrint;
import com.github.manolo8.simplemachines.utils.book.BookFactory;

import java.util.List;

public class BluePrintService {

    private List<BluePrint> bluePrints;

    public BluePrintService(List<BluePrint> bluePrints, BookFactory bookFactory) {
        this.bluePrints = bluePrints;

        for (BluePrint bluePrint : bluePrints) {
            bluePrint.setBook(bookFactory.generateBook(bluePrint));
        }
    }

    public BluePrint getBluePrint(String name) {
        for (BluePrint bluePrint : bluePrints) {
            if (bluePrint.getName().equals(name)) return bluePrint;
        }
        return null;
    }

    public List<BluePrint> getBluePrints() {
        return bluePrints;
    }
}
