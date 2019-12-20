package agh.cs.proj1.main;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;


public class Animal extends AbstractWorldMapElement{
    private MapDirection  direction;
    private int [] genes = new int[32];
    private int energy;
    private int animalID;
    private static int startEnergy;
    private static int moveEnergy;
    public static int plantEnergy;
    private static int ID = 0;


    public static void initConstants(int startEnergy, int moveEnergy, int plantEnergy){
        Animal.startEnergy = startEnergy;
        Animal.moveEnergy = moveEnergy;
        Animal.plantEnergy = plantEnergy;
    }

    Animal(GrassField map){
        this.direction = MapDirection.NORTH;
        this.map = map;
        this.position = this.map.randomPosition(this.map.getLowerLeft(), this.map.getUpperRight());
        for (int i = 0; i < 32; i++){
            this.genes[i] = (new Random()).nextInt(8);
        }
        this.energy = startEnergy;
        Animal.ID++;
        this.animalID = Animal.ID;
    }

    Animal(int energy, Vector2d position, int [] genes){
        this.energy = energy;
        this.position = position;
        this.genes = genes;
        Animal.ID++;
        this.animalID = Animal.ID;
    }

    public int getEnergy() { return this.energy; }
    public GrassField getMap(){
        return this.map;
    }
    public MapDirection getDirection(){
        return this.direction;
    }
    public int [] getGenes() { return this.genes; }
    public String toLongString(){
        return String.format("%s %s", this.position.toString(),  this.direction.toString());
    }
    public String toString(){
        return this.direction.toShortString();
    }

    public int getNewDirection(){
        return this.genes[new Random().nextInt(32)];
    }

    public void turn(int deg){
        this.direction = this.direction.next(deg);
    }

    public boolean move(){
        if (this.energy < moveEnergy) return false;
        this.position = this.position.add(Objects.requireNonNull(this.direction.toUnitVector()));
        this.energy -= moveEnergy;
        return true;
    }

    public void eatGrass(int competitorNumber){
        this.energy += plantEnergy/competitorNumber;
    }

    public int makeOffspring(){
        this.energy *=  3 / 4;
        return this.energy/4;
    }

    public boolean isAbleToReproduction(){
        return  (this.energy >= 0.5 *  Animal.startEnergy);
    }

    public static int [] getGenes (Animal father, Animal mother){
        int [] fathersGenes = father.getGenes();
        int [] mothersGenes = mother.getGenes();
        int interval1 = (new Random()).nextInt(30);
        int interval2 = (new Random()).nextInt(32 - interval1);
        int [] newGenes = new int[32];
        int motherCount = 0;
        int fatherCount = 0;
        // the first part
        if ((new Random()).nextInt(2) == 0){
            for (int i = 0; i < interval1; i++)
                newGenes[i] = fathersGenes[i];
            fatherCount++;
        }
        else{
            for (int i = 0; i < interval1; i++)
                newGenes[i] = mothersGenes[i];
            motherCount++;
        }
        // the second part
        if ((new Random()).nextInt(2) == 0){
            for (int i = interval1; i < interval2; i++)
                newGenes[i] = fathersGenes[i];
            fatherCount++;
        }
        else{
            for (int i = interval1; i < interval2; i++)
                newGenes[i] = mothersGenes[i];
            motherCount++;
        }
        // the third part
        // these checks are for the case when the first two parts were taken from one parent,
        // because we must have two parts from one parent and one part from another,
        // so we cannot lead to case, when the child has three part of genes from the only one parent
        if (motherCount == 0){
            for (int i = interval2; i < 32; i++)
                newGenes[i] = mothersGenes[i];
        }
        else if (fatherCount == 0){
            for (int i = interval2; i < 32; i++)
                newGenes[i] = fathersGenes[i];
        }
        // if the first two parts were taken from different parents we haven't got any problems,
        // so we may do it like earlier
        else if ((new Random()).nextInt(2) == 0){
            for (int i = interval2; i < 32; i++)
                newGenes[i] = fathersGenes[i];
        }
        else{
            for (int i = interval2; i < 32; i++)
                newGenes[i] = mothersGenes[i];
        }
        Arrays.sort(newGenes, 0, 31);

        // check if every turn is included
        LinkedList<Integer> notExistingTurns = new LinkedList<>();
        for (int i = 0; i < 31; i++)
            if (newGenes[i + 1] > newGenes[i] + 1){
                int j = newGenes[i] + 1;
                while (j < newGenes[i + 1]) notExistingTurns.add(j);
            }
        if (notExistingTurns.size() != 0){
            // we can't set these genes to random positions,
            // because we may have case, when the turn on this random position is the only value of such turn,
            // so we may add eliminated turn, but at the same time eliminate the new turn.
            // it is not effective to check it after every random attempt, so here is proposal of an algorithm,
            // which find a turn, which is the most countable, and then set new values to some of its positions
            int [] count = {0, 0, 0, 0, 0, 0, 0, 0};
            for (int i = 0; i < 32; i++)
                count[newGenes[i]]++;
            int max = count[0];
            int maxCountableTurn = 0;
            for (int i = 1; i < 8; i++)
                if (count[i] > max){
                    max = count[i];
                    maxCountableTurn = i;
                }
            int changeFromPosition = 0;
            while (newGenes[changeFromPosition] != maxCountableTurn) changeFromPosition++;
            for (int turn : notExistingTurns){
                newGenes[changeFromPosition] = turn;
                changeFromPosition++;
            }
            Arrays.sort(newGenes, 0, 31);
        }
        return newGenes;
    }

    public void setPosition(Vector2d position){
        this.position = position;
    }
}