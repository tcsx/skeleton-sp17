public class NBody {
    public static double readRadius(String filename) {
        In in = new In(filename);
        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String filename) {
        In in = new In(filename);
        int n = in.readInt();
        Planet[] aPlanets = new Planet[n];
        in.readDouble();
        for (int i = 0; i < n; i++) {
            aPlanets[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(),
                    in.readDouble(), in.readString());
        }
        return aPlanets;
    }
    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);
        int n = planets.length;
        final String background = Planet.IMG_DIR + "starfield.jpg";
        StdDraw.setScale(-radius,radius);
        StdDraw.picture(0, 0, background);
        for (Planet p : planets) {
            p.draw();
        }
        double time = 0;
        while (time  <= T) {
            double XForces[] = new double[n];
            double YForces[] = new double[n];
            for(int i = 0; i < n; i++){
                XForces[i] = planets[i].calcNetForceExertedByX(planets);
                YForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for(int i = 0; i < n; i++){
                planets[i].update(dt, XForces[i], YForces[i]);
            }
            StdDraw.picture(0, 0, background);
            for (Planet p : planets) {
                p.draw();
            }
            StdDraw.show(10);
            time += dt;
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n", planets[i].xxPos, planets[i].yyPos,
                    planets[i].xxVel, planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
