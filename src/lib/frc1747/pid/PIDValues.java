package lib.frc1747.pid;

public class PIDValues {

	public double P, I, D, F, V, A;
	
	public PIDValues(double P, double I, double D) {
		this(P, I, D, 0);
	}
	
	public PIDValues(double P, double I, double D, double F) {
		this.P = P;
		this.I = I;
		this.D = D;
		this.F = F;
	}
	
	public PIDValues(double P, double I, double D, double V, double A) {
		this(P, I, D);
		this.V = V;
		this.A = A;
	}
}
