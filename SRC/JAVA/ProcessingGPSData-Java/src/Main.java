/**
 * The main class of the application.
 *
 * This application takes in two GPS .dat
 * files and extracts data from them in
 * order to create a completed route, represented
 * by a GPX file.
 *
 * There are two GPS .dat data files so
 * that one file/stream can support the
 * other file/stream.
 *
 * Created by jee22 on 05/03/14.
 */
public class Main {

    /**
     * Create the application.
     */
    private static DataCoordinator application = new DataCoordinator();

    /**
     * Run the application.
     */
    private static void runApplication(){
        application.run();
    }

    public static void main(String args[]){
        runApplication();
    }
}
