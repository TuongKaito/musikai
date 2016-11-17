package com.kaito.musiconline.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.kaito.musiconline.model.MediaStreamer;

public final class Resources {

	public static final String JSON = MediaType.APPLICATION_JSON + ";charset=utf-8";
	public static final String MUSIC_PATH = "musics";
	public static final String AVATAR_PATH = "avatar_images";
	public static final String ARTIST_PATH = "artist_images";
	public static final String ALBUM_PATH = "album_images";
	public static final String PLAYLIST_PATH = "playlist_images";
	public static final String[] MUSICS_EXTENSION = new String[] {"mp3", "wav"};
	public static final String[] IMAGES_EXTENSION = new String[] {"jpg", "jpeg", "bmp", "png", "gif"};
	
	static final int chunk_size = 1024 * 1024;
	
	public static String getPath(ServletContext context, String path) {
		String contextPath = context.getRealPath("/");
		File file = new File(contextPath, path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath().replace("\\", "/");
	}
	
	public static boolean isFile(String ext, String[] format) {
		boolean isMusicFile = false;
		for (String mExt : format) {
			if (mExt.equalsIgnoreCase(ext)) {
				isMusicFile = true;
				break;
			}
		}
		return isMusicFile;
	}
	
	public static Response buildStream(final File asset, final String range) throws Exception {
        // range not requested : Firefox, Opera, IE do not send range headers
        if (range == null) {
            StreamingOutput streamer = new StreamingOutput() {
                @Override
                public void write(final OutputStream output) throws IOException, WebApplicationException {

                    @SuppressWarnings("resource")
					final FileChannel inputChannel = new FileInputStream(asset).getChannel();
                    final WritableByteChannel outputChannel = Channels.newChannel(output);
                    try {
                        inputChannel.transferTo(0, inputChannel.size(), outputChannel);
                    } finally {
                        // closing the channels
                        inputChannel.close();
                        outputChannel.close();
                    }
                }
            };
            return Response.ok(streamer).status(200).header(HttpHeaders.CONTENT_LENGTH, asset.length()).build();
        }

        String[] ranges = range.split("=")[1].split("-");
        final int from = Integer.parseInt(ranges[0]);
        /**
         * Chunk media if the range upper bound is unspecified. Chrome sends "bytes=0-"
         */
        int to = chunk_size + from;
        if (to >= asset.length()) {
            to = (int) (asset.length() - 1);
        }
        if (ranges.length == 2) {
            to = Integer.parseInt(ranges[1]);
        }

        final String responseRange = String.format("bytes %d-%d/%d", from, to, asset.length());
        final RandomAccessFile raf = new RandomAccessFile(asset, "r");
        raf.seek(from);

        final int len = to - from + 1;
        final MediaStreamer streamer = new MediaStreamer(len, raf);
        Response.ResponseBuilder res = Response.ok(streamer).status(206)
                .header("Accept-Ranges", "bytes")
                .header("Content-Range", responseRange)
                .header(HttpHeaders.CONTENT_LENGTH, streamer.getLenth())
                .header(HttpHeaders.LAST_MODIFIED, new Date(asset.lastModified()));
        return res.build();
    }
	
}
