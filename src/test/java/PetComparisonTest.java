import org.example.Pet;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.fail;

public class PetComparisonTest {

//    @Test
//    public void compareIvyAndRoma() {
//        // Создаем объекты Pet
//        Pet ivy = new Pet(1L, "Ivy", "available");
//        Pet roma = new Pet(2L, "Roma", "available");
//
//        // Сравниваем объекты
//        boolean isEqual = ivy.equals(roma);
//
//        // Выводим результат сравнения
//        System.out.println("Ivy and Roma are " + (isEqual ? "equal." : "not equal."));
//        // Проверяем, что объекты не равны
//        Assert.assertNotEquals(ivy, roma, "Ivy and Roma should not be equal as they have different ids and names.");
//
//        // Проверяем конкретные поля объектов
//        Assert.assertNotEquals(ivy.getId(), roma.getId(), "IDs should be different.");
//        Assert.assertNotEquals(ivy.getName(), roma.getName(), "Names should be different.");
//        Assert.assertEquals(ivy.getStatus(), roma.getStatus(), "Statuses should be the same.");
private void comparePetsAndPrintDifferences(Pet pet1, Pet pet2) {
    StringBuilder differences = new StringBuilder();
    boolean isEqual = true;

    if (!pet1.getId().equals(pet2.getId())) {
        differences.append(String.format("ID differs: Pet1 ID = %s, Pet2 ID = %s%n", pet1.getId(), pet2.getId()));
        isEqual = false;
    }

    if (!pet1.getName().equals(pet2.getName())) {
        differences.append(String.format("Name differs: Pet1 Name = %s, Pet2 Name = %s%n", pet1.getName(), pet2.getName()));
        isEqual = false;
    }

    if (!pet1.getStatus().equals(pet2.getStatus())) {
        differences.append(String.format("Status differs: Pet1 Status = %s, Pet2 Status = %s%n", pet1.getStatus(), pet2.getStatus()));
        isEqual = false;
    }

    if (!isEqual) {
        fail("Pets are not equal. Differences:\n" + differences.toString());
    } else {
        System.out.println("Pets are equal.");
    }
    }

    @Test
    public void testComparePets() {
        Pet pet1 = new Pet(1L, "Ivy", "available");
        Pet pet2 = new Pet(2L, "Roma", "pending");

        comparePetsAndPrintDifferences(pet1, pet2);
    }


}

