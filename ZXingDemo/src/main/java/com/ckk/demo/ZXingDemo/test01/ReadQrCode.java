package com.ckk.demo.ZXingDemo.test01;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class ReadQrCode {

	public static void main(String[] args) {
		MultiFormatReader formatReader=new MultiFormatReader();
		File file=new File( System.getProperty("user.dir")+"/img.png");
		
		try {
			BufferedImage image=ImageIO.read(file);
			BinaryBitmap binaryBitmap=new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
			
			//定义二维码参数
			HashMap hints=new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//编码格式
			Result result=formatReader.decode(binaryBitmap,hints);
			
			System.out.println("解析到二维码内容："+result.toString());
			System.out.println("解析到二维码类型："+result.getBarcodeFormat());
			System.out.println("解析到二维码文本："+result.getText());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
