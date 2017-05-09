package com.qqmaster.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_videoio;
import org.bytedeco.javacpp.opencv_videoio.CvCapture;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import com.qqmaster.common.SystemConstant;
import com.qqmaster.service.VideoService;
import com.qqmaster.util.CommonUtils;

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
	public List<Frame> getFramesOfVideo(String filePath,List<Integer> locs) throws Exception{
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
		FFmpegFrameGrabber fgb = new FFmpegFrameGrabber(SystemConstant.TEST_VIDEO_LESS_FACE);
		fgb.start();
	
//		System.out.println(fgb.getFrameNumber());
//		System.out.println(fgb.getLengthInFrames());
		
//		writeImages(SystemConstant.TEST_VIDEO_LESS_FACE);
		
	}

	@Override
	public List<Frame> getRandomFramesOfVideo(String filePath) throws Exception {
		List<Frame> frames = new ArrayList<Frame>();
		FFmpegFrameGrabber ffg = FFmpegFrameGrabber.createDefault(filePath);
		ffg.start();

		{
			int numFrames = ffg.getLengthInFrames();
			List<Integer> locs = CommonUtils.random(numFrames);
			int i = 0;
			for(Integer loc : locs){
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

	@Override
	public List<Frame> getUniformFramesOfVideo(String filePath) throws Exception {
		
		List<Frame> frames = new ArrayList<Frame>();
		FFmpegFrameGrabber ffg = FFmpegFrameGrabber.createDefault(filePath);
//		OpenCVFrameGrabber ofg = OpenCVFrameGrabber.createDefault(filePath);
		ffg.start();
		{
			System.out.println("-->" + filePath);
			int numFrames = ffg.getLengthInFrames();
			List<Integer> locs = CommonUtils.uniform(numFrames);
			System.out.println("locs-->:" + locs);
			int i = 0;
			for(Integer loc:locs){
				for(; i< numFrames; i++){
					Frame frame = ffg.grabImage();
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
	
	public static void writeImages(String src) throws Exception{
		FFmpegFrameGrabber ffg = new FFmpegFrameGrabber(src);
		ffg.start();
		int len = ffg.getLengthInFrames();
		for(int i=0; i<len; i++){
			Frame frame = ffg.grabImage();
			saveFrame(frame,SystemConstant.TEST_FILE_DIR,"src",i);
			System.out.println("save image");
		}
		ffg.stop();
	}
	
	public static void saveFrame(Frame f, String targerFilePath, String targetFileName, int index) {
        if (null == f || null == f.image) {
            return;
        }
         
        Java2DFrameConverter converter = new Java2DFrameConverter();
 
        String imageMat = "jpg";
        String FileName = targerFilePath + File.separator + targetFileName + "_" + index + "." + imageMat;
        BufferedImage bi = converter.getBufferedImage(f);
        File output = new File(FileName);
        try {
            ImageIO.write(bi, imageMat, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	
}
