package com.flyonsky.file.txt.api;

import java.util.List;

/**
 * Text文件操作的写入接口
 * @author luowg
 *
 */
public interface ITxtWriter {
	
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
}
