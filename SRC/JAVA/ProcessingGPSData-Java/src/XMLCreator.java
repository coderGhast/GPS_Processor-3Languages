import java.util.Calendar;

/**
 * Created by jee22 on 05/03/14.
 */
public class XMLCreator {

    public String startXML(){
        StringBuilder sb = new StringBuilder();
        sb.append("<gpx version=\"1.0\"" +
                " \ncreator=\"ExpertGPS 1.1 - http://www.topografix.com\"" +
                " \nxmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " \nxmlns=\"http://www.topografix.com/GPX/1/0\"" +
                " \nxsi:schemaLocation=\"http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd\">");
        return sb.toString();
    }

    public String formatDateAndTime(Calendar date_and_time){
        StringBuilder sb = new StringBuilder();

        sb.append("<time>");
        sb.append(date_and_time.get(Calendar.YEAR));
        sb.append("-");
        sb.append(date_and_time.get(Calendar.MONTH)+1);
        sb.append("-");
        sb.append(date_and_time.get(Calendar.DAY_OF_MONTH));
        sb.append("T");
        sb.append(date_and_time.get(Calendar.HOUR_OF_DAY) + ":");
        sb.append(date_and_time.get(Calendar.MINUTE) + ":");
        sb.append(date_and_time.get(Calendar.SECOND));
        sb.append("Z</time>");

        return sb.toString();
    }

    public String formatLatitudeAndLongitude(String latitude, String longitude){
        StringBuilder sb = new StringBuilder();
        sb.append("<wpt lat=\"");
        sb.append(latitude + "\" lon=\"");
        sb.append(longitude + "\">");
        return sb.toString();
    }

    public String endWaypoint(){
        return "</wpt>";
    }

    public String formatElevation(String elevation){
        StringBuilder sb = new StringBuilder();
        sb.append("<ele>");
        sb.append(elevation);
        sb.append("</ele>");
        return sb.toString();
    }

    public String endXML(){
        StringBuilder sb = new StringBuilder();
        sb.append("</gpx>");
        return sb.toString();
    }
}
