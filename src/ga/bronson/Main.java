package ga.bronson;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Usage: [encode or decode] [image file name] [message to encode]\n");
        }

        switch (args[0]) {
            case "decode":
                decode(args[1]);
                break;
            case "encode":
                if (args.length == 3) {
                    encode(args[1], args[2]);
                } else {
                    System.out.println("Usage: bdcrack [encode or decode]\n");
                }
                break;
            default:
                System.out.println("Usage: bdcrack [encode or decode]\n");
                break;
        }
    }

    private static void encode(String fileName, String message) {
        ArrayList<Color> pixels = new ArrayList<>();
        int rgbIndex = 0;
        int rgb[] = new int[3];

        for(int i=0; i<message.length(); i++) {
            rgb[rgbIndex] = message.charAt(i);

            if(rgbIndex == 2) {
                pixels.add(new Color(rgb[0], rgb[1], rgb[2]));
            }

            rgbIndex = (rgbIndex+1)%3;
        }

        int dimensions = (int)Math.sqrt((double)pixels.size()) + 1;
        BufferedImage img = new BufferedImage(dimensions, dimensions, BufferedImage.TYPE_INT_RGB);
        int k=0;

        for(int i=0; i<dimensions; i++) {
            for(int j=0; j<dimensions; j++) {
                if(k < pixels.size()) {
                    img.setRGB(j, i, pixels.get(k).getRGB());
                } else {
                    img.setRGB(j, i, new Color(0, 0, 0).getRGB());
                }
                k++;
            }
        }

        try {
            File file = new File(fileName);
            ImageIO.write(img, "png", file);
        } catch(IOException e) {
            System.out.println("Failed to open file ["+fileName+"].\n");
        }
    }

    private static void decode(String fileName) {
        BufferedImage img;

        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            System.out.println("Failed to open file ["+fileName+"].\n");
            return;
        }

        StringBuilder message = new StringBuilder();

        for(int i=0; i<img.getHeight(); i++) {
            for(int j=0; j<img.getWidth(); j++) {
                Color pixel = new Color(img.getRGB(j, i));
                message.append((char)pixel.getRed()).
                        append((char)pixel.getGreen()).
                        append((char) pixel.getBlue());
            }
        }

        System.out.println(message);
    }
}
