package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int MAX_UNITS_PER_TYPE = 11;
    private static final int MAX_POINTS = 1500;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        Army computerArmy = new Army();
        List<Unit> selectedUnits = new ArrayList<>();
        int currentPoints = 0;

        // Sort units by efficiency: attack/cost and health/cost
        unitList.sort(Comparator.comparingDouble(this::calculateUnitEfficiency).reversed());

        for (Unit unit : unitList) {
            if (currentPoints >= maxPoints) break;

            int maxUnitsForType = Math.min(MAX_UNITS_PER_TYPE, (maxPoints - currentPoints) / unit.getCost());
            for (int i = 0; i < maxUnitsForType; i++) {
                selectedUnits.add(createUnitCopy(unit));
                currentPoints += unit.getCost();
            }
        }

        computerArmy.setUnits(selectedUnits);
        computerArmy.setPoints(currentPoints);

        System.out.println("Generated army with " + selectedUnits.size() + " units and " + currentPoints + " points.");
        return computerArmy;
    }

    private double calculateUnitEfficiency(Unit unit) {
        return (double) unit.getBaseAttack() / unit.getCost() + (double) unit.getHealth() / unit.getCost();
    }

    private Unit createUnitCopy(Unit unit) {
        return new Unit(
                unit.getName(),
                unit.getUnitType(),
                unit.getHealth(),
                unit.getBaseAttack(),
                unit.getCost(),
                unit.getAttackType(),
                unit.getAttackBonuses(),
                unit.getDefenceBonuses(),
                unit.getxCoordinate(),
                unit.getyCoordinate()
        );
    }
}