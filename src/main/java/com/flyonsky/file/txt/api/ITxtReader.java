package com.flyonsky.file.txt.api;

import java.util.List;

/**
 * Text文件操作的读取接口
 * @author luowg
 *
 */
public interface ITxtReader {
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
