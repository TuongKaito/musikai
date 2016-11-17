package com.kaito.musiconline.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

public class MediaStreamer implements StreamingOutput {

	private int length;
    private RandomAccessFile raf;
    final byte[] buf = new byte[4096];
    
    public MediaStreamer() {
    	super();
    }

    public MediaStreamer(int length, RandomAccessFile raf) {
        this.length = length;
        this.raf = raf;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException, WebApplicationException {
        try {
            while( length != 0) {
                int read = raf.read(buf, 0, buf.length > length ? length : buf.length);
                outputStream.write(buf, 0, read);
                length -= read;
            }
        } finally {
            raf.close();
        }
    }

    public int getLenth() {
        return length;
    }

	//Storage file uploaded
	public void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
        try {
            OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];
            
            out = new FileOutputStream(new File(uploadedFileLocation));
            while((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(MediaStreamer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
