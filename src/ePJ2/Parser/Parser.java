package ePJ2.Parser;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser{
    
    /**
     *Parsira podatke dobijene iz csv fajla
     *@param path putanja do csv fajla 
     *@return lista nizova stringova sa podacima iz csv fajla 
     */
    public static List<String[]> readData(String path){
        String line = "";
        List<String[]> data = new ArrayList<String[]>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            reader.readLine();
            while((line = reader.readLine()) != null){
                data.add(line.split(",(?=([^\"]|\"[^\"]*\")*$)"));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    } 
}
