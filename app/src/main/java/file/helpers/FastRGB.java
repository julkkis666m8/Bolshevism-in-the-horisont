package file.helpers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.util.ArrayList;

/**
 * From: https://gist.github.com/alexpahom/1d41a8d4148eb778963edf1ef8c25087
 */
public class FastRGB {

    public static void main(String[] args) {
        try {
            System.out.println("Processing the image...");

            // Upload the image
            BufferedImage image = ImageIO.read(new File("resources/europeMapProvinces.png"));
            int width = image.getWidth();
            int height = image.getHeight();
            int[] pixels = new int[width * height];

            // Retrieve pixel info and store in 'pixels' variable
            PixelGrabber pgb = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
            pgb.grabPixels();

            // Write pixels to CSV
            writeTextFile("src/raw.txt", pixels, width);

            // It's supposed that user modifies pixels file here
            System.out.println("Done! Cast your spells with the text file and press Enter...");
            //System.in.read();

            // Read pixels from CSV
            int[] parsedPixels = readTextFile("src/raw.txt", width, height);

            textToImage("src/raw.txt", width, height, parsedPixels);

        } catch (Exception exc) {
            System.out.println("Interrupted: " + exc.getMessage());
        }
    }

    public static int[][] getMatrix(File file){
        int[][] matrix = new int[1][1];
        try {
            System.out.println("Processing the image...");

            // Upload the image
            BufferedImage image = ImageIO.read(file);
            int width = image.getWidth();
            int height = image.getHeight();
            int[] pixels = new int[width * height];

            // Retrieve pixel info and store in 'pixels' variable
            PixelGrabber pgb = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
            pgb.grabPixels();

            // Write pixels to CSV
            writeTextFile("src/raw.txt", pixels, width);

            // It's supposed that user modifies pixels file here
            System.out.println("File reading done!");

            // Read pixels from CSV
            int[] parsedPixels = readTextFile("src/raw.txt", width, height);

            matrix = toMatrix(parsedPixels, width, height);

        } catch (Exception exc) {
            System.out.println("Interrupted: " + exc.getMessage());
        }

        return matrix;
    }

    private static int[][] toMatrix(int[] parsedPixels, int width, int height) {
        int[][] matrix = new int[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                matrix[i][j] = parsedPixels[i*j];
            }
        }
        return matrix;
    }

    private static void writeTextFile(String path, int[] data, int width) throws IOException {
        FileWriter f = new FileWriter(path);
        // Write pixel info to file, comma separated
        for(int i = 0; i < data.length; i++) {
            String s = Integer.toString(data[i]);
            f.write(s + ", ");
            if (i % width == 0) f.write(System.lineSeparator());
        }
        f.close();
    }

    private static int[] readTextFile(String path, int width, int height) throws IOException {
        System.out.println("Processing text file...");
        BufferedReader csv = new BufferedReader(new FileReader(path));
        int[] data = new int[width * height];

        // Retrieves array of pixels as strings
        String[] stringData = parseCSV(csv);

        // Converts array of pixels to ints
        for(int i = 0; i < stringData.length; i++) {
            String num = stringData[i];
            data[i] = Integer.parseInt(num.trim());
        }
        return data;
    }

    private static String[] parseCSV(BufferedReader csv) throws IOException {
        ArrayList<String> fileData = new ArrayList<>();
        String row;

        // Fulfills 'fileData' with list of rows
        while ((row = csv.readLine()) != null) fileData.add(row);

        // Joins 'fileData' values into single 'joinedRows' string
        StringBuilder joinedRows = new StringBuilder();
        for(String line : fileData) joinedRows.append(line);

        // Splits 'joinedRows' by comma and returns array of pixels
        return joinedRows.toString().split(", ");
    }

    private static void textToImage(String path, int width, int height, int[] data) throws IOException {
        MemoryImageSource mis = new MemoryImageSource(width, height, data, 0, width);
        Image im = Toolkit.getDefaultToolkit().createImage(mis);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(im, 0, 0, null);
        ImageIO.write(bufferedImage, "png", new File(path));
        System.out.println("Done! Check the result");
    }
}