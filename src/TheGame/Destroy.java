package TheGame;


public class Destroy {


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

    }

    public static void destroy(RiverRaidGLEventListener g) {
        double d;
        for (Object1 plan : g.plans) {
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
        for (Object1 ship : g.ships) {
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


    }

    public static double calcD(int x1, int x2, int y1, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

}
