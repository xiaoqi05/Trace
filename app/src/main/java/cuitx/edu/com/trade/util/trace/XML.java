package cuitx.edu.com.trade.util.trace;

import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cuitx.edu.com.trade.bean.KeyValue;


public class XML {

	private String fullName;
	private String homeFolder;

	public XML() {
		String path = Environment.getExternalStorageDirectory() + "/tracedata";
		homeFolder = path;
		folderCheck(homeFolder);
	}

	public boolean genNewFile() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date curDate = new Date(System.currentTimeMillis());
		String time = formatter.format(curDate);
		String fileName = "data" + time + ".xml";
		fullName = homeFolder + "/" + fileName;
		if (!isFileExist(fullName)) {
			try {
				creatSDFile(fullName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else
			return false;
	}

	public void folderCheck(String path) {
		File file = new File(path);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
	}

	public boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	public File creatSDFile(String fileName) throws IOException {
		File file = new File(fileName);
		file.createNewFile();
		return file;
	}

	public String genXMLString(String operation, ArrayList<KeyValue> content) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		String time = formatter.format(curDate);
		try {
			serializer.setOutput(writer);

			serializer.startDocument("UTF-8", true);

			serializer.startTag("", operation);
			serializer.text("\n");

			serializer.startTag("", "data");
			serializer.attribute("", "time", time);
			serializer.text("\n");

			for (int i = 0; i < content.size(); i++) {
				serializer.text("\n");
				serializer.startTag("", "op");
				serializer.text(content.get(i).getKey());
				serializer.endTag("", "op");
				serializer.startTag("", "value");
				serializer.text(content.get(i).getValue());
				serializer.endTag("", "value");
				serializer.text("\n");
			}

			serializer.endTag("", "data");

			serializer.endTag("", operation);

			serializer.endDocument();

			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void WriteXMLFile(String message) {
		try {
			File file = new File(fullName);
			FileOutputStream fos = new FileOutputStream(file, true);
			fos.write(message.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}