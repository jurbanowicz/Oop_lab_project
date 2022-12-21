package Project;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

public class GenotypeGenerator {
    private int mutationsNumber;
    private int mutationVariant;
    private int genotypeLength;

    public GenotypeGenerator(int genotypeLength, int mutationsNumber, int mutationVariant) {
        this.genotypeLength = genotypeLength;
        this.mutationsNumber = mutationsNumber;
        this.mutationVariant = mutationVariant;
    }

    /** Creates a random genotype of given legth for an animal
     * @return int array with animals genotype
     */
    public int[] GenerateNew() {
        int n = genotypeLength;
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

        return mutate(result);
    }
    private int[] mutate(int[] genotype) {
        if (mutationsNumber == 0) {
            return genotype;
        }
        if (mutationVariant == 0) {
            genotype = randomMutation(genotype);
        }
        else {
            genotype = slightCorrection(genotype);
        }
        return genotype;
    }
    private int[] randomMutation(int[] genotype) {
        for (int i = 0; i < mutationsNumber; i++) {
            int genIdx = ThreadLocalRandom.current().nextInt(0, genotypeLength);
            genotype[genIdx] = ThreadLocalRandom.current().nextInt(0, 8);
        }
        return genotype;
    }
    private int[] slightCorrection(int[] genotype) {
        for (int i = 0; i < mutationsNumber; i++) {
            int genIdx = ThreadLocalRandom.current().nextInt(0, genotypeLength);
            int change = ThreadLocalRandom.current().nextInt(0, 2);
            if (change == 0) {
                genotype[genIdx]--;
                if (genotype[genIdx] < 0) {
                    genotype[genIdx] = 7;
                }
            } else {
                genotype[genIdx]++;
                if (genotype[genIdx] > 7) {
                    genotype[genIdx] = 0;
                }
            }
        }
        return genotype;
    }
}

