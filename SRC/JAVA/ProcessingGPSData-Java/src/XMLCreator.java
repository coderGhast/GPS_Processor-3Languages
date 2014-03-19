import java.util.Calendar;

/**
 * Formats the passed Strings into their
 * specific XML tags for the intended
 * GPX file outputs.
 *
 * Created by jee22 on 05/03/14.
 */
public class XMLCreator {

    /**
     * The start of the GPX XML file.
     * @return The XML String.
     */
    public String startXML(){
        return "<gpx version=\"1.0\"" +
                " \ncreator=\"ExpertGPS 1.1 - http://www.topografix.com\"" +
                " \nxmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " \nxmlns=\"http://www.topografix.com/GPX/1/0\"" +
                " \nxsi:schemaLocation=\"http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd\">\n";
    }

    /**
     * Turn a Calendar object into XML for GPX.
     * @param date_and_time - The time to format.
     * @return The formatted String
     */
    public String formatDateAndTime(Calendar date_and_time){
        StringBuilder sb = new StringBuilder();

        sb.append("<time>");
        sb.append(date_and_time.get(Calendar.YEAR));
        sb.append("-");
        sb.append(date_and_time.get(Calendar.MONTH)+1);
        sb.append("-");
        sb.append(date_and_time.get(Calendar.DAY_OF_MONTH));
        sb.append("T");
        sb.append(date_and_time.get(Calendar.HOUR_OF_DAY));
        sb.append(":");
        sb.append(date_and_time.get(Calendar.MINUTE));
        sb.append(":");
        sb.append(date_and_time.get(Calendar.SECOND));
        sb.append("Z</time>");
        sb.append("\n");

        return sb.toString();
    }

    /**
     * Format Latitude and Longitude into tags.
     * @param latitude Latitude as a String
     * @param longitude Longitude as a String
     * @return The resulting XML String.
     */
    public String formatLatitudeAndLongitude(String latitude, String longitude){
        StringBuilder sb = new StringBuilder();
        sb.append("<wpt lat=\"");
        sb.append(latitude);
        sb.append("\" lon=\"");
        sb.append(longitude);
        sb.append("\">");
        sb.append("\n");

        return sb.toString();
    }

    /**
     * The end of a way point for the route tag.
     * @return The XML tag.
     */
    public String endWaypoint(){
        return "</wpt>\n";
    }

    /**
     * Put the elevation into correct XML tagging.
     * @param elevation The elevation to be tagged.
     * @return The tagged elevation.
     */
    public String formatElevation(String elevation){
        StringBuilder sb = new StringBuilder();
        sb.append("<ele>");
        sb.append(elevation);
        sb.append("</ele>");
        sb.append("\n");

        return sb.toString();
    }

    /**
     * The end of the GPX XML file tag.
     * @return The GPX XML end tag.
     */
    public String endXML(){
        return "</gpx>\n";
    }
}
