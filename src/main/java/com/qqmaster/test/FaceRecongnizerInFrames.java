package com.qqmaster.test;

import com.qqmaster.common.SystemConstant;
import com.qqmaster.service.impl.FaceRecognizerServiceImpl;
import com.qqmaster.service.impl.VideoServiceImpl;

public class FaceRecongnizerInFrames {

	public static void main(String[] args) throws Exception{

		FaceRecognizerServiceImpl frs = new FaceRecognizerServiceImpl();
		VideoServiceImpl vs = new VideoServiceImpl();

		frs.trainFaceRecognizer(SystemConstant.TRAIN_FILE_DIR);

//		System.out.println("face detect in " + SystemConstant.TEST_VIDEO_WITH_FACE);
//		System.out.println("face detect in " + SystemConstant.TEST_VIDEO_WITHOUT_FACE);
//		System.out.println("face detect in " + SystemConstant.TEST_VIDEO_LESS_FACE);
//		System.out.println("face detect in " + SystemConstant.TEST_VIDEO_MORE_FACE);

		frs.detectFaceInFrames(vs.getUniformFramesOfVideo(SystemConstant.TEST_VIDEO_WITH_FACE));
		frs.detectFaceInFrames(vs.getUniformFramesOfVideo(SystemConstant.TEST_VIDEO_WITHOUT_FACE));
		frs.detectFaceInFrames(vs.getUniformFramesOfVideo(SystemConstant.TEST_VIDEO_LESS_FACE));
		frs.detectFaceInFrames(vs.getUniformFramesOfVideo(SystemConstant.TEST_VIDEO_MORE_FACE));
		
	}
}
