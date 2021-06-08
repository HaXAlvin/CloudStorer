import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.*;
import java.rmi.server.*;

//javac -cp ".:./libs/gson-2.2.2.jar" *.java
//java -cp ".:./libs/gson-2.2.2.jar" CloudServer
//java -cp ".:./libs/gson-2.2.2.jar" Client
public class CloudServer
{
	public static void main(String args[])
	{
		try
		{
//			TODO old way
//			CloudInterface name = new RMIImpl();
//			System.out.println("Registering ...");
//			Naming.rebind("arithmetic", name);	// arithmetic is the name of the service
//			System.out.println("Register success");

//			TODO rmi on server
			CloudInterface name = new RMIImpl();
//			LocateRegistry.getRegistry(1099); 							//connect to exist registry
			Registry registry = LocateRegistry.createRegistry(1099);//create registry on server
			System.out.println("Registering ...");
			registry.bind("CloudServer", name);
			System.err.println("Register success");
		}
		catch(Exception e)
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}