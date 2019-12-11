package ru.demetrious.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class FileManager {
    /**
     * Read image from file as Buffered Image
     *
     * @param path Path in string format
     * @return Return Buffered Image object
     */
    public static BufferedImage readImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(99204);
        }
        return null;
    }

    /**
     * Write string into a file; if file or directories are not exist - create them
     *
     * @param path   Path in string format
     * @param string String what to write
     * @param append Whether to continue recording the file.
     *               If false file will be overwrite
     * @return Returns true if written successfully
     */
    public static boolean write(String path, String string, boolean append) {
        if (!new File(path).exists()) {
            if (!createFile(path)) return false;
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, append))) {
            bufferedWriter.write(string);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(99213);
            return false;
        }
    }

    /**
     * Read file as string array list
     *
     * @param path Path in string format
     * @return Return string array list
     */
    public static ArrayList<String> read(String path) {
        ArrayList<String> strings = new ArrayList<>();

        if (!new File(path).exists()) return null;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String tmp;

            while ((tmp = bufferedReader.readLine()) != null) {
                strings.add(tmp);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(99415);
            return null;
        }
        return strings;
    }

    /**
     * Create file if file or directory are not exist
     *
     * @param path Path in string format
     * @return Return true if file created
     */
    public static boolean createFile(String path) {
        String[] tmp = path.split("\\\\");
        String fileName = tmp[tmp.length - 1];
        String fileDir = path.substring(0, path.length() - fileName.length() - 1);

        if (!new File(fileDir).exists() && !new File(fileDir).mkdirs()) return false;
        try {
            if (!new File(path).createNewFile()) return false;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(99414);
        }
        return true;
    }
}
