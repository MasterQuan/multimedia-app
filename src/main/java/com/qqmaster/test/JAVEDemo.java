package com.qqmaster.test;

import java.io.File;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

public class JAVEDemo {
	public static void main(String[] args) throws Exception, InputFormatException, EncoderException {
		File source = new File("/JAVA_Files/audio/face.mov");
		File target = new File("/JAVA_Files/audio/face.wav");
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("pcm_s16le");
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("wav");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		System.out.println(source.exists());
		System.out.println(target.exists());
		System.out.println(encoder.getAudioEncoders());
		System.out.println(encoder.getAudioDecoders());
		System.out.println(encoder.getVideoEncoders());
		System.out.println(encoder.getVideoDecoders());
		
		encoder.encode(source, target, attrs);
	}

}
