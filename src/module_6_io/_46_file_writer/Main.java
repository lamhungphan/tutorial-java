package module_6_io._46_file_writer;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            FileWriter writer = new FileWriter("poem.txt");
            writer.write("roses are red\nViolet are blue\n");
            writer.append("this line will add to the end of the poem file");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
