import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Created by Pernille on 20/11/2017.
 */
public class Server {

    public static void main(String[] args){

        new MQTTHandler().start();

/*
        String state = Database.getCurrentState("1234567891");

        System.out.println(state);

        ArrayList<String> states = Database.getLoggedStates("1234567891");

        for (int i = 0 ; i < states.size();i++){
            System.out.println(states.get(i));
        }
*/


    }


}
