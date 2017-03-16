import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

public abstract class HBRSubsystem<E extends Enum<E>> {
	// Profiles to use
	E[] profiles;
	
	// PID/Follower constants
	
	// Feedforward constants
	double kf_x[];
	double kf_v[];
	double kv_a[];
	
	// Feedback constants
	double k_p[];
	double k_i[];
	double k_d[];
	
	protected HBRSubsystem() {
		// Initialize subsystem
		super();

		// Get the enum containing the systems to control
		Class<?> type = this.getClass();
		while(type.getSuperclass().getName() != "HBRSubsystem") {
			type = type.getSuperclass();
		}
		ParameterizedType superType = (ParameterizedType)type.getGenericSuperclass();
		Class<?> typeArguments = (Class<?>)superType.getActualTypeArguments()[0];
		profiles = (E[])typeArguments.getEnumConstants();
	}
	
	public int getProfileIndex(E profile) {
		return Arrays.asList(profiles).indexOf(profile);
	}
	
	public void setFeedforward(E profile, double kf_x, double kf_v, double kf_a) {
		
	}
	
	public void setFeedback(E profile, double k_p, double k_i, double k_d) {
		
	}
}
