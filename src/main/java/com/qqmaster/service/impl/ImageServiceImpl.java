package com.qqmaster.service.impl;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacv.Frame;

import com.qqmaster.service.ImageService;

public class ImageServiceImpl implements ImageService{

	@Override
	public boolean faceDetection(List<Frame> frames) throws Exception {
		int size = frames.size();
		AtomicInteger count = new AtomicInteger(0);

		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(size);
		CountDownLatch latch = new CountDownLatch(size);

		frames.forEach(frame->{
			fixedThreadPool.execute(()->{
				if(containsFace(frame)){
					count.incrementAndGet();
				}
				latch.countDown();
			});

		});
//		LBPHFaceRecognizer faceRecognizer = LBPHFaceRecognizer();
		FaceRecognizer faceRecognizer = new FaceRecognizer(new Pointer());
		faceRecognizer.getThreshold();
		latch.await();
		//如果超出一半的帧检测出人脸，则返回true，否则返回false
		if(count.get() > (size>>1))
			return true;
		return false;
	}

	@Override
	public boolean isBlackScreen(Frame frame) {

		return false;
	}

	@Override
	public boolean isWriteScreen(Frame Frame) {

		return false;
	}

	private boolean containsFace(Frame frame){
		
		

		return true;
	}
	
	
	

}
