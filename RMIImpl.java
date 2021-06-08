import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.*;



public class RMIImpl extends UnicastRemoteObject implements CloudInterface
{
	//define here
	private static final String USERJSONFILE = "user.json";
	private static final String FILEDETAILJSONFILE = "fileDetail.json";
	private static final Boolean DEFAULTACTIVE = true;
	//TODO Data
	ArrayList<User> users = new ArrayList<>();
	ArrayList<FileDetail> fileDetails = new ArrayList<>();

	public RMIImpl() throws java.rmi.RemoteException
	{
		super();
		try {
			File file = new File(USERJSONFILE);
			if (file.createNewFile()) {
				System.out.println("File Created.");
			} else {
				System.out.println("File Already Exists.");
			}
			updateList(USERJSONFILE);
			file = new File(FILEDETAILJSONFILE);
			if (file.createNewFile()) {
				System.out.println("File Created.");
			} else {
				System.out.println("File Already Exists.");
			}
			updateList(FILEDETAILJSONFILE);
		}catch (IOException e){
			System.exit(1);
		}

	}

	//read user file to user list
	private void updateList(String jsonFile){
		try {
			Scanner reader = new Scanner(new File(jsonFile));
			if(!reader.hasNextLine()) return;
			String data = reader.nextLine();
			reader.close();
			switch (jsonFile) {
				case USERJSONFILE -> this.users = new Gson().fromJson(data, new TypeToken<ArrayList<User>>() {}.getType());
				case FILEDETAILJSONFILE -> this.fileDetails = new Gson().fromJson(data, new TypeToken<ArrayList<FileDetail>>() {}.getType());
			}
		} catch (FileNotFoundException e) {
			System.out.println("An Error Occurred.");
			e.printStackTrace();
		}
	}

	//write user list to user file
	private void updateJson(String jsonFile) {
		try{
			FileWriter writer = new FileWriter(jsonFile);
			switch (jsonFile) {
				case USERJSONFILE -> writer.write(new Gson().toJson(users));
				case FILEDETAILJSONFILE -> writer.write(new Gson().toJson(fileDetails));
			}
			writer.close();
			System.out.println("Updated File.");
		} catch (IOException e) {
			System.out.println("An Error Occurred.");
			e.printStackTrace();
		}
	}

	// register
	synchronized public boolean register(String account, String password) {
		//FIXME add strong password
		for (User nowUser : users) {//check same account
			if(nowUser.getAccount().equals(account)){
				return false;
			}
		}
		users.add(new User(
				users.size(),
				account,
				password,
				DEFAULTACTIVE
		));
		updateJson(USERJSONFILE);
		updateList(USERJSONFILE);
		return true;
	}

	synchronized public boolean login(String account, String password) {
		updateList(USERJSONFILE);
		for (User nowUser : users) {
			//TODO ENCRYPT
			if(nowUser.checkLogin(account,password)){
				return true;
			}
		}
		return false;
	}


	public boolean writeDataToServer(String fileName, byte[] bytes,int len,String account) {
		try {
			//TODO System.getProperty() and FILE.Separator
			File dir = new File(System.getProperty("user.dir")+File.separator+"cloud"+File.separator+account);
//			File dir2 = new File("./cloud/"+account);
			if (!dir.exists()){
				dir.mkdirs();
			}
			File file = new File(System.getProperty("user.dir")+File.separator+"cloud"+File.separator+account+File.separator+fileName);
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file,false);
			out.write(bytes,0,len);
			out.flush();
			out.close();
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
			for (FileDetail detail : fileDetails) {
				if (detail.getFileName().equals(fileName) && detail.getCreator().equals(account)) {
					detail.setDate(formatter.format(new Date()));
					updateJson(FILEDETAILJSONFILE);
					updateList(FILEDETAILJSONFILE);
					return true;
				}
			}
			//TODO init FileDetail
			fileDetails.add(new FileDetail(
					account,//creator
					new String[]{account},//accessor
					fileName,
					formatter.format(new Date())//date
			));
			updateJson(FILEDETAILJSONFILE);
			updateList(FILEDETAILJSONFILE);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public byte[] loadDataFromServer(String fileName, String creator) {
		try {
			File file = new File(System.getProperty("user.dir")+File.separator+"cloud"+File.separator+ creator +File.separator+fileName);
			int size = (int) file.length();
			byte[] bytes = new byte[size];
			try {
				BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
				buf.read(bytes, 0, bytes.length);
				buf.close();
				return bytes;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} catch (Exception e){
			return null;
		}
	}


	public String[][] showMyFile(String account) {
		updateList(FILEDETAILJSONFILE);
		List<List<String>> fileList = new ArrayList<>();
		int counter=0;
		for (FileDetail file : fileDetails) {
			for(String accessor:file.getAccessor()) {
				if (accessor.equals(account)) {
					//TODO {ID fileName creator date}
					fileList.add(Arrays.asList(
							String.valueOf(counter++),
							file.getFileName(),
							file.getCreator(),
							file.getDate()
					));
					break;
				}
			}
		}
		//FIXME change to list
		String[][] array = new String[fileList.size()][];
		for (int i = 0; i < fileList.size(); i++) {
			List<String> row = fileList.get(i);
			array[i] = row.toArray(new String[0]);
		}
		return array;
	}


	public boolean delFile(String fileName, String account) throws RemoteException {
		updateList(FILEDETAILJSONFILE);
		File file = new File(System.getProperty("user.dir")+File.separator+"cloud"+File.separator+account+File.separator+fileName);
		if(file.delete()){
			for(int i=0;i<fileDetails.size();i++){
				if(fileDetails.get(i).getCreator().equals(account) && fileDetails.get(i).getFileName().equals(fileName)){
					//FIXME bonus of list
					fileDetails.remove(i);
					updateJson(FILEDETAILJSONFILE);
					return true;
				}
			}
		}
		return false;
	}

	//TODO
	public String[][] showCreatedFile(String account) throws RemoteException {
		updateList(FILEDETAILJSONFILE);
		List<List<String>> fileList = new ArrayList<>();
		int counter=0;
		for (FileDetail file : fileDetails) {
			if (file.getCreator().equals(account)) {
				fileList.add(Arrays.asList(
						String.valueOf(counter++),
						file.getFileName(),
						Arrays.toString(file.getAccessor()),
						file.getDate()
				));
			}

		}
		String[][] array = new String[fileList.size()][];
		for (int i = 0; i < fileList.size(); i++) {
			List<String> row = fileList.get(i);
			array[i] = row.toArray(new String[0]);
		}
		return array;
	}

	public boolean addAccessor(String creator, String fileName, String newAccessor) throws RemoteException {
		updateList(FILEDETAILJSONFILE);
		for (FileDetail file : fileDetails) {
			if (file.getCreator().equals(creator) && file.getFileName().equals(fileName)) {
				List<String> accessorList = new ArrayList<>(Arrays.asList(file.getAccessor()));
				if(accessorList.contains(newAccessor)){
					return false;
				}else{
					accessorList.add(newAccessor);
					file.setAccessor(accessorList.toArray(new String[0]));
					updateJson(FILEDETAILJSONFILE);
					return true;
				}
			}

		}
		return false;
	}

	public boolean delAccount(String account, String password) throws RemoteException {
		updateList(FILEDETAILJSONFILE);
		for (User nowUser : users) {
			if(nowUser.checkLogin(account,password)){
				nowUser.setActive(false);
				updateJson(USERJSONFILE);
				return true;
			}
		}
		return false;

	}

}
