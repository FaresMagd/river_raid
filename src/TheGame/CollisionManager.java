package TheGame;

import models.GameObject;

public class CollisionManager {

    public static void remove(RiverRaidGLEventListener g) {
        int i = 0;
        while (i < g.plans.size()) {
            if (g.plans.get(i).remove) {
                g.plans.remove(i);
            }
            i++;
        }

        i = 0;
        while (i < g.ships.size()) {
            if (g.ships.get(i).remove) {
                g.ships.remove(i);
            }
            i++;
        }
        while (i < g.homes.size()) {
            if (g.homes.get(i).remove) {
                g.homes.remove(i);
            }
            i++;
        }
        i = 0;
        while (i < g.fulls.size()) {
            if (g.fulls.get(i).remove) {
                g.fulls.remove(i);
            }
            i++;
        }

    }

    public static void destroy(RiverRaidGLEventListener g) {
        double d;
        for (GameObject plan : g.plans) {
            d = calcD(plan.x, g.xBullet, plan.y, g.yBullet);

            if (g.fired && d < 7) {
                plan.remove = true;
                g.fired = false;
                g.score += 50;
            }
            d = calcD(plan.x, g.x, plan.y, g.y);

            if (d < 7) {
                g.crashed();
            }
        }
        for (GameObject ship : g.ships) {
            d = calcD(ship.x, g.xBullet, ship.y, g.yBullet);
            if (g.fired && d < 7) {
                ship.remove = true;
                g.fired = false;
                g.score += 100;
            }
            d = calcD(ship.x, g.x, ship.y, g.y);

            if (d < 10) {
                g.crashed();
            }

        }
        if (g.x > 70) {
            g.crashed();
        }
        if (g.x < 20) {
            g.crashed();
        }
        for (GameObject full : g.fulls) {
            if (g.x > full.x - 2 && g.x < full.x + 2 && g.y > full.y - 8 && g.y < full.y + 5) {
                g.tank += 2;
                g.tank = Math.min(g.tank, 100);

            }
            if (g.xBullet > full.x - 2 && g.xBullet < full.x + 2 && g.yBullet > full.y - 8 && g.yBullet < full.y + 5) {
                full.remove = true;
                g.fired = false;
                g.score += 100;

            }
        }
    }

    public static double calcD(int x1, int x2, int y1, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

}
