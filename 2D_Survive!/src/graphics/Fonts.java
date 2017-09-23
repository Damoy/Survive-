package graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

public class Fonts {

	
	 public static Image transformwhiteToTransparency(BufferedImage image){
	    ImageFilter filter = new RGBImageFilter(){
	      public final int filterRGB(int x, int y, int rgb){
	        return (rgb << 8) & 0xFFFFFFFF;
	      }
	    };

	    ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
	    return Toolkit.getDefaultToolkit().createImage(ip);
	  }
	 
	 public static BufferedImage imageToBufferedImage(Image image, int width, int height){
	    BufferedImage dest = new BufferedImage(
	        width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = dest.createGraphics();
	    g2.drawImage(image, 0, 0, null);
	    g2.dispose();
	    return dest;
	  }
	
	  public static Image transformColorToTransparency(BufferedImage image, Color c1, Color c2){
	    // Primitive test, just an example
	    final int r1 = c1.getRed();
	    final int g1 = c1.getGreen();
	    final int b1 = c1.getBlue();
	    final int r2 = c2.getRed();
	    final int g2 = c2.getGreen();
	    final int b2 = c2.getBlue();
	    ImageFilter filter = new RGBImageFilter(){
	    	
	      public final int filterRGB(int x, int y, int rgb){
	        int r = (rgb & 0xFF0000) >> 16;
	        int g = (rgb & 0xFF00) >> 8;
	        int b = rgb & 0xFF;
	        if (r >= r1 && r <= r2 &&
	            g >= g1 && g <= g2 &&
	            b >= b1 && b <= b2){
	          // Set fully transparent but keep color
	          return rgb & 0xFFFFFF;
	        }
	        return rgb;
	      }
	    };

	    ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
	      return Toolkit.getDefaultToolkit().createImage(ip);
	  }
	  
	  public static Image makeColorTransparent(BufferedImage im, final Color color) {
	        ImageFilter filter = new RGBImageFilter() {

	            // the color we are looking for... Alpha bits are set to opaque
	            public int markerRGB = color.getRGB() | 0xFF000000;

	            public final int filterRGB(int x, int y, int rgb) {
	                if ((rgb | 0xFF000000) == markerRGB) {
	                    // Mark the alpha bits as zero - transparent
	                    return 0x00FFFFFF & rgb;
	                } else {
	                    // nothing to do
	                    return rgb;
	                }
	            }
	        };
	        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
	        return Toolkit.getDefaultToolkit().createImage(ip);
	  }
	  
	  public static BufferedImage getBIWithoutWhite(BufferedImage bi){
		  return imageToBufferedImage(makeColorTransparent(bi, Color.WHITE),
				  bi.getWidth(), bi.getHeight());
	  }
	  
	  
	  public static int getWidth(Graphics2D g, String s){
		  return g.getFontMetrics().stringWidth(s);
	  }

}
