package com.baidu.camare.helloopengl.vedio;

import java.io.File;
import android.app.Activity;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.camare.helloopengl.R;

public class RecordActivity extends Activity {
	private Button btn_RecordStart, btn_RecordStop;
	private MediaRecorder mediaRecorder;
	private boolean isRecording;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);

		btn_RecordStart = (Button) findViewById(R.id.btn_RecordStart);
		btn_RecordStop = (Button) findViewById(R.id.btn_RecordStop);
		
		btn_RecordStop.setEnabled(false);
		
		btn_RecordStart.setOnClickListener(click);
		btn_RecordStop.setOnClickListener(click);
	}

	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_RecordStart:
				start();
				break;
			case R.id.btn_RecordStop:
				stop();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 开始录像
	 */
	protected void start() {
		try {
			File file = new File("/sdcard/mediarecorder.amr");
			if (file.exists()) {
				file.delete();
			}
			mediaRecorder = new MediaRecorder();
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mediaRecorder.setOutputFile(file.getAbsolutePath());

			mediaRecorder.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public void onError(MediaRecorder mr, int what, int extra) {
					mediaRecorder.stop();
					mediaRecorder.release();
					mediaRecorder = null;
					isRecording=false;
					btn_RecordStart.setEnabled(true);
					btn_RecordStop.setEnabled(false);
					Toast.makeText(RecordActivity.this, "¼����������", 0).show();
				}
			});
			
			mediaRecorder.prepare();
			mediaRecorder.start();
			
			isRecording=true;
			btn_RecordStart.setEnabled(false);
			btn_RecordStop.setEnabled(true);
			Toast.makeText(RecordActivity.this, "开始录像", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止录像
	 */
	protected void stop() {
		if (isRecording) {
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
			isRecording=false;
			btn_RecordStart.setEnabled(true);
			btn_RecordStop.setEnabled(false);
			Toast.makeText(RecordActivity.this, "停止录像", Toast.LENGTH_SHORT).show();
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
