import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
	public static void main (String []args) {
		CloudInterface		o = null;
		try {
			Registry registry = LocateRegistry.getRegistry(null);
			o = (CloudInterface) registry.lookup("CloudServer");
			System.out.println("RMI server connected");
		} catch (Exception e) {
			System.out.println("Server lookup exception: " + e.getMessage());
		}
		new Page_Login(o);
	}
}

