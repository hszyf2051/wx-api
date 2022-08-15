package com.yif.Listener;

import com.yif.service.IDoctorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;


/**
 * @author yif
 */
@Slf4j
@Component
public class FileListener extends FileAlterationListenerAdaptor {
	@Autowired
	public IDoctorService doctorService;

	@Override
	public void onStart(FileAlterationObserver observer) {
		super.onStart(observer);
//		System.out.println("onStart");
	}

	@Override
	public void onDirectoryCreate(File directory) {
		log.info("新建：" , directory.getAbsolutePath());
	}

	@Override
	public void onDirectoryChange(File directory) {
		log.info("修改：" , directory.getAbsolutePath());
	}

	@Override
	public void onDirectoryDelete(File directory) {
		log.info("删除：" , directory.getAbsolutePath());
	}

	@Override
	public void onFileCreate(File file) {
		String compressedPath = file.getAbsolutePath();
		log.info("新建文件{}",  compressedPath);
		if (file.canRead()) {
			// TODO 读取或重新加载文件内容
			log.info("文件变更，进行处理");
		}
	}

	@Override
	public void onFileChange(File file) {
		String compressedPath = file.getAbsolutePath();
//		String filePath = "C:\\Users\\admin\\Desktop\\Api\\医师节数据样例.xlsx";
		// 如果被修改的是目标文件，执行自己的业务操作
//		if (StringUtils.equals(filePath,compressedPath)){
//			try {
//				doctorService.editorExcelData();
//			} catch (Exception e) {
//				log.error("错误：",e);
//			}
//		}
		log.info("修改文件路径:" + compressedPath);
	}

	@Override
	public void onFileDelete(File file) {
		log.info("删除：" + file.getAbsolutePath());
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		super.onStop(observer);
//		System.out.println("onStop");
	}
}
