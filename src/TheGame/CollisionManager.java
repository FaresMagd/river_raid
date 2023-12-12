package TheGame;

import models.GameObject;

public class CollisionManager {

    public static void remove(RiverRaidGLEventListener g) {
        int i = 0;
        while (i < g.gameState.plans.size()) {
            if (g.gameState.plans.get(i).remove) {
                g.gameState.plans.remove(i);
            }
            i++;
        }

        i = 0;
        while (i < g.gameState.ships.size()) {
            if (g.gameState.ships.get(i).remove) {
                g.gameState.ships.remove(i);
            }
            i++;
        }
        while (i < g.gameState.homes.size()) {
            if (g.gameState.homes.get(i).remove) {
                g.gameState.homes.remove(i);
            }
            i++;
        }
        i = 0;
        while (i < g.gameState.fulls.size()) {
            if (g.gameState.fulls.get(i).remove) {
                g.gameState.fulls.remove(i);
            }
            i++;
        }

    }

    public static void destroy(RiverRaidGLEventListener g) {
        double d;
        for (GameObject plan : g.gameState.plans) {
            d = calcD(plan.x, g.gameState.xBullet, plan.y, g.gameState.yBullet);

            if (g.gameState.fired && d < 7) {
                plan.remove = true;
                g.gameState.fired = false;
                g.gameState.score += 50;
            }
            d = calcD(plan.x, g.gameState.x, plan.y, g.gameState.y);

            if (d < 7) {
                g.crashed();
            }
        }
        for (GameObject ship : g.gameState.ships) {
            d = calcD(ship.x, g.gameState.xBullet, ship.y, g.gameState.yBullet);
            if (g.gameState.fired && d < 7) {
                ship.remove = true;
                g.gameState.fired = false;
                g.gameState.score += 100;
            }
            d = calcD(ship.x, g.gameState.x, ship.y, g.gameState.y);

            if (d < 10) {
                g.crashed();
            }

        }
        if (g.gameState.x > 70) {
            g.crashed();
        }
        if (g.gameState.x < 20) {
            g.crashed();
        }
        for (GameObject full : g.gameState.fulls) {
            if (g.gameState.x > full.x - 2 && g.gameState.x < full.x + 2 && g.gameState.y > full.y - 8 && g.gameState.y < full.y + 5) {
                g.gameState.tank += 6;
                g.gameState.tank = Math.min(g.gameState.tank, 100);

            }
            if (g.gameState.xBullet > full.x - 2 && g.gameState.xBullet < full.x + 2 && g.gameState.yBullet > full.y - 8 && g.gameState.yBullet < full.y + 5) {
                full.remove = true;
                g.gameState.fired = false;
                g.gameState.score += 100;

            }
        }
    }

    public static double calcD(int x1, int x2, int y1, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

}
