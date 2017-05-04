package com.qqmaster.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.opencv_videoio;
import org.bytedeco.javacpp.opencv_videoio.CvCapture;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import com.qqmaster.common.SystemConstant;
import com.qqmaster.service.VideoService;

public class VideoServiceImpl implements VideoService{

	/**
	 * Get CvCapture from the specified file path.
	 * @param file
	 * @return
	 */
	@Override
	public CvCapture getCvCapture(String filePath){

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
	@Override
	public long getCvFrameCount(CvCapture cvCapture){
		return (long)opencv_videoio.cvGetCaptureProperty(cvCapture, opencv_videoio.CAP_PROP_FRAME_COUNT);
	}

	/**
	 * Get fps from CvCapture
	 * @param cvCapture
	 * @return
	 */
	@Override
	public long getCvFPS(CvCapture cvCapture){
		return (long)opencv_videoio.cvGetCaptureProperty(cvCapture, opencv_videoio.CV_CAP_PROP_FPS);
	}

	/**
	 * calculate video duration from the specified fileName;
	 * @param filePath
	 * @return
	 * @throws Exception 
	 */
	@Override
	public double getVideoDuration(String filePath) throws Exception{
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
	@Override
	public List<Frame> getFramesOfVideo(String filePath,List<Integer> randoms) throws Exception{
		List<Frame> frames = new ArrayList<Frame>();
		FFmpegFrameGrabber ffg = FFmpegFrameGrabber.createDefault(filePath);
		ffg.start();

		{
			int numFrames = ffg.getLengthInFrames();
			int i = 0;
			for(Integer random:randoms){
				for(; i< numFrames; i++){
					Frame frame = ffg.grabFrame();
					if(i == random){
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
	@Override
	public List<Frame> getAllFramesOfVideo(String filePath) throws Exception{
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
