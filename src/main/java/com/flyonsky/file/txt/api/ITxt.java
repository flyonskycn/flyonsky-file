package com.flyonsky.file.txt.api;

import java.util.List;

/**
 * 操作text文件的接口
 * @author luowg
 *
 */
public interface ITxt {
	
	/**
	 * 将内容保存成表格时，值之间的分隔符
	 */
	static final char TXT_DELIMETER = '\t'; 
	
	/**
	 * 将内容保存成表格时，用于补全数据的符号
	 */
	static final char TXT_SPACE = '\040';
	
	/**
	 * 将内容保存
	 * @param content 内容
	 * @return false:表示失败;true:表示成功
	 */
	boolean save(String content);
	
	/**
	 * 以表格对齐的方式,将内容保存
	 * @param titles 表格表头
	 * @param contentList 文件内容
	 * @return false:表示失败;true:表示成功
	 */
	boolean save(String[] titles,List<Object[]> contentList);
	
	/**
	 * 当读取的text文件是以表格对齐方式写入的文件时，获取其表头的内容
	 * @return 表头信息
	 */
	String[] getTitle();
	
	/**
	 * 当读取的text文件是以表格对齐方式写入的文件时，获取除表头以外的内容
	 * @return 每个单元格内容作为一个String返回
	 */
	List<String[]> getValue();
	
	/**
	 * 将整个text文件内容整体返回
	 * @return 每行值作为一个String对象返回
	 */
	List<String> getContent();
}
