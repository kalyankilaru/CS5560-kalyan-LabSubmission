import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.*;

/**
 * Created by chait on 07/06/2017.
 */
public class FileUtil {

    public String readFromFile(String path){
        String everything=null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            sb.append(line);
            while (line != null) {
                sb.append(System.lineSeparator());
                line = br.readLine();
                sb.append(line);
            }
            everything = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return everything;
    }

    public void writeInTOFile(String Path){

    }
}