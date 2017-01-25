public class NBody {
	public static double readRadius(String file) {
		In in = new In(file);
		in.readInt();
		return in.readDouble();
	}

	public static Planet[] readPlanets(String file) {
		In in = new In(file);
		int N = in.readInt();
		in.readDouble();
		Planet[] pl = new Planet[N];
		for(int i = 0; i < N; i++) {
			double xxPos = in.readDouble();
			double yyPos = in.readDouble();
			double xxVel = in.readDouble();
			double yyVel = in.readDouble();
			double mass = in.readDouble();
			String name = in.readString();
			pl[i] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, name);
		}
		return pl;
	}
	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		Planet[] pl = readPlanets(filename);
		double r = readRadius(filename);
		StdDraw.setScale(-r, r);
		StdDraw.picture(0, 0, "images/starfield.jpg");
		for(int i = 0; i < pl.length; i++) {
			pl[i].draw();
		}
		double time = 0;
		StdAudio.play("audio/Enya - Wild Child.wav");
		while(time < T) {
			double[] xForces = new double[pl.length];
			double[] yForces = new double[pl.length];
			for(int i = 0; i < pl.length; i++) {
				xForces[i] = pl[i].calcNetForceExertedByX(pl);
				yForces[i] = pl[i].calcNetForceExertedByY(pl);
			}
			for(int i = 0; i < pl.length; i++) {
				pl[i].update(dt, xForces[i], yForces[i]);
			}
			StdDraw.picture(0, 0, "images/starfield.jpg");
			for(int i = 0; i < pl.length; i++) {
				pl[i].draw();
			}
			StdDraw.show(10);
			time += dt;
		}
		StdOut.printf("%d\n", pl.length);
		StdOut.printf("%.2e\n", r);
		for (int i = 0; i < pl.length; i++) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
			pl[i].xxPos, pl[i].yyPos, pl[i].xxVel, pl[i].yyVel, pl[i].mass, pl[i].imgFileName);	
		}		
	}
}
