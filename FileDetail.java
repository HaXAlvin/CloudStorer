public class FileDetail {
    private String creator;
    private String[] accessor;
    private String fileName;
    private String date;//yyyy-MM-dd HH:mm:ss

    public FileDetail(String creator, String[] accessor, String fileName, String date){
        this.creator = creator;
        this.accessor = accessor;
        this.fileName = fileName;
        this.date = date;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String[] getAccessor() {
        return accessor;
    }

    public void setAccessor(String[] accessor) {
        this.accessor = accessor;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
