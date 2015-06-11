package com.sktelink.sk00700.callservices;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Environment;
import android.util.Log;

public class FileHandler {
	
	private static final String FOLDER_ROOT		= "data00700";
	private static final String FOLDER_CALL_LOG = "callLog";
	private static final String FOLDER_CONTACT	= "contact";
	
	public static final int TYPE_CALL_LOG = 0x0;
	public static final int TYPE_CONTACT  = 0x1;
	
	public static String
					PATH_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath()
								+ File.separator + FOLDER_ROOT + File.separator;
	public static String
					PATH_CALL_LOG = PATH_ROOT + FOLDER_CALL_LOG + File.separator,
					PATH_CONTACT  = PATH_ROOT + FOLDER_CONTACT + File.separator;
	
	/*
	 * TODO: 
	 */
	public static void createFolderApp () {
		
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Log.d("MyApp", "No SDCARD");
		} else {
			// root's app's folder
			File directory = new File(PATH_ROOT);
			if (directory.exists()) {
				deleteBookFolder(directory);
			}

			directory.mkdir();

			// call log folder
			File calllog = new File(PATH_CALL_LOG);
			calllog.mkdir();

			// contact folder
			File contact = new File(PATH_CONTACT);
			contact.mkdir();
		}
	}
	
	
	/*
	 * TODO: delete Folder
	 */	
	public static boolean deleteBookFolder(File path) {
		
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}// end-if
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteBookFolder(files[i]);
				} else {
					files[i].delete();
				}// end-if
			}// end-for
		}// end-if
		return (path.delete());
	}// end-func
	
	
	/*
	 * TODO: write data
	 */	
	public static void writeData (String strData, String fileName, int type) {
		String filePath = null;
		OutputStreamWriter myOutWriter	= null;
		FileOutputStream fOut			= null;
		
		if (type == TYPE_CALL_LOG) {
			filePath = PATH_CALL_LOG + fileName; 
		} else if (type == TYPE_CONTACT) {
			filePath = PATH_CONTACT + fileName;
		}
		
		try {
			File myFile = new File(filePath);
			
			myFile.createNewFile();
			
			fOut		= new FileOutputStream(myFile, false);
			myOutWriter	= new OutputStreamWriter(fOut);
			
			myOutWriter.append(strData);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				myOutWriter.close();
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}// end-try
	}// end-func
	
	
	/*
	 * TODO: delete file data
	 */
	public static boolean deleteFileFromSdcard(String fileName, int type){
		String filePath = null;
		if (type == TYPE_CALL_LOG) {
			filePath = PATH_CALL_LOG + fileName; 
		} else if (type == TYPE_CONTACT) {
			filePath = PATH_CONTACT + fileName;
		}
		File file = new File(filePath);
		boolean deleted = file.delete();
		return deleted;
	}// end-func
	
}
