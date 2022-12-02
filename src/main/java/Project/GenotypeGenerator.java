package Project;

import java.util.concurrent.ThreadLocalRandom;

public class GenotypeGenerator {

    /** Creates a random genotype of given legth for an animal
     *
     * @param n length of the genotype
     * @return int array with animals genotype
     */
    public int[] GenerateNew(int n) {
        int[] genotype = new int[n];
        for (int i = 0; i < n; i++) {
            genotype[i] = ThreadLocalRandom.current().nextInt(0, 8);
        }
        return genotype;
    }
    public int[] GenerateBaby(Animal a1, Animal a2) {
        int genLength = a1.getGenotype().length;
        int[] stronger;
        int[] weaker;
        float strongerEnergy;
        float weakerEnergy;

        int[] result = new int[genLength];
        if (a1.getEnergy() > a2.getEnergy()) {
            stronger = a1.getGenotype();
            strongerEnergy = a1.getEnergy();
            weaker = a2.getGenotype();
            weakerEnergy = a2.getEnergy();
        } else {
            stronger = a2.getGenotype();
            strongerEnergy = a2.getEnergy();
            weaker = a1.getGenotype();
            weakerEnergy = a1.getEnergy();
        }
        float totalEnergy = a1.getEnergy() + a2.getEnergy();
        int side = ThreadLocalRandom.current().nextInt(0, 2);
        if (side == 0) {
            for (int i = 0; i < genLength; i++) {
                if (i/genLength > strongerEnergy/totalEnergy) {
                    result[i] = stronger[i];
                } else {
                    result[i] = weaker[i];
                }
            }
        } else {
            for (int i = 0; i < genLength; i++) {
                if (i / genLength > weakerEnergy / totalEnergy) {
                    result[i] = weaker[i];
                } else {
                    result[i] = stronger[i];
                }
            }
        }
        return result;
    }
}

