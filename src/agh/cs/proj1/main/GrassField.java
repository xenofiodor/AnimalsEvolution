package agh.cs.proj1.main;

import java.util.*;

public class GrassField{
    private Vector2d lowerLeft = new Vector2d(0,0);
    private Vector2d upperRight;
    private Vector2d jungleLowerLeft;
    private Vector2d jungleUpperRight;
    private LinkedList<Animal> animals = new LinkedList<>();
    private Map<Vector2d,Grass> grass = new HashMap<>();
    private MapVisualizer visualisation = new MapVisualizer(this);

    public GrassField(int width, int height, double jungleRatio, int animalNumber){
        // established that if field's width and length is a and b,
        // and jungle's width and length is c and d,
        // then jungleRatio is jungleRatio = c/a = d/b
        this.upperRight = new Vector2d(width, height);
        this.jungleLowerLeft = new Vector2d((int) (this.upperRight.x * ((1 - jungleRatio) / 2)) ,
                (int) (this.upperRight.y * ((1 - jungleRatio) / 2)));
        this.jungleUpperRight = new Vector2d((int) (this.upperRight.x - this.jungleLowerLeft.x),
                (int) (this.upperRight.y - this.jungleLowerLeft.y));
        for (int i = 0; i < animalNumber; i++){
            Animal animal = new Animal(this);
            this.place(animal);
            this.grassGrowing();
        }

    }

    public boolean isOccupiedByAnimals(Vector2d position) {
        for (Animal animal : this.animals) {
            if (animal.getPosition().equals(position))
                return true;
        }
        return false;
    }

    public boolean isGrassAtPosition(Vector2d position){
        for (Map.Entry<Vector2d, Grass> vector2dGrassEntry : this.grass.entrySet()) {
            if (((Map.Entry) vector2dGrassEntry).getKey().equals(position))
                return true;
        }
        return false;
    }

    public LinkedList<Animal> animalsAt (Vector2d position) {
        LinkedList<Animal> objects = new LinkedList<>();
        for (Animal animal : this.animals) {
            if (animal.getPosition().equals(position))
                objects.add(animal);
        }
        return objects;
    }

    private void place(Grass grass){
        Vector2d position = grass.getPosition();
        if(!this.isOccupiedByAnimals(position) && !this.isGrassAtPosition(position)){
            this.grass.put(position, grass);
        }
    }

    public void place(Animal animal) {
        this.animals.add(animal);
    }

    public String toString(){
        return (this.visualisation.draw(this.lowerLeft,
                new Vector2d(this.upperRight.x - 1, this.upperRight.y - 1)));
        // we subtract 1 because of counting from 0 in MapVisualizer
    }

    public void eatGrassFromPosition (Vector2d position){
        this.grass.remove(position);
    }

    public void removeDiedAnimal (Animal diedAnimal){
        this.animals.remove(diedAnimal);
    }

    public Vector2d randomPosition(Vector2d lowerLeft, Vector2d upperRight){
        int x = (int)(new Random().nextInt(upperRight.x - lowerLeft.x + 1) + lowerLeft.x);
        int y = (int)(new Random().nextInt(upperRight.y - lowerLeft.y + 1) + lowerLeft.y);
        return new Vector2d(x, y);
    }

    public Vector2d getLowerLeft(){
        return this.lowerLeft;
    }

    public Vector2d getUpperRight(){
        return this.upperRight;
    }


    public void run(int dayNumber) {
        for (int i = 0; i < dayNumber; i++){
            System.out.println("day " + i);
            this.grassGrowing();
            LinkedList<Animal> diedAnimals = new LinkedList<>();

            // change a direction of all animals, then move them
            for (Animal animal : this.animals){
                animal.turn(animal.getNewDirection());
                if(animal.move())
                    this.updateAnimalPosition(animal);
                else diedAnimals.add(animal);
            }
            for (Animal animal : diedAnimals){
                this.removeDiedAnimal(animal);
            }
            if (this.animals.size() != 0) {
                this.animals.sort(new Comparator<Animal>() { // sort animals by their position
                    @Override
                    public int compare(Animal animal, Animal t1) {
                        Vector2d position1 = animal.getPosition();
                        Vector2d position2 = t1.getPosition();
                        if (position1.equals(position2)) return 0;
                        if (position1.x >= position2.x) return 1;
                        if (position1.x < position2.x) return -1;
                        if (position1.y >= position2.y) return 1;
                        if (position1.y < position2.y) return -1;
                        return 0;
                    }
                });
                LinkedList<Vector2d> positions = new LinkedList<>();
                Vector2d lastPosition = this.animals.getFirst().getPosition();
                for (Animal animal : this.animals){     // reading all the positions without repetition
                    Vector2d tempPosition = animal.getPosition();
                    if (!tempPosition.equals(lastPosition))
                        positions.add(tempPosition);
                }
                for (Vector2d position : positions){
                    LinkedList<Animal> tempAnimals = this.animalsAt(position);
                    if (tempAnimals.size() == 1) {
                        if (this.isGrassAtPosition(position)){
                            tempAnimals.getFirst().eatGrass(1);
                            this.eatGrassFromPosition(position);
                        }
                    }
                    else {
                        tempAnimals.sort(new Comparator<Animal>() { // sort by energy descending
                            @Override
                            public int compare(Animal animal, Animal t1) {
                                return t1.getEnergy() - animal.getEnergy();
                            }
                        });
                        if (this.isGrassAtPosition(position)){
                            int mostPowerfulCount = 0;
                            int maxEnergy = tempAnimals.getFirst().getEnergy();
                            for (Animal animal : tempAnimals){
                                if (animal.getEnergy() == maxEnergy)
                                    mostPowerfulCount++;
                                else break;
                            }
                            for (int j = 0; j < mostPowerfulCount; j++){
                                tempAnimals.get(j).eatGrass(mostPowerfulCount);
                            }
                            this.eatGrassFromPosition(position);
                        }
                        if (tempAnimals.get(1).isAbleToReproduction()){ // if the second animal able
                            // then the first animal is able too, because animals are sorted by energy descending
                            Animal father = tempAnimals.getFirst();
                            Animal mother = tempAnimals.get(1);
                            int childEnergy = 0;
                            childEnergy += father.makeOffspring();
                            childEnergy += mother.makeOffspring();
                            int [] childGenes = Animal.getGenes(father, mother);
                            Vector2d childPosition = position;
                            LinkedList<Vector2d> possiblePositions = this.neighborhoods(position);
                            for (Vector2d possiblePosition : possiblePositions){
                                if (!this.isOccupiedByAnimals(possiblePosition)){
                                    childPosition = possiblePosition;
                                    if (this.isGrassAtPosition(possiblePosition)){
                                        childEnergy += Animal.plantEnergy;
                                        this.eatGrassFromPosition(position);
                                    }
                                    break;
                                }
                            }
                            Animal child = new Animal(childEnergy, childPosition, childGenes);
                            this.place(child);
                        }
                    }
                }
            }
            System.out.println(this.toString());
        }

    }

    private LinkedList<Vector2d> neighborhoods(Vector2d position){
        LinkedList<Vector2d> neighborhoods = new LinkedList<>();
        Vector2d tempPosition = new Vector2d(position.x + 1, position.y + 1);
        if (tempPosition.precedes(this.upperRight) && tempPosition.follows(this.lowerLeft))
            neighborhoods.add(tempPosition);
        tempPosition = new Vector2d(position.x, position.y + 1);
        if (tempPosition.precedes(this.upperRight) && tempPosition.follows(this.lowerLeft))
            neighborhoods.add(tempPosition);
        tempPosition = new Vector2d(position.x - 1, position.y + 1);
        if (tempPosition.precedes(this.upperRight) && tempPosition.follows(this.lowerLeft))
            neighborhoods.add(tempPosition);
        tempPosition = new Vector2d(position.x - 1, position.y);
        if (tempPosition.precedes(this.upperRight) && tempPosition.follows(this.lowerLeft))
            neighborhoods.add(tempPosition);
        tempPosition = new Vector2d(position.x - 1, position.y - 1);
        if (tempPosition.precedes(this.upperRight) && tempPosition.follows(this.lowerLeft))
            neighborhoods.add(tempPosition);
        tempPosition = new Vector2d(position.x, position.y - 1);
        if (tempPosition.precedes(this.upperRight) && tempPosition.follows(this.lowerLeft))
            neighborhoods.add(tempPosition);
        tempPosition = new Vector2d(position.x + 1, position.y - 1);
        if (tempPosition.precedes(this.upperRight) && tempPosition.follows(this.lowerLeft))
            neighborhoods.add(tempPosition);
        tempPosition = new Vector2d(position.x + 1, position.y);
        if (tempPosition.precedes(this.upperRight) && tempPosition.follows(this.lowerLeft))
            neighborhoods.add(tempPosition);
        return neighborhoods;
    }

    public Grass getGrassFromPosition(Vector2d position){
        if (this.isGrassAtPosition(position))
            return this.grass.get(position);
        return null;
    }

    private void grassGrowing(){
        int jungleArea = (this.jungleUpperRight.x - this.jungleLowerLeft.x) *
                (this.jungleUpperRight.y - this.jungleLowerLeft.y);
        int fieldArea = (this.upperRight.x - this.lowerLeft.x) * (this.upperRight.y - this.lowerLeft.y);

        Vector2d randomJungle;
        Vector2d randomNotJungle;

        int testCounter = 0; // we need it for the check if the jungle or the field is not "filled"
        // the area is "filled" if the number of attempts is more that its area
        // in this case the program does not add new grass to the field
        do{
            randomJungle = this.randomPosition(this.jungleLowerLeft, this.jungleUpperRight);
            testCounter++;

        } while (this.isGrassAtPosition(randomJungle) && this.isOccupiedByAnimals(randomJungle)
                && testCounter < jungleArea);
        if (testCounter < jungleArea) // if the jungle is not filled add new grass
            this.place(new Grass(this, randomJungle));
        testCounter = 0;
        do {
            randomNotJungle = this.randomPosition(this.lowerLeft, this.upperRight);
            testCounter++;
        } while ((randomNotJungle.precedes(this.jungleLowerLeft) && randomNotJungle.follows(this.jungleUpperRight))&&
                this.isGrassAtPosition(randomNotJungle) && this.isOccupiedByAnimals(randomNotJungle)
                && testCounter < fieldArea); // here we check also if the position is not belong to jungle
        if (testCounter < fieldArea) // if the field is not filled add new grass
            this.place(new Grass(this,  randomNotJungle));
    }

    private void updateAnimalPosition(Animal animal){
        Vector2d position = animal.getPosition();
        animal.setPosition(new Vector2d((position.x + this.upperRight.x) % this.upperRight.x,
                (position.y + this.upperRight.y) % this.upperRight.y));
    }
}
