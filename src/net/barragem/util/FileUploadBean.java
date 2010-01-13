package net.barragem.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;

import net.barragem.view.backingbean.BaseBean;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

public class FileUploadBean extends BaseBean {

	public class File {
		private byte[] data;

		public void setData(byte[] data) {
			this.data = data;
		}

		public byte[] getData() {
			return data;
		}
	}

	private File file;

	public FileUploadBean() {
	}

	public void paint(OutputStream stream, Object object) throws IOException {
		if (file == null || file.getData() == null) {
			String path = getServletContext().getRealPath("img/foto-jogador.png");
			java.io.File fsFile = new java.io.File(path);
			file = new File();
			file.setData(getBytesFromFile(fsFile));
		}
		stream.write(file.getData());
	}

	public static byte[] getBytesFromFile(java.io.File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}

	private ServletContext getServletContext() {
		return getSession().getServletContext();
	}

	public void listener(UploadEvent event) throws Exception {
		UploadItem item = event.getUploadItem();

		file = new File();
		file.setData(item.getData());
		event.getUploadItems().clear();
	}

	public long getTimeStamp() {
		return System.currentTimeMillis();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}