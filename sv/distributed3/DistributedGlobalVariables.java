package sv.distributed3;

import java.util.Comparator;

public class DistributedGlobalVariables {
	/**
	 * This class contains static information that is used by distributed3
	 * classes.
	 */
	public static boolean debugClient = true;
	public static boolean debugServer = true;
	public static boolean debugConnection = false;
	public static boolean debugServerBuffer = false;
	public static boolean debugBufferValue = false;
	
	  public static final byte BOOL_CONST = 1;
	  public static final byte BYTE_CONST = 2;
	  public static final byte INT_CONST = 3;
	  public static final byte STRING_CONST = 4;
	  public static final byte FLOAT_CONST = 5;
	  public static final byte DOUBLE_CONST = 6;
	  
	  public static final byte LARGE_TRANSFER = 16;
	  public static final byte LARGE_OBJECT_TRANSFER = 17;

	public DistributedGlobalVariables() {
		// TODO Auto-generated constructor stub
	}

	// Comparators
	public class ConnectionInetCompare implements Comparator<LiteConnection> {
		@Override
		public int compare(LiteConnection arg0, LiteConnection arg1) {
			// TODO Auto-generated method stub
			return arg0.ip.compareTo(arg1.ip);
		}
	}

}
