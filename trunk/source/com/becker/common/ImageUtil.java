package com.becker.common;

import com.sun.image.codec.jpeg.*;
import com.sun.media.jai.codec.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

/**
 * A utility class for generating image files and manipulating images
 *
 * @author Barry Becker
 */
public final class ImageUtil
{

    // print quality for JPGs 1 is no compression
    private static final float JPG_QUALITY = 0.9f;

    public enum ImageType { PNG, JPG };

    private ImageUtil() {}

    /**
     * create a BufferedImage from an Image
     */
    public static BufferedImage makeBufferedImage( final Image image )
    {
        BufferedImage bImg = new BufferedImage( image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB );
        Graphics2D g2 = bImg.createGraphics();
        g2.drawImage( image, null, null );
        g2.dispose();
        return bImg;
    }

    /**
     * create an image that is compatible with your hardware
     */
    public static BufferedImage createCompatibleImage( int width, int height )
    {
        GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen = local.getDefaultScreenDevice();
        GraphicsConfiguration configuration = screen.getDefaultConfiguration();
        return configuration.createCompatibleImage( width, height );
    }

    /**
     * return a byte array given an image
     * @param img the image to convert
     * @param type the type of image to create ("jpg" or "png")
     */
    public static byte[] getImageAsByteArray( Image img, ImageType type )
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedOutputStream os = new BufferedOutputStream( bos );
        writeImage( img, os, type );

        return bos.toByteArray();
    }

    /**
     *  write an image to the given output stream
     *  @param img
     *  @param os output stream to write to
     *  @param type the type of image to create ("jpg" or "png")
     */
    public static void writeImage( Image img, BufferedOutputStream os, ImageType type )
    {
        //long time = System.currentTimeMillis();
        BufferedImage bi = makeBufferedImage( img );

        if ( type == ImageType.JPG ) {
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder( os );
            com.sun.image.codec.jpeg.JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam( bi );
            // this makes the images near perfect - very little compression
            param.setQuality( JPG_QUALITY, false );
            encoder.setJPEGEncodeParam( param );

            try {
                encoder.encode( bi );  // this writes it to a file as a .jpg
            } catch (IOException fne) {
                System.out.println( "IOException error:" + fne.getMessage());
            }
        }
        else { // PNG is the default
            // Create the ParameterBlock.
            // we can play with this for better compression/quality
            PNGEncodeParam param = PNGEncodeParam.getDefaultEncodeParam( bi );

            //Create the PNG image encoder.
            ImageEncoder encoder = ImageCodec.createImageEncoder( "PNG", os, param );
            try {
                encoder.encode( bi );  // this writes it to a file as a .png
            } catch (IOException fne) {
                System.out.println( "IOException error:" +  fne.getMessage());
            } catch (NullPointerException npe) {
                System.out.println("bi="+bi);
                throw npe;
            }
        }

        try {
            os.flush();
            os.close();
        } catch (IOException fne) {
            System.out.println( "IOException error:" + fne.getMessage());
        }
        //System.out.println("VizUtil: createImage time = "+(System.currentTimeMillis()-time));
    }

    /**
     * Saves an image to a file using the format specified by the type
     * note the filename should not include the extension.
     * this will be added as appropriate.
     * @param fileName the fileName should not have an extension because it gets added based on VizContext.imageFormat
     * @param img the image to save
     * @param type of image ("jpg" or "png" (default))
     */
    public static void saveAsImage( String fileName, Image img, ImageType type )
    {
        BufferedOutputStream os = null;
        try {
            String extension = '.' +type.toString().toLowerCase();
            String fn = fileName;
            if (!fn.endsWith(extension))  {
                // if it does not already have the appropriate extension add it.
                fn += extension;
            }

            //System.out.println("saving as "+  fileName + extension );
            os = new BufferedOutputStream( new FileOutputStream( fn ) );
        } catch (FileNotFoundException fne) {
            System.out.println( "File " + fileName + " not found: " + fne.getMessage());
        }

        writeImage( img, os, type );
    }

           
    // temp vars for interpolation
    private static float[] rgbaL = new float[4];
    private static float[] rgbaU = new float[4];
    
    public static Color interpolate( double x, double y, float[]  colorLL, float[] colorLR, float[] colorUL, float[]  colorUR )
    {      
         rgbaL[0] = (float) (colorLL[0] + x * (colorLR[0] - colorLL[0]));
         rgbaL[1] = (float) (colorLL[1] + x * (colorLR[1] - colorLL[1]));
         rgbaL[2] = (float) (colorLL[2] + x * (colorLR[2] - colorLL[2]));
         rgbaL[3] = (float) (colorLL[3] + x * (colorLR[3] - colorLL[3]));
         
         rgbaU[0] = (float) (colorUL[0] + x * (colorUR[0] - colorUL[0]));
         rgbaU[1] = (float) (colorUL[1] + x * (colorUR[1] - colorUL[1]));
         rgbaU[2] = (float) (colorUL[2] + x * (colorUR[2] - colorUL[2]));
         rgbaU[3] = (float) (colorUL[3] + x * (colorUR[3] - colorUL[3]));
                  
        return new Color( (float) (rgbaL[0] + y * (rgbaU[0] - rgbaL[0])),
                                      (float) (rgbaL[1] + y * (rgbaU[1] - rgbaL[1])),
                                      (float) (rgbaL[2] + y * (rgbaU[2] - rgbaL[2])),
                                      (float) (rgbaL[3] + y * (rgbaU[3] - rgbaL[3])));
    }
}
