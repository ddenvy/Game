package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.EdgeDistance;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

/* Алгоритмическая сложность:

Инициализация расстояний: O(WIDTH * HEIGHT).

Основной цикл алгоритма Дейкстры: O((WIDTH * HEIGHT) log (WIDTH * HEIGHT)).

Восстановление пути: O(L), где L — длина пути.

Общая сложность: O((WIDTH * HEIGHT) log (WIDTH * HEIGHT)). */

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        int[][] distance = new int[WIDTH][HEIGHT];
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Edge[][] parent = new Edge[WIDTH][HEIGHT];
        PriorityQueue<EdgeDistance> queue = new PriorityQueue<>(Comparator.comparingInt(EdgeDistance::getDistance));

        // Инициализация расстояний
        for (int i = 0; i < WIDTH; i++) {
            Arrays.fill(distance[i], Integer.MAX_VALUE);
        }

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        distance[startX][startY] = 0;
        queue.add(new EdgeDistance(startX, startY, 0));

        // Множество занятых клеток
        Set<String> occupied = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit != attackUnit && unit != targetUnit && unit.isAlive()) {
                occupied.add(unit.getxCoordinate() + "," + unit.getyCoordinate());
            }
        }

        // Алгоритм Дейкстры
        while (!queue.isEmpty()) {
            EdgeDistance current = queue.poll();
            int x = current.getX();
            int y = current.getY();

            if (visited[x][y]) continue;
            visited[x][y] = true;

            if (x == targetUnit.getxCoordinate() && y == targetUnit.getyCoordinate()) {
                break;
            }

            for (int[] dir : DIRECTIONS) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if (isValid(nx, ny, occupied)) {
                    int newDist = distance[x][y] + 1;
                    if (newDist < distance[nx][ny]) {
                        distance[nx][ny] = newDist;
                        parent[nx][ny] = new Edge(x, y);
                        queue.add(new EdgeDistance(nx, ny, newDist));
                    }
                }
            }
        }

        // Восстановление пути
        List<Edge> path = new ArrayList<>();
        int x = targetUnit.getxCoordinate();
        int y = targetUnit.getyCoordinate();

        if (parent[x][y] == null) {
            System.out.println("Unit " + attackUnit.getName() + " cannot find path to attack unit " + targetUnit.getName());
            return path;
        }

        while (x != startX || y != startY) {
            path.add(new Edge(x, y));
            Edge p = parent[x][y];
            x = p.getX();
            y = p.getY();
        }

        path.add(new Edge(startX, startY));
        Collections.reverse(path);
        return path;
    }

    private boolean isValid(int x, int y, Set<String> occupied) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && !occupied.contains(x + "," + y);
    }
}