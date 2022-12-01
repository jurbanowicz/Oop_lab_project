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
}

