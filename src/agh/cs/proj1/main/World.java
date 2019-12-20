package agh.cs.proj1.main;


public class World {
    public static void main(String[] args){
        JSONReader jsonReader = new JSONReader();
        Animal.initConstants(jsonReader.startEnergy, jsonReader.moveEnergy, jsonReader.plantEnergy);
        GrassField field = new GrassField(jsonReader.width, jsonReader.height,
                                                                jsonReader.jungleRatio, jsonReader.animalNumber);
//        System.out.println(field.toString());
        field.run(10);
//        System.out.println(field.toString());
    }
}