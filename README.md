# CloudStorer

網路程式設計 期末專題 - 雲端備份系統    
以RPC模型為研究方向  
並使用JAVA RMI實作檔案傳輸 Swing作為GUI介面  
  
  
使用方式： 
1. 編譯所有檔案  
javac -cp ".:./libs/gson-2.2.2.jar" *.java 
3. 開啟雲端伺服器  
java -cp ".:./libs/gson-2.2.2.jar" CloudServer  
5. 開啟客戶端  
java -cp ".:./libs/gson-2.2.2.jar" Client
