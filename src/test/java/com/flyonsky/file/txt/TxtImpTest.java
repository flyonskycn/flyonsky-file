package com.flyonsky.file.txt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.flyonsky.file.common.FileMode;

/**
 * 
 * @author luowg
 *
 */
public class TxtImpTest {
	
	private static String[] titles;
	
	private static List<Object[]> contentList;
	
	private static String content;
	
	@BeforeClass
	public static void init(){
		Random random = new Random();
		titles = new String[5];
		titles[0]="序列号";
		titles[1]="网元名称";
		titles[2]="通话时长";
		titles[3]="CDRID";
		titles[4]="globalid";
		
		int len = random.nextInt(50);
		System.out.println(len);
		contentList = new ArrayList<Object[]>();
		Object[] values = null;
		for(int i=0;i<len;i++){
			values = new Object[5];
			values[0]=RandomString(random.nextInt(30));
			values[1]=RandomString(random.nextInt(30));
			values[2]=RandomString(random.nextInt(30));
			values[3]=RandomString(random.nextInt(30));
			values[4]=RandomString(random.nextInt(30));
			contentList.add(values);
		}
		
		content = RandomString(random.nextInt(3500));
	}
	
	/** 产生一个随机的字符串*/  
	public static String RandomString(int length) {  
	    String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  
	    Random random = new Random();  
	    StringBuffer buf = new StringBuffer();  
	    for (int i = 0; i < length; i++) {  
	        int num = random.nextInt(62);  
	        buf.append(str.charAt(num));  
	    }  
	    return buf.toString();  
	}  
	
	@Test
	public void testChartSave(){
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + File.separator + "SimpleTxtImpTest.txt";
		TxtImp txt = new TxtImp(path, FileMode.write);
		boolean flag = txt.save(titles, contentList);
		Assert.assertEquals(true, flag);
	}
	
	@Test
	public void testSave(){
		
	}
}
