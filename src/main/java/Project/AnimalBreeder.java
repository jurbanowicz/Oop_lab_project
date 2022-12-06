package Project;

public class AnimalBreeder {
    IWorldMap map;
    GenotypeGenerator genotypeGenerator;
    int breedingCost;
    int genotypeLength;
    int breedingMinEnergy;
    public AnimalBreeder(IWorldMap map, int breedingCost, int breedingMinEnergy, int genotypeLength, GenotypeGenerator genotypeGenerator) {
        this.map = map;
        this.breedingCost = breedingCost;
        this.breedingMinEnergy = breedingMinEnergy;
        this.genotypeLength = genotypeLength;
        this.genotypeGenerator = genotypeGenerator;

    }
    public boolean isBreedingPossible(Animal a1, Animal a2) {
        return a1.getEnergy() > breedingMinEnergy && a2.getEnergy() > breedingMinEnergy;
    }
    public Animal breedNewAnimal(Animal a1, Animal a2) {
        Animal baby = new Animal(a1.getPosition(), map, genotypeLength,2*breedingCost);
        baby.setGenotype(genotypeGenerator.GenerateBaby(a1, a2));
        a1.subtractEnergy(breedingCost);
        a2.subtractEnergy(breedingCost);
        return baby;
    }
}
