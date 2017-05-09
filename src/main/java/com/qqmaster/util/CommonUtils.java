package com.qqmaster.util;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import com.qqmaster.common.SystemConstant;

/**
 * 公共的工具类
 * @author zhaoshiquan 2017年5月3日 上午10:56:05
 *
 */
public class CommonUtils {

	/**
	 * 获取指定数目{@value length}和指定范围{@value baseNum}的随机数，并排序
	 * @param baseNum
	 * @param length
	 * @return
	 */
	public static List<Integer> random(int baseNum, int length) {
		List<Integer> list = new ArrayList<>(length);
		for(int i=0; i<length; ){

			Integer next = (int)(Math.random() * baseNum);
			if(list.contains(next)){
				continue;
			}else{
				list.add(next);
				i++;
			}
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * 获取SystemConstant.NUM_OF_USE_FRAMES个指定范围{@value baseNum}的随机数
	 * ，并排序
	 * @param baseNum
	 * @param length
	 * @return
	 */
	public static List<Integer> random(int baseNum) {

		List<Integer> list = new ArrayList<>(SystemConstant.NUM_OF_USE_FRAMES);
		for(int i=0; i<SystemConstant.NUM_OF_USE_FRAMES; ){

			Integer next = (int)(Math.random() * baseNum);
			if(list.contains(next)){
				continue;
			}else{
				list.add(next);
				i++;
			}
		}
		Collections.sort(list);
		return list;
	}

	/**
	 * 均匀获取{@value length}个指定范围{@value baseNum}的随机数
	 * ，并排序
	 * @param baseNum
	 * @param length
	 * @return
	 */
	public static List<Integer> uniform(int baseNum, int length) {
		List<Integer> list = new ArrayList<>(length);
		int num = SystemConstant.FRAMES_OFFSET;
		int dev = (baseNum-SystemConstant.FRAMES_OFFSET)/length;
		for(int i=0; i<length; i++){
			list.add(num += dev);
		}
		return list;
	}

	/**
	 * 均匀获取SystemConstant.FRAMES_OFFSET个指定范围{@value baseNum}的随机数
	 * ，并排序
	 * @param baseNum
	 * @param length
	 * @return
	 */
	public static List<Integer> uniform(int baseNum) {
		List<Integer> list = new ArrayList<>();
		int num = SystemConstant.FRAMES_OFFSET;
		int dev = (baseNum-SystemConstant.FRAMES_OFFSET)/(SystemConstant.NUM_OF_USE_FRAMES -1) - 1;
		list.add(num);
		for(int i=1; i<SystemConstant.NUM_OF_USE_FRAMES; i++){
			list.add(num += dev);
		}
		return list;
	}

	public static File[] getImageFiles(String trainDir){
		FilenameFilter imgFilter = (dir,name)->{
			name = name.toLowerCase();
			return name.endsWith(".jpg") 
					|| name.endsWith(".pgm") 
					|| name.endsWith(".png");
		};

		File[] imageFiles = new File(trainDir).listFiles(imgFilter);
		return imageFiles;
	}

	public static void trainFaceRecognizerWithImages(FaceRecognizer faceRecognizer, String trainDir){

		File[] imageFiles = getImageFiles(SystemConstant.TRAIN_FILE_DIR);
		MatVector images = new MatVector(imageFiles.length);
		Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
		IntBuffer labelsBuf = labels.createBuffer();
		int counter = 0;
		for (File image : imageFiles) {
			Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
			int label = Integer.parseInt(image.getName().split("\\-")[0]);
			images.put(counter, img);
			labelsBuf.put(counter, label);
			counter++;
		}
		faceRecognizer.train(images, labels);
	}

	public static void trainFaceRecognizerWithFile(FaceRecognizer faceRecognizer, String trainFile){
		faceRecognizer.load(trainFile);
	}

	public static void saveTrainFile(FaceRecognizer faceRecognizer, String trainFile){
		trainFaceRecognizerWithImages(faceRecognizer,trainFile);
		faceRecognizer.save(SystemConstant.TRAIN_RESULT_FILE);
	}


	public static void detectFaceInImages(FaceRecognizer faceRecognizer, String testDir){
		File[] images = getImageFiles(testDir);
		int index = 1;
		for(File image:images){
			Mat testImage = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
			int predictedLabel = faceRecognizer.predict_label(testImage);
			System.out.println(image.getAbsolutePath() + " has face--> " + ((predictedLabel ==1) ? true : false));
			index++;
		}
	}

	public static boolean detectFaceInFrames(FaceRecognizer faceRecognizer, Frame[] frames){
		OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();

		int count = 0;
		int i = 0;
		for(Frame frame : frames){
			Mat testFrame = new Mat();
			cvtColor(converterToMat.convert(frame), testFrame, COLOR_BGRA2GRAY);

			int predictLable = faceRecognizer.predict_label(testFrame);
			System.out.println(i+"th frame has face? --> " + (predictLable == 1 ? true:false));
			count += (predictLable == 1 ? 1:0);
			i++;
		}
		
		return count > (frames.length>>1) ? true:false;
	}

	public static boolean detectFaceInFrames(FaceRecognizer faceRecognizer, Frame frame){
		OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();

		Mat testFrame = new Mat();
		cvtColor(converterToMat.convert(frame), testFrame, COLOR_BGRA2GRAY);

		int predictLable = faceRecognizer.predict_label(testFrame);
		boolean  result = (predictLable == 1 ? true:false);
		System.out.println("the frame has face? --> " + (predictLable == 1 ? true:false));
		return result;
	}


	public static void main(String[] args) {

	}

}
