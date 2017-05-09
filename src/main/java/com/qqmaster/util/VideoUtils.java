package com.qqmaster.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.bytedeco.javacpp.opencv_videoio.CvCapture;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameFilter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import com.qqmaster.common.SystemConstant;

import org.bytedeco.javacpp.avformat.AVFormatContext;
import org.bytedeco.javacpp.opencv_core;
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
	public static CvCapture getCvCapture(String filePath){

		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}

		return opencv_videoio.cvCreateFileCapture(filePath);
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
	 * @param filePath
	 * @return
	 * @throws Exception 
	 */
	public static double getVideoDuration(String filePath) throws Exception{
		double duration = 0.0;
		try(FFmpegFrameGrabber ffg = FFmpegFrameGrabber.createDefault(filePath);){
			ffg.start();
			duration = 1.0*ffg.getLengthInTime()/SystemConstant.AV_TIME_BASE;
			ffg.stop();
		}
		return duration;
	}

	/**
	 * get specified frames from the specified filePath according to the given random numbers.
	 * @param filePath
	 * @param randoms
	 * @return
	 * @throws Exception
	 */
	public static List<Frame> getFramesOfVideo(String filePath,List<Integer> locs) throws Exception{
		List<Frame> frames = new ArrayList<Frame>();
		FFmpegFrameGrabber ffg = FFmpegFrameGrabber.createDefault(filePath);
		ffg.start();
		
		{
			int numFrames = ffg.getLengthInFrames();
			int i = 0;
			for(Integer loc:locs){
				for(; i< numFrames; i++){
					Frame frame = ffg.grabFrame();
					if(i == loc){
						frames.add(frame);
						i++;
						break;
					}
				}
			}
		}
		
		ffg.stop();
		
		return frames;
	}


	/**
	 * get all frames of 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static List<Frame> getAllFramesOfVideo(String filePath) throws Exception{
		List<Frame> frames = new ArrayList<Frame>();
		FFmpegFrameGrabber ffg = FFmpegFrameGrabber.createDefault(filePath);
		ffg.start();
		int num = ffg.getLengthInFrames();
		for(int i=0; i < num; i++){
			frames.add(ffg.grabFrame());
		}
		ffg.stop();
		return frames;
	}














	public static void main(String[] args) throws Exception {
		
//		opencv_core.IplImage
		
		List<Integer> randoms = new ArrayList<Integer>();
		randoms.add(2);
		randoms.add(4);
		randoms.add(8);
		randoms.add(13);

		int numFrames = 200;
		int i=0;
		for(Integer random:randoms){
			for(; i< numFrames; i++){
				if(i == random){
					System.out.println("-->" + random);
					i++;
					break;
				}
			}
		}
		System.out.println("==>" +i);

	}
}
