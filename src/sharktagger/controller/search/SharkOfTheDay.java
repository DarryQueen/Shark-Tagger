package sharktagger.controller.search;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import api.jaws.Jaws;
import api.jaws.Shark;

public class SharkOfTheDay {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    private Shark mShark;

    private int indexFromDate(Date date, int size) {
        String shaString = DATE_FORMATTER.format(date);
        int rawInt = shaString.hashCode() % size;
        rawInt += rawInt < 0 ? size : 0;

        return rawInt;
    }

    public SharkOfTheDay(Jaws jaws, Date date) {
        List<String> sharkNames = jaws.getSharkNames();
        Collections.sort(sharkNames);

        String sharkName = sharkNames.get(indexFromDate(date, sharkNames.size()));
        mShark = jaws.getShark(sharkName);
    }

    public Shark getSharkOfTheDay() {
        return mShark;
    }
}
