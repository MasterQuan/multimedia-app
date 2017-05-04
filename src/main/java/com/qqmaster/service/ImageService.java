package com.qqmaster.service;

import java.util.List;

import org.bytedeco.javacv.Frame;

public interface ImageService {
	public boolean faceDetection(List<Frame> frames) throws Exception;
	public boolean isBlackScreen(Frame frame);
	public boolean isWriteScreen(Frame Frame);
}
