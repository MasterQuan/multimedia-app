package com.qqmaster.test;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_face.createFisherFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.createEigenFaceRecognizer;
//import static org.bytedeco.javacpp.opencv_face.createEigenFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;

import com.qqmaster.common.SystemConstant;
import com.qqmaster.service.impl.FaceRecognizerServiceImpl;
import com.qqmaster.util.CommonUtils;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;

/**
 * I couldn't find any tutorial on how to perform face recognition using OpenCV and Java,
 * so I decided to share a viable solution here. The solution is very inefficient in its
 * current form as the training model is built at each run, however it shows what's needed
 * to make it work.
 *
 * The class below takes two arguments: The path to the directory containing the training
 * faces and the path to the image you want to classify. Not that all images has to be of
 * the same size and that the faces already has to be cropped out of their original images
 * (Take a look here http://fivedots.coe.psu.ac.th/~ad/jg/nui07/index.html if you haven't
 * done the face detection yet).
 *
 * For the simplicity of this post, the class also requires that the training images have
 * filename format: <label>-rest_of_filename.png. For example:
 *
 * 1-jon_doe_1.png
 * 1-jon_doe_2.png
 * 2-jane_doe_1.png
 * 2-jane_doe_2.png
 * ...and so on.
 *
 */
public class OpenCVFaceRecognizer {

	public static void main(String[] args) {
		String trainDir = SystemConstant.TRAIN_FILE_DIR;
		String testDir = SystemConstant.TEST_FILE_DIR;
		
		FaceRecognizer faceRecognizer = createLBPHFaceRecognizer();
//		FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
//		FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
		
//		MultiMediaUtils.trainFaceRecognizerWithImages(faceRecognizer, trainDir);
//		faceRecognizer.save(SystemConstant.TRAIN_FILE);
		CommonUtils.trainFaceRecognizerWithImages(faceRecognizer, trainDir);
		CommonUtils.detectFaceInImages(faceRecognizer, testDir);
	}
}
