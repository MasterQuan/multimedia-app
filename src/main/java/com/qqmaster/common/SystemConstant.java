package com.qqmaster.common;

public class SystemConstant {
	public static final long AV_TIME_BASE = 1000000L;
	public static final int FRAMES_OFFSET = 8;
	public static final int NUM_OF_USE_FRAMES = 7;
	
	//训练图片、训练文件、测试图片
	public static final String TRAIN_FILE_DIR = "/JAVA_Files/video/train";
	public static final String TRAIN_RESULT_FILE = "/JAVA_Files/video/config/train.xml";
	public static final String TEST_FILE_DIR = "/JAVA_Files/video/test";
	
	//测试视频
	public static final String TEST_VIDEO_WITH_FACE = "/JAVA_Files/video/face.mp4";
	public static final String TEST_VIDEO_WITHOUT_FACE = "/JAVA_Files/video/noface.mp4";
	public static final String TEST_VIDEO_LESS_FACE = "/JAVA_Files/video/lessface.mp4";
	public static final String TEST_VIDEO_MORE_FACE = "/JAVA_Files/video/moreface.mp4";
	
}
