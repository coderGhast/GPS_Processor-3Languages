package utilities;

/**
 * @author - James Euesden <jee22@aber.ac.uk>
 * @version - 1.0
 */
public class DateAndTime implements Comparable {
    private double date;
    private double time;

    public DateAndTime(String time, String date){
        this.date = convertString(date);
        this.time = convertString(time);
    }


    private double convertString(String toConvert){
        return Double.parseDouble(toConvert);
    }

    private String appendDate(String stringDate){
        StringBuilder sb = new StringBuilder();
        sb.append(20);
        sb.append(stringDate.substring(4, 6));
        sb.append("-");
        sb.append(stringDate.substring(2, 4));
        sb.append("-");
        sb.append(stringDate.substring(0, 2));

        return sb.toString();
    }

    private String appendTime(String stringTime){
        StringBuilder sb = new StringBuilder();
        sb.append("T");
        sb.append(stringTime.substring(0,2));
        sb.append(":");
        sb.append(stringTime.substring(2,4));
        sb.append(":");
        sb.append(stringTime.substring(4,6));
        sb.append("Z");

        return sb.toString();
    }

    public double getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = convertString(date);
    }

    public double getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = convertString(time);
    }

    @Override
    public int compareTo(Object o) {
        DateAndTime comTemp = (DateAndTime) o;

        int result;

        if(comTemp.getDate() < this.getDate()){
            result = 1;
        } else if(comTemp.getDate() > this.getDate()){
            result = -1;
        } else {
            if(comTemp.getTime() < this.getTime()){
                result = 1;
            } else if (comTemp.getTime() > this.getTime()){
                result = -1;
            } else {
                result = 0;
            }
        }
        return result;
    }

    @Override
    public String toString(){
        return appendDate(String.valueOf(date)) + appendTime(String.valueOf(time));
    }
}
