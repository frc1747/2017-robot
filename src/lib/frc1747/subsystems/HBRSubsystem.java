package lib.frc1747.subsystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class HBRSubsystem extends Subsystem {

	private static ArrayList<HBRSubsystem> hbrSubsystem = new ArrayList<>();
	
	public HBRSubsystem() {
		super();
		hbrSubsystem.add(this);
	}
	
	public HBRSubsystem(String name) {
		super(name);
		hbrSubsystem.add(this);
	}
	
	/**
	 * Called at a periodic rate to update the
	 * dashboard.
	 */
	public abstract void updateDashboard();
	
	public static void update() {
		for(HBRSubsystem subsystem : hbrSubsystem)
			subsystem.updateDashboard();
	}
}
