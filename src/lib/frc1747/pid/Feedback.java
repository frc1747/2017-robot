package lib.frc1747.pid;

public abstract class Feedback {

	public abstract double getFeedback();
	
	public double getVelocityFeedback() {
		
		return 0;
	}
	
	public abstract void setOutput(double value);
}
