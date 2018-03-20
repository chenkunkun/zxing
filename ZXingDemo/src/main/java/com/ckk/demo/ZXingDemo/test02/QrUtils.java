package com.ckk.demo.ZXingDemo.test02;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrUtils {
	 private static final int IMAGE_WIDTH = 130;
	  private static final int IMAGE_HEIGHT = 130;
	  private static final int IMAGE_HALF_WIDTH = IMAGE_WIDTH / 2;
	  private static final int FRAME_WIDTH = 2;
	  private static MultiFormatWriter mutiWriter = new MultiFormatWriter();
	  
	  public static void encode(String content, int width, int height,
	      String srcImagePath, String destImagePath) {
	    try {
	      ImageIO.write(genBarcode(content, width, height, srcImagePath),
	          "jpg", new File(destImagePath));
	    } catch (IOException e) {
	      e.printStackTrace();
	    } catch (WriterException e) {
	      e.printStackTrace();
	    }
	  }
	  
	  private static BufferedImage genBarcode(String content, int width,
	      int height, String srcImagePath) throws WriterException,
	      IOException {
	    BufferedImage scaleImage = scale(srcImagePath, IMAGE_WIDTH,
	        IMAGE_HEIGHT, true);
	    int[][] srcPixels = new int[IMAGE_WIDTH][IMAGE_HEIGHT];
	    for (int i = 0; i < scaleImage.getWidth(); i++) {
	      for (int j = 0; j < scaleImage.getHeight(); j++) {
	        srcPixels[i][j] = scaleImage.getRGB(i, j);
	      }
	    }
	    Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();
	    hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
	    hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
	    // 生成二维码 
	    BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE,
	        width, height, hint);
	    // 二维矩阵转为一维像素数组
	    int halfW = matrix.getWidth() / 2;
	    int halfH = matrix.getHeight() / 2;
	    int[] pixels = new int[width * height];
	    for (int y = 0; y < matrix.getHeight(); y++) {
	      for (int x = 0; x < matrix.getWidth(); x++) {
	        // 左上角颜色,根据自己需要调整颜色范围和颜色   
	        if (x > 0 && x < 170 && y > 0 && y < 170) {
	          Color color = new Color(77, 112, 176);
	          int colorInt = color.getRGB();
	          pixels[y * width + x] = matrix.get(x, y) ? colorInt
	              : 16777215;
	        }
	        // 读取图片
	        else if (x > halfW - IMAGE_HALF_WIDTH
	            && x < halfW + IMAGE_HALF_WIDTH
	            && y > halfH - IMAGE_HALF_WIDTH
	            && y < halfH + IMAGE_HALF_WIDTH) {
	          pixels[y * width + x] = srcPixels[x - halfW
	              + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];
	        } else if ((x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
	            && x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH
	            && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
	            + IMAGE_HALF_WIDTH + FRAME_WIDTH)
	            || (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH
	                && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
	                && y > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
	                + IMAGE_HALF_WIDTH + FRAME_WIDTH)
	            || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
	                && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
	                && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
	                - IMAGE_HALF_WIDTH + FRAME_WIDTH)
	            || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
	                && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
	                && y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
	                + IMAGE_HALF_WIDTH + FRAME_WIDTH)) {
	          pixels[y * width + x] = 0xfffffff;
	          // 在图片四周形成边框
	        } else {
	          // 二维码颜色
	          int num1 = (int) (77 - (77.0 - 13.0) / matrix.getHeight()
	              * (y + 1));
	          int num2 = (int) (112 - (112.0 - 72.0) / matrix.getHeight()
	              * (y + 1));
	          int num3 = (int) (176 - (176.0 - 107.0)
	              / matrix.getHeight() * (y + 1));
	          Color color = new Color(77, 112, 176);
	          int colorInt = color.getRGB();
	          // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色； 
	          pixels[y * width + x] = matrix.get(x, y) ? colorInt: 16777215;
	          // 0x000000:0xffffff   
	        }
	      }
	    }
	    BufferedImage image = new BufferedImage(width, height,
	        BufferedImage.TYPE_INT_RGB);
	    image.getRaster().setDataElements(0, 0, width, height, pixels);
	    return image;
	  }
	  
	  private static BufferedImage scale(String srcImageFile, int height,
	      int width, boolean hasFiller) throws IOException {
	    double ratio = 0.0; // 缩放比例
	    File file = new File(srcImageFile);
	    BufferedImage srcImage = ImageIO.read(file);
	    Image destImage = srcImage.getScaledInstance(width, height,
	        BufferedImage.SCALE_SMOOTH);
	    // 计算比例  
	    if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
	      if (srcImage.getHeight() > srcImage.getWidth()) {
	        ratio = (new Integer(height)).doubleValue()
	            / srcImage.getHeight();
	      } else {
	        ratio = (new Integer(width)).doubleValue()
	            / srcImage.getWidth();
	      }
	      AffineTransformOp op = new AffineTransformOp(
	          AffineTransform.getScaleInstance(ratio, ratio), null);
	      destImage = op.filter(srcImage, null);
	    }
	    if (hasFiller) {
	      // 补白
	      BufferedImage image = new BufferedImage(width, height,
	          BufferedImage.TYPE_INT_RGB);
	      Graphics2D graphic = image.createGraphics();
	      graphic.setColor(Color.white);
	      graphic.fillRect(0, 0, width, height);
	      if (width == destImage.getWidth(null))
	        graphic.drawImage(destImage, 0,
	            (height - destImage.getHeight(null)) / 2,
	            destImage.getWidth(null), destImage.getHeight(null),
	            Color.white, null);
	      else
	        graphic.drawImage(destImage,
	            (width - destImage.getWidth(null)) / 2, 0,
	            destImage.getWidth(null), destImage.getHeight(null),
	            Color.white, null);
	      graphic.dispose();
	      destImage = image;
	    }
	    return (BufferedImage) destImage;
	  }
	  
	  public static  String makeRoundedCorner(URL url,File result,String type,int cornerRadius) {          
			 try {  
		            BufferedImage bi1 = ImageIO.read(url);  
		              
		            // 根据需要是否使用 BufferedImage.TYPE_INT_ARGB  
		            BufferedImage image = new BufferedImage(bi1.getWidth(), bi1.getHeight(),  
		                    BufferedImage.TYPE_INT_ARGB);  
		        
		            Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1  
		                    .getHeight());  
		               
		            Graphics2D g2 = image.createGraphics();  
		            image = g2.getDeviceConfiguration().createCompatibleImage(bi1.getWidth(), bi1.getHeight(), Transparency.TRANSLUCENT);  
		            g2 = image.createGraphics();  
		            g2.setComposite(AlphaComposite.Clear);  
		            g2.fill(new Rectangle(image.getWidth(), image.getHeight()));  
		            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));  
		            g2.setClip(shape);  
		            // 使用 setRenderingHint 设置抗锯齿  
		            g2 = image.createGraphics();  
		            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
		            g2.fillRoundRect(0, 0,bi1.getWidth(), bi1.getHeight(), cornerRadius, cornerRadius);  
		            g2.setComposite(AlphaComposite.SrcIn);  
		            g2.drawImage(bi1, 0, 0, bi1.getWidth(), bi1.getHeight(), null);  
		            g2.dispose();  
		            ImageIO.write(image, type, result);  
		            return result.getAbsolutePath();
		        } catch (Exception e) {  
		            // TODO: handle exception  
		        }  
			 return null;
		    }  
}
