package com.flyonsky.file.txt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.flyonsky.file.common.FileMode;
import com.flyonsky.file.txt.api.ITxt;
import com.flyonsky.file.txt.api.ITxtReader;
import com.flyonsky.file.txt.api.ITxtWriter;

public class TxtImp implements ITxt{
	
	/**
	 * Text文件的读取对象
	 */
	private ITxtReader in;
	
	/**
	 * Text文件的写入对象
	 */
	private ITxtWriter out;
	
	public TxtImp(String filePath,FileMode mode){
		File file = new File(filePath);
		OutputStream outputStream = null;
		switch(mode){
		case read:
			try {
				in = new TxtReader(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			break;
			default:
				outputStream = create(file,mode);
				out = new TxtWriter(outputStream);
				break;
		}
	}
	
	/**
	 * 对文件形式的输出流，不存在时，新建输出流
	 * @param file 输出流路径
	 * @param mode 输出模式
	 * @return 输出流
	 */
	private OutputStream create(File file,FileMode mode){
		if(!file.exists()){
			File parentFile = file.getParentFile();
			if(parentFile!=null && !parentFile.exists()){
				boolean dirFlag = parentFile.mkdirs();
			}
			try {
				boolean fileFlag = file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		OutputStream out = null;
		if(mode.equals(FileMode.override)){
			try {
				out = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else if(mode.equals(FileMode.write)){
			try {
				out = new FileOutputStream(file,true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return out;
	}
	
	public TxtImp(InputStream input){
		in = new TxtReader(input);
	}
	
	public TxtImp(OutputStream output){
		out = new TxtWriter(output);
	}
	
	@Override
	public boolean save(String content) {
		if(out==null){
			return false;
		}
		return out.save(content);
	}

	@Override
	public boolean save(String[] titles, List<Object[]> contentList) {
		if(out == null){
			return false;
		}
		return out.save(titles, contentList);
	}

	@Override
	public List<String> getContent() {
		if(in == null){
			return null;
		}
		return in.getContent();
	}

	@Override
	public String[] getTitle() {
		if(in == null){
			return null;
		}
		return in.getTitle();
	}

	@Override
	public List<String[]> getValue() {
		if(in == null){
			return null;
		}
		return in.getValue();
	}
	
	/**
	 * Text文件的读取实现类
	 * @author luowg
	 *
	 */
	private static class TxtReader implements ITxtReader{
		
		private List<String> contentList;
		
		private boolean flag = false;
		
		private BufferedReader in;
		
		/**
		 * 
		 * @param input
		 */
		TxtReader(InputStream input){
			this.in = new BufferedReader(new InputStreamReader(input));
		}
		
		@Override
		public List<String> getContent() {
			if(!flag){
				flag = true;
				contentList = new ArrayList<String>();
				String content = null;
				try {
					while((content = in.readLine()) != null){
						contentList.add(content);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return contentList;
		}

		@Override
		public String[] getTitle() {
			List<String> list = this.getContent();
			String stitle = list.get(0);
			String[] titles = stitle.split(new Character(TXT_DELIMETER).toString());
			for(int i=0;i<titles.length;i++){
				titles[i] = titles[i].trim();
			}
			return titles;
		}

		@Override
		public List<String[]> getValue() {
			List<String[]> listValue = new ArrayList<String[]>();
			List<String> list = this.getContent();
			String values = null;
			String[] titles = null;
			for(int i=1;i<list.size();i++){
				values = list.get(i);
				titles = values.split(new Character(TXT_DELIMETER).toString());
				
				for(int j=0;j<titles.length;j++){
					titles[j] = titles[j].trim();
				}
				listValue.add(titles);
			}
			return listValue;
		}
		
	}
	
	/**
	 * Text文件的写入实现类
	 * @author luowg
	 *
	 */
	private static class TxtWriter implements ITxtWriter{

		private BufferedWriter out;
		
		TxtWriter(OutputStream output){
			this.out = new BufferedWriter(new OutputStreamWriter(output));
		}
		@Override
		public boolean save(String content) {
			boolean flag = false;
			if(content != null){
				try {
					this.out.write(content);
					this.out.flush();
					flag = true;
				} catch (IOException e) {
					flag = false;
					e.printStackTrace();
				}finally{
					try {
						this.out.close();
					} catch (IOException e) {
						flag = false;
						e.printStackTrace();
					}
				}
			}
			return flag;
		}

		@Override
		public boolean save(String[] titles,
				List<Object[]> contentList) {
			boolean flag = false;
			int[] lengths = maxValueLength(titles,contentList);
			List<String> valueList = merge(titles,contentList,lengths);
			try {
				for(String value: valueList){
					this.out.write(value);
					this.out.flush();
					flag = true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					this.out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return flag;
		}
		
		/**
		 * 获取出列头和值中每列最长的字符长度
		 * @param titles 表头
		 * @param contentList 值列表
		 * @return
		 */
		private int[] maxValueLength(String[] titles,List<Object[]> contentList){
			int[] lengths = new int[titles.length];
			for(int i=0;i<titles.length;i++){
				lengths[i] = titles[i].length();
			}
			String tmp = "";
			for(Object[] values : contentList){
				for(int i=0;i<values.length;i++){
					if(values[i] == null){
						continue;
					}
					tmp = values[i].toString();
					if(lengths[i] < tmp.length()){
						lengths[i] = tmp.length();
					}
				}
			}
			return lengths;
		}
		
		/**
		 * 将列头与数据补齐
		 * @param titles 列头信息
		 * @param contentList 数据信息
		 * @param lengths 每列的长度信息 
		 * @return 表格的数据信息，每一行为一个String
		 */
		private List<String> merge(String[] titles,List<Object[]> contentList,int[] lengths){
			List<String> valueList = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			String tmp = "";
			for(int i = 0;i< titles.length;i++){
				tmp = append(titles[i],TXT_SPACE,lengths[i] - titles[i].length());
				sb.append(tmp);
			}
			sb.append("\r\n");
			valueList.add(sb.toString());
			
			for(Object[] values : contentList){
				sb = new StringBuilder();
				for(int i=0;i<values.length;i++){
					if(values[i] == null){
						tmp = "";
					}else{
						tmp = values[i].toString();
					}
					tmp = append(tmp,TXT_SPACE,lengths[i]-tmp.length());
					sb.append(tmp);
				}
				sb.append("\r\n");
				valueList.add(sb.toString());
			}
			return valueList;
		}
		
		/**
		 * 将字符串以指定的字符补齐
		 * @param source 源字符串
		 * @param ch 用于补齐的字符
		 * @param length 补齐的个数
		 * @return 补齐后的字符串
		 */
		private String append(String source,char ch,int length){
			if(length <=0){
				return source;
			}
			StringBuilder sb = new StringBuilder(source);
//			for(int i=0;i<length;i++){
//				sb.append(ch);
//			}
			sb.append(TXT_DELIMETER);
			return sb.toString();
		}
	}
}
