package com.qqmaster.service.impl;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.equalizeHist;

import java.io.File;
import java.nio.IntBuffer;
import java.util.List;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import com.qqmaster.common.SystemConstant;
import com.qqmaster.service.FaceRecognizerService;
import com.qqmaster.util.CommonUtils;

public class FaceRecognizerServiceImpl implements FaceRecognizerService{

	private static FaceRecognizer faceRecognizer = createLBPHFaceRecognizer();
	private OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();

	@Override
	public void trainFaceRecognizer(String trainDir){
		//优先加载已经训练的结果，如果结果不存在，重新读取图片训练模型，并将结果写入指定的目录。

		if(new File(SystemConstant.TRAIN_RESULT_FILE).exists()){
			faceRecognizer.load(SystemConstant.TRAIN_RESULT_FILE);
			return;
		}

		File[] imageFiles = CommonUtils.getImageFiles(SystemConstant.TRAIN_FILE_DIR);
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
		faceRecognizer.save(SystemConstant.TRAIN_RESULT_FILE);
	}

	@Override
	public void detectFaceInImages(String testDir){
		File[] images = CommonUtils.getImageFiles(testDir);
		int index = 1;
		for(File image:images){
			Mat testImage = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
			int predictedLabel = faceRecognizer.predict_label(testImage);
			System.out.println(index + " has face--> " + ((predictedLabel ==1) ? true : false));
			index++;
		}
	}

	@Override
	public boolean detectFaceInFrames(List<Frame> frames){
		
		int count = 0;
		int i = 1;
		Mat dst = new Mat();
		for(Frame frame : frames){
			Mat src = converterToMat.convertToMat(frame);
			cvtColor(src, dst, CV_BGR2RGBA);
//			equalizeHist(dst, dst);
			int predictLable = faceRecognizer.predict_label(dst);
			System.out.println(i+"th frame has face? --> " + (predictLable == 1 ? "YES":"NO"));
			count += (predictLable == 1 ? 1:0);
			i++;
		}
		return count > (frames.size()>>1) ? true:false;
	}

	@Override
	public boolean detectFaceInFrame(Frame frame){
		OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
		 OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
		 converter.convertToMat(frame);
		 Mat testFrame = new Mat();
		cvtColor(converterToMat.convert(frame), testFrame, COLOR_BGRA2GRAY);

		int predictLable = faceRecognizer.predict_label(testFrame);
		boolean  result = (predictLable == 1 ? true:false);
		System.out.println("the frame has face? --> " + (predictLable == 1 ? "YES":"NO"));
		return result;
	}

}
