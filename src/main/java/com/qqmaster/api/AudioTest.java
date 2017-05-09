package com.qqmaster.api;

import org.bytedeco.javacv.FFmpegFrameRecorder;

public class AudioTest {
	public static void main(String[] args) {
		FFmpegFrameRecorder recoder = new FFmpegFrameRecorder("/JAVA_Files/video/moreface.mp4", 0);
		System.out.println(recoder.getAudioBitrate());
		System.out.println(recoder.getAudioChannels());
		System.out.println(recoder.getAudioCodec());
		System.out.println(recoder.getAudioQuality());

	}
}
