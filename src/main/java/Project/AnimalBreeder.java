package Project;

public class AnimalBreeder {
    IWorldMap map;
    GenotypeGenerator genotypeGenerator;
    int breedingCost;
    int genotypeLength;
    public AnimalBreeder(IWorldMap map, int breedingCost, int genotypeLength, GenotypeGenerator genotypeGenerator) {
        this.map = map;
        this.breedingCost = breedingCost;
        this.genotypeLength = genotypeLength;
        this.genotypeGenerator = genotypeGenerator;

    }
    public Animal breedNewAnimal(Animal a1, Animal a2) {
        Animal baby = new Animal(a1.getPosition(), map, genotypeLength,2*breedingCost);
        baby.setGenotype(genotypeGenerator.GenerateBaby(a1, a2));
        a1.subtractEnergy(breedingCost);
        a2.subtractEnergy(breedingCost);
        return baby;
    }
}
