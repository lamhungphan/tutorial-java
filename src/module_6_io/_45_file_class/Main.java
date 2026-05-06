package module_6_io._45_file_class;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        // file =   An abstract representation of file and directory path names
        File file = new File("secret_message.txt");
        //File file = new File("C:/Users/ACER/OneDrive/Máy tính/secret_message.txt");

        if(file.exists()){
            System.out.println("That file exists :O ");
            System.out.println(file.getPath());
            System.out.println(file.getAbsolutePath());
            System.out.println(file.isFile());
            //file.delete();
        }else {
            System.out.println("That file doesn't exist :( ");
        }
    }
}
