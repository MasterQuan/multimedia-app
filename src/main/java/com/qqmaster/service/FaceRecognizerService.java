package com.qqmaster.service;

import java.util.List;

import org.bytedeco.javacv.Frame;

public interface FaceRecognizerService {
	
	//用指定目录下的图片训练人脸识别模型
	public void trainFaceRecognizer(String trainDir);
	
	//检测一系列帧中是否存在人脸
	public boolean detectFaceInFrames(List<Frame> frames);
	
	//检测一帧中是否存在人脸
	public boolean detectFaceInFrame(Frame frame);
	
	//检测指定目录下的图片中是否存在人脸
	public void detectFaceInImages(String testDir);

}
