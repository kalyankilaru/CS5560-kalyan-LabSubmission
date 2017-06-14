import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by USER on 13-06-2017.
 */
public class DataReader {

    public String readData(String filePath){
        String data = null;
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(filePath));
            StringBuilder strbld = new StringBuilder();

            String sentence = bfr.readLine();

            while(sentence != null)
            {
                strbld.append(sentence);
                strbld.append(System.lineSeparator());
                sentence = bfr.readLine();

            }

            data = strbld.toString();


        }

        catch (IOException e){
            e.printStackTrace();
        }

        return data;
    }

}
