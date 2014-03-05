/**
 * Created by jee22 on 05/03/14.
 */
public class DataCoordinator {

    private FileHandler files;

    DataCoordinator(){

    }

    protected void start(){
        files = new FileHandler();
        files.readGPS_data("gps_data/gps_1.dat");
    }
}
