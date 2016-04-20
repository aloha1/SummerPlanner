package planner.summer.sonle.summerplanner;

/**
 * Created by sonle on 4/11/16.
 */
public class DayForeCast {
    public DayForeCast(String day, String minTemp, String maxTemp) {
        this.day = day;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public String getDay() {
        return day;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    private String day;
    private String minTemp;
    private String maxTemp;
}
