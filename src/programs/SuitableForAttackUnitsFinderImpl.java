package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();
        int targetRow = isLeftArmyTarget ? 2 : 0;

        for (List<Unit> row : unitsByRow) {
            for (Unit unit : row) {
                if (unit.isAlive()) {
                    int y = unit.getyCoordinate();
                    boolean isBlocked = false;

                    // Проверяем, не закрыт ли юнит другим юнитом
                    for (int i = targetRow; i >= 0 && i < unitsByRow.size(); i += (isLeftArmyTarget ? -1 : 1)) {
                        if (i != targetRow && unitsByRow.get(i).stream().anyMatch(u -> u.getyCoordinate() == y && u.isAlive())) {
                            isBlocked = true;
                            break;
                        }
                    }

                    if (!isBlocked) {
                        suitableUnits.add(unit);
                    }
                }
            }
        }

        return suitableUnits;
    }
}