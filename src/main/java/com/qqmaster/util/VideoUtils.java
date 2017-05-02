package com.qqmaster.util;

import java.io.File;

import org.bytedeco.javacpp.opencv_videoio.CvCapture;
import org.bytedeco.javacpp.opencv_videoio;
/**
 * VideoUtils which contains the following functions:
 * 	1.get the CvCapture from a specified file name;
 * 	2.get the frame count of a specified CvCapture;
 * 	3.get the fps of a specified CvCapture;
 * 	4.get the duration of a specified CvCapture by given file name;
 * 
 * @author qqmaster 2017年5月2日 上午10:45:24
 *
 */
public class VideoUtils {

	/**
	 * Get CvCapture from the specified file path.
	 * @param file
	 * @return
	 */
	public static CvCapture getCvCapture(String fileName){

		File file = new File(fileName);
		if (!file.exists()) {
			return null;
		}

		return opencv_videoio.cvCreateFileCapture(fileName);
	}

	/**
	 * Get frame count from CvCapture
	 * @param cvCapture
	 * @return
	 */
	public static long getCvFrameCount(CvCapture cvCapture){
		return (long)opencv_videoio.cvGetCaptureProperty(cvCapture, opencv_videoio.CAP_PROP_FRAME_COUNT);
	}
	
	/**
	 * Get fps from CvCapture
	 * @param cvCapture
	 * @return
	 */
	public static long getCvFPS(CvCapture cvCapture){
		return (long)opencv_videoio.cvGetCaptureProperty(cvCapture, opencv_videoio.CV_CAP_PROP_FPS);
	}
	
	/**
	 * calculate video duration from the specified fileName;
	 * @param fileName
	 * @return
	 */
	public static double getVideoDuration(String fileName){
		double duration = 0.0;
		try(CvCapture cvCapture = getCvCapture(fileName)){
			duration = 1.0*getCvFrameCount(cvCapture)/getCvFPS(cvCapture);
		}catch (Exception e) {
			throw e;
		}
		return duration;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		System.out.println(getVideoDuration("/JAVA_Files/vedio/7220.mp4"));
	}
}
