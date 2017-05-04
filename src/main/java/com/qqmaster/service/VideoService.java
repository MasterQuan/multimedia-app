package com.qqmaster.service;

import java.util.List;

import org.bytedeco.javacpp.opencv_videoio.CvCapture;
import org.bytedeco.javacv.Frame;

public interface VideoService {
	
	public CvCapture getCvCapture(String filePath);
	
	public long getCvFrameCount(CvCapture cvCapture);
	
	public long getCvFPS(CvCapture cvCapture);
	
	public double getVideoDuration(String filePath) throws Exception;
	
	public List<Frame> getFramesOfVideo(String filePath,List<Integer> randoms) throws Exception;
	
	public List<Frame> getAllFramesOfVideo(String filePath) throws Exception;
}
