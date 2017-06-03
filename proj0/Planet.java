public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        this(p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
    }

    public double calcDistance(Planet p) {
        return Math.sqrt((xxPos - p.xxPos) * (xxPos - p.xxPos) + (yyPos - p.yyPos) * (yyPos - p.yyPos));
    }

    public double calcForceExertedBy(Planet p) {
        return 6.67e-11 * mass * p.mass / (this.calcDistance(p) * this.calcDistance(p));
    }
    public double calcForceExertedByX(Planet p){
        return this.calcForceExertedBy(p) * (p.xxPos - xxPos)/this.calcDistance(p);
    }
    public double calcForceExertedByY(Planet p){
        return this.calcForceExertedBy(p) * (p.yyPos - yyPos)/this.calcDistance(p);
    }
    public double calcNetForceExertedByX(Planet [] aPlanets){
        double xNetForce = 0.0;
        for (Planet p : aPlanets) {
            if (p.equals(this)) {
                continue;
            }
            xNetForce += this.calcForceExertedByX(p);
        }
        return xNetForce;
    }
    public double calcNetForceExertedByY(Planet [] aPlanets){
        double yNetForce = 0.0;
        for (Planet p : aPlanets) {
            if (p.equals(this)) {
                continue;
            }
            yNetForce += this.calcForceExertedByY(p);
        }
        return yNetForce;
    }
    public void update(double dt, double fX, double fY){
        xxVel = xxVel + fX / mass * dt;
        yyVel = yyVel + fY / mass * dt;
        xxPos += xxVel * dt;
        yyPos += yyVel * dt;
    }
}
