package com.gsccs.smart.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gsccs.smart.R;
import com.gsccs.smart.adapter.NoticeListAdapter;
import com.gsccs.smart.model.ArticleEntity;
import com.gsccs.smart.network.ArticleHttps;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.speech.setting.TtsSettings;
import com.gsccs.smart.view.NoticeRecyclerView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 语音播报
 */
public class NoticeActivity extends Activity implements OnClickListener {

	private static String TAG = NoticeActivity.class.getSimpleName();
	// 语音合成对象
	private SpeechSynthesizer mTts;
	// 默认发音人
	private String voicer = "xiaoyan";
	
	// 缓冲进度
	private int mPercentForBuffering = 0;
	// 播放进度
	private int mPercentForPlaying = 0;
	
	// 云端/本地单选按钮
	private RadioGroup mRadioGroup;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	
	private Toast mToast;
	private SharedPreferences mSharedPreferences;

	@Bind(R.id.back_ico)
	ImageView mHeadImageView;
	@Bind(R.id.back_head)
	TextView mHeadTextView;

	@Bind(R.id.tts_play)						  //操作按钮
	Button mButton;
	@Bind(R.id.radio_list_view)
	NoticeRecyclerView mArticleRecyclerView;   //上拉加载
	@Bind(R.id.refresh_layout)
	SwipeRefreshLayout swipeRefreshLayout;      //下拉刷新
	private NoticeListAdapter mNoticeAdapter;

	private ArrayList<ArticleEntity> notices = new ArrayList<ArticleEntity>();
	private int page = 0;

	@SuppressLint("ShowToast")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notice_activity);
		ButterKnife.bind(this);

		mHeadTextView.setText(R.string.title_notice);
		mHeadImageView.setOnClickListener(this);

		SpeechUtility.createUtility(this, SpeechConstant.APPID +"=58256cbc");
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
		mSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, MODE_PRIVATE);
		mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);

		//保持固定的大小
		mArticleRecyclerView.setHasFixedSize(true);

		mArticleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mArticleRecyclerView.setAutoLoadMoreEnable(true);
		mArticleRecyclerView.setLoadMoreListener(new NoticeRecyclerView.LoadMoreListener() {
			@Override
			public void onLoadMore() {
				mArticleRecyclerView.postDelayed(new Runnable() {
					@Override
					public void run() {
						loadNoticeData();
						swipeRefreshLayout.setRefreshing(false);
						//mNoticeAdapter.addDatas(DummyContent.generyData(++page));
						mArticleRecyclerView.notifyMoreFinish(true);
					}
				}, 1000);
			}
		});

		mNoticeAdapter = new NoticeListAdapter(this);
		mNoticeAdapter.setOnItemClickListener(new NoticeListAdapter.onRecyclerViewItemClickListener(){
			@Override
			public void onItemClick(View v, int id) {
				mButton.callOnClick();
			}
		});
		mArticleRecyclerView.setAdapter(mNoticeAdapter);

		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipeRefreshLayout.setRefreshing(false);
				page = 0;
				loadNoticeData();
				mArticleRecyclerView.setAutoLoadMoreEnable(true);
				mNoticeAdapter.notifyDataSetChanged();
			}
		});

		mButton.setOnClickListener(this);
		loadNoticeData();
	}


	private void loadNoticeData(){
		ArticleHttps.getInstance(this).queryNoticePageList(page, refreshHandler);
	}



	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.back_ico:
			finish();
			break;
		case R.id.tts_play:
			String title = (String)mButton.getText();
			if(title.equals(R.string.tts_play)){
				mButton.setText(R.string.tts_pause);
			}
			if(title.equals(R.string.tts_pause)){
				mButton.setText(R.string.tts_resume);
			}

			// 移动数据分析，收集开始合成事件
			FlowerCollector.onEvent(this, "tts_play");
			// 设置参数
			setParam();
			Log.d("noticeSize",""+notices.size());
			for(ArticleEntity article:notices){
				int code = mTts.startSpeaking(article.getContent(), mTtsListener);
				Log.d("Speech","code:"+code);
				if (code != ErrorCode.SUCCESS) {
					showTip("语音合成失败,错误码: " + code);
				}
			}
			break;
		/*// 取消合成
		case R.id.tts_cancel:
			mTts.stopSpeaking();
			break;
		// 暂停播放
		case R.id.tts_pause:
			mTts.pauseSpeaking();
			break;
		// 继续播放
		case R.id.tts_resume:
			mTts.resumeSpeaking();
			break;*/
		// 选择发音人
		/*case R.id.tts_btn_person_select:
			showPresonSelectDialog();
			break;
			*/
		}
	}

	private int selectedNum = 0;

	/**
	 * 发音人选择。
	 */
	private void showPresonSelectDialog() {
		switch (mRadioGroup.getCheckedRadioButtonId()) {
		// 选择在线合成
		/*case R.id.tts_radioCloud:
			new AlertDialog.Builder(this).setTitle("在线合成发音人选项")
				.setSingleChoiceItems(mCloudVoicersEntries, // 单选框有几项,各是什么名字
						selectedNum, // 默认的选项
						new DialogInterface.OnClickListener() { // 点击单选框后的处理
					public void onClick(DialogInterface dialog,
							int which) { // 点击了哪一项
						voicer = mCloudVoicersValue[which];
						//((EditText) findViewById(R.id.tts_text)).setText(R.string.text_tts_source);
						selectedNum = which;
						dialog.dismiss();
					}
				}).show();
			break;*/
		default:
			break;
		}
	}

	/**
	 * 初始化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
        		showTip("初始化失败,错误码："+code);
        	} else {
				// 初始化成功，之后可以调用startSpeaking方法
        		// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
        		// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}		
		}
	};

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		
		@Override
		public void onSpeakBegin() {
			showTip("开始播放");
			mButton.setText(R.string.tts_pause);
		}

		@Override
		public void onSpeakPaused() {
			showTip("暂停播放");
			mButton.setText(R.string.tts_resume);
		}

		@Override
		public void onSpeakResumed() {
			showTip("继续播放");
			//mButton.setText(R.string.tts_pause);
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			// 合成进度
			mPercentForBuffering = percent;
			/*showTip(String.format(getString(R.string.tts_toast_format),
					mPercentForBuffering, mPercentForPlaying));*/
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// 播放进度
			mPercentForPlaying = percent;
			showTip(String.format(getString(R.string.tts_toast_format),
					mPercentForBuffering, mPercentForPlaying));
		}

		@Override
		public void onCompleted(SpeechError error) {
			if (error == null) {
				showTip("播放完成");
				mButton.setText(R.string.tts_play);
			} else if (error != null) {
				showTip(error.getPlainDescription(true));
			}
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			// 若使用本地能力，会话id为null
			//	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			//		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			//		Log.d(TAG, "session id =" + sid);
			//	}
		}
	};

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 参数设置
	 * @param
	 * @return 
	 */
	private void setParam(){
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 根据合成引擎设置相应参数
		if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			// 设置在线合成发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
			//设置合成语速
			mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
			//设置合成音调
			mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
			//设置合成音量
			mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
		}else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
			// 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME, "");

		}
		//设置播放器音频流类型
		mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
		// 设置播放合成音频打断音乐播放，默认为true
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
		
		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.wav");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTts.stopSpeaking();
		// 退出时释放连接
		mTts.destroy();
	}
	
	@Override
	protected void onResume() {
		//移动数据统计分析
		FlowerCollector.onResume(this);
		FlowerCollector.onPageStart(TAG);
		super.onResume();
	}
	@Override
	protected void onPause() {
		//移动数据统计分析
		FlowerCollector.onPageEnd(TAG);
		FlowerCollector.onPause(this);
		super.onPause();
	}


	private Handler refreshHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case BaseConst.WHAT_NOTICE_PAGELIST:
					List<ArticleEntity> list = (List<ArticleEntity>) msg.obj;
					if (list.size() == 0) {

					}
					notices.addAll(list);
					//SystemToastDialog.showShortToast(getActivity(), "数据列表："+articles.size());
					mNoticeAdapter.addList(list);
					mArticleRecyclerView.notifyMoreFinish(true);
					//mNoticeAdapter.notifyDataSetChanged();
					break;
				default:
					// activityListView.loadCompleted();
					break;
			}
		}
	};

}
