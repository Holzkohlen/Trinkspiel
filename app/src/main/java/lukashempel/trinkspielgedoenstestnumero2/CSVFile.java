package lukashempel.trinkspielgedoenstestnumero2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {

    private InputStream inputStream;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public List read(){
        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try{
            String csvLine;
            while((csvLine = reader.readLine()) != null){
                String[] row = csvLine.split(";");
                resultList.add(row);
            }
        }
        catch (IOException e) {e.printStackTrace();}
        finally{
            try{
                inputStream.close();
            }
            catch (IOException e){e.printStackTrace();}
        }
        return resultList;
    }
}
