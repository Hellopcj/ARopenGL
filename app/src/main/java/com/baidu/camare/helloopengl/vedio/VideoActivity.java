package com.baidu.camare.helloopengl.vedio;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.camare.helloopengl.R;

public class VideoActivity extends Activity {
	private Button btn_VideoStart, btn_VideoStop;
	private SurfaceView sv_view;
	private boolean isRecording;
	private MediaRecorder mediaRecorder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);

		btn_VideoStart = (Button) findViewById(R.id.btn_VideoStart);
		btn_VideoStop = (Button) findViewById(R.id.btn_VideoStop);
		sv_view = (SurfaceView) findViewById(R.id.sv_view);

		btn_VideoStop.setEnabled(false);

		btn_VideoStart.setOnClickListener(click);
		btn_VideoStop.setOnClickListener(click);
		
		sv_view.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_VideoStart:
				start();
				break;
			case R.id.btn_VideoStop:
				stop();
				break;
			default:
				break;
			}
		}
	};

	protected void start() {
		try {
			File file = new File("/sdcard/video.mp4");
			if (file.exists()) {
				file.delete();
			}
			
			mediaRecorder = new MediaRecorder();
			mediaRecorder.reset();
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
			mediaRecorder.setVideoFrameRate(4);
			mediaRecorder.setOutputFile(file.getAbsolutePath());
			mediaRecorder.setPreviewDisplay(sv_view.getHolder().getSurface());
			
			mediaRecorder.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public void onError(MediaRecorder mr, int what, int extra) {
					mediaRecorder.stop();
					mediaRecorder.release();
					mediaRecorder = null;
					isRecording=false;
					btn_VideoStart.setEnabled(true);
					btn_VideoStop.setEnabled(false);
					Toast.makeText(VideoActivity.this, "error", Toast.LENGTH_SHORT).show();
				}
			});
			
			mediaRecorder.prepare();
			mediaRecorder.start();

			btn_VideoStart.setEnabled(false);
			btn_VideoStop.setEnabled(true);
			isRecording = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void stop() {
		if (isRecording) {
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
			isRecording=false;
			btn_VideoStart.setEnabled(true);
			btn_VideoStop.setEnabled(false);
		}
	}

	@Override
	protected void onDestroy() {
		if (isRecording) {
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
		}
		super.onDestroy();
	}

}
