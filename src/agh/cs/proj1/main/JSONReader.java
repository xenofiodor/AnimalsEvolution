package agh.cs.proj1.main;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JSONReader {
    public int width;
    public int height;
    public int startEnergy;
    public int plantEnergy;
    public int moveEnergy;
    public int animalNumber;
    public double jungleRatio;
    public JSONReader() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("parameters.json")){
            Object obj = jsonParser.parse(reader);
            JSONObject parameters = (JSONObject) obj;
            this.width = ((Long)parameters.get("width")).intValue();
            this.height = ((Long) parameters.get("height")).intValue();
            this.startEnergy = ((Long) parameters.get("startEnergy")).intValue();
            this.plantEnergy = ((Long) parameters.get("plantEnergy")).intValue();
            this.moveEnergy = ((Long) parameters.get("moveEnergy")).intValue();
            this.animalNumber = ((Long) parameters.get("animalNumber")).intValue();
            this.jungleRatio = (Double) parameters.get("jungleRatio");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}