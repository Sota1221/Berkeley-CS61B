public class Planet {
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	public Planet(double xP, double yP, double xV, double yV, double m, String img) {
		this.xxPos = xP;
		this.yyPos = yP;
		this.xxVel = xV;
		this.yyVel = yV;
		this.mass = m;
		this.imgFileName = img;
	}

	public Planet(Planet p) {
		this.xxPos = p.xxPos;
		this.yyPos = p.yyPos;
		this.xxVel = p.xxVel;
		this.yyVel = p.yyVel;
		this.mass = p.mass;
		this.imgFileName = p.imgFileName;
	}

	public double calcDistance(Planet p) {
		double dx = this.xxPos - p.xxPos;
		double dy = this.yyPos - p.yyPos;
		double rsq = Math.pow(dx, 2) + Math.pow(dy, 2);
		return Math.pow(rsq, 0.5);
	}

	public double calcForceExertedBy(Planet p) {
		return 6.67 * Math.pow(10, -11) * this.mass * p.mass / Math.pow(this.calcDistance(p), 2);
	}

	public double calcForceExertedByX(Planet p) {
		return this.calcForceExertedBy(p) * (p.xxPos - this.xxPos) / this.calcDistance(p);
	}

	public double calcForceExertedByY(Planet p) {
		return this.calcForceExertedBy(p) * (p.yyPos - this.yyPos) / this.calcDistance(p);
	}
	
	public double calcNetForceExertedByX(Planet[] allPlanets) {
		double fX = 0;
		for (int i = 0; i < allPlanets.length; i++) {
			if(!(this.equals(allPlanets[i]))) {
				fX += this.calcForceExertedByX(allPlanets[i]);
			}
		}
		return fX;
	}

	public double calcNetForceExertedByY(Planet [] allPlanets) {
		double fY = 0;
		for (int i = 0; i < allPlanets.length; i++) {
			if(!(this.equals(allPlanets[i]))) {
				fY += this.calcForceExertedByY(allPlanets[i]);
			}
		}
		return fY;
	}

	public void update(double dt, double fX, double fY) {
		double ax = fX / this.mass;
		double ay = fY / this.mass;
		this.xxVel = this.xxVel + dt * ax;
		this.yyVel = this.yyVel + dt * ay;
		this.xxPos = this.xxPos + dt * this.xxVel;
		this.yyPos = this.yyPos + dt * this.yyVel;
	}

	public void draw() {
		StdDraw.picture(this.xxPos, this.yyPos, "images/" + this.imgFileName);


	}

}
