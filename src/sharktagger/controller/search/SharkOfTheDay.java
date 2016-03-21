package sharktagger.controller.search;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import api.jaws.Jaws;
import api.jaws.Shark;

public class SharkOfTheDay {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Shark mShark;

    private int indexFromDate(Date date) {
        String shaString = DATE_FORMATTER.format(date);
        return shaString.hashCode();
    }

    public SharkOfTheDay(Jaws jaws, Date date) {
        List<String> sharkNames = jaws.getSharkNames();

        String sharkName = sharkNames.get(indexFromDate(date) % sharkNames.size());
        mShark = jaws.getShark(sharkName);
    }

    public Shark getSharkOfTheDay() {
        return mShark;
    }
}
