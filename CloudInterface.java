import java.rmi.Remote;

public interface CloudInterface extends Remote
{
	boolean register(String account, String password) throws java.rmi.RemoteException;
	boolean login(String account, String password) throws java.rmi.RemoteException;
	boolean writeDataToServer(String fileName,byte[] bytes,int len,String account) throws java.rmi.RemoteException;
	byte[] loadDataFromServer(String fileName,String creator) throws java.rmi.RemoteException;
	String[][] showMyFile(String account) throws java.rmi.RemoteException;
	boolean delFile(String fileName,String account) throws java.rmi.RemoteException;
	String[][] showCreatedFile(String account) throws java.rmi.RemoteException;
	boolean addAccessor(String creator, String fileName , String newAccessor) throws java.rmi.RemoteException;
	boolean delAccount(String account, String password) throws java.rmi.RemoteException;
}