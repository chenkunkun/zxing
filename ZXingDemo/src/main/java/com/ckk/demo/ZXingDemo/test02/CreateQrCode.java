package com.ckk.demo.ZXingDemo.test02;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class CreateQrCode {

	public static void main(String[] args) {
		URL url;
		try {
			url = new URL("http://img5.duitang.com/uploads/item/201609/26/20160926124027_vxRkt.jpeg");
			//1、先圓角透明
			String path=new QrUtils().makeRoundedCorner(url,new File(System.getProperty("user.dir")+"/qr0.jpg"),"jpg",120);
		    String contents="this is a QrCode";
			// 依次为内容(不支持中文),宽,长,中间图标路径,储存路径  
		    QrUtils.encode(contents, 638, 638,path, "qr.jpg");
		    
		    File outputFile=new File(System.getProperty("user.dir")+"/qr.jpg");
		    InputStream inputStream;
			try {
				inputStream = new FileInputStream(outputFile);
				outputFile.delete();
				File tmpFile=new File(System.getProperty("user.dir")+"/qr0.jpg");
				if(tmpFile.exists()){
					tmpFile.delete();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
