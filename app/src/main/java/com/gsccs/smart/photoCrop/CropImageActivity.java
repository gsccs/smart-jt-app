/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gsccs.smart.photoCrop;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.gsccs.smart.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;


public class CropImageActivity extends MonitoredActivity {
	private static final String TAG = "CropImageActivity";

	private CompressFormat mOutputFormat = CompressFormat.JPEG; // only
	private Uri mSaveUri = null;
	private int mAspectX, mAspectY;
	private boolean mCircleCrop = false;
	private final Handler mHandler = new Handler();
	private int mOutputX, mOutputY;
	private boolean mScale;
	private boolean mScaleUp = true;

	boolean mWaitingToPick; // Whether we are wait the user to pick a face.
	boolean mSaving; // Whether the "save" button is already clicked.

	private CropImageView mImageView;
	private ContentResolver mContentResolver;

	private Bitmap mBitmap;
	private final BitmapManager.ThreadSet mDecodingThreads = new BitmapManager.ThreadSet();
	HighlightView mCrop;
	private IImage mImage;
	private String mImagePath;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mContentResolver = getContentResolver();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_crop_view);

		mImageView = (CropImageView) findViewById(R.id.image);

		// 4.0图像处理有个原生bug，需要禁止硬件加速
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
				|| Build.VERSION.SDK_INT == Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			if (extras.getString("circleCrop") != null) {
				mCircleCrop = true;
				mAspectX = 1;
				mAspectY = 1;
			}

			mImagePath = extras.getString("image-path");

			if (mImagePath == null) {
				Toast.makeText(this, "抱歉,无法获取缓存路径！", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}

			// if (extras.getString("selectphoto") != null) {
			// mBitmap = getBitmap();
			// } else {
			// mBitmap = getBitmap(mImagePath);
			// }
			// 图片旋转
			int rotate = ImageUtil.getRotate(mImagePath);// 旋转度数
			WindowManager winManager = getWindowManager();
			DisplayMetrics metrics = new DisplayMetrics();
			winManager.getDefaultDisplay().getMetrics(metrics);
			mBitmap = BitmapUtils.getBmpFromFile(mImagePath,
					metrics.widthPixels, metrics.heightPixels, null);
			if (!TextUtils.isEmpty(extras.getString("image-resultPath"))) {// 是否是相册获取的图片，相册获取图片压缩后需保存到另外一个路径，以免修改了原相册图片
				mImagePath = extras.getString("image-resultPath");
			}
			try {
				//将图片压缩后，再保存到新路径下
				mBitmap.compress(CompressFormat.JPEG, 100,
						new FileOutputStream(mImagePath));
				// 旋转图片
				if (rotate > 0) {
					/*
					 * bmp.recycle(); bmp=null;
					 */
					Bitmap tempBitmap = BitmapFactory.decodeFile(mImagePath);
					Matrix m = new Matrix();
					m.postRotate(rotate);
					Bitmap newBitmap = Bitmap.createBitmap(tempBitmap, 0, 0,
							tempBitmap.getWidth(), tempBitmap.getHeight(), m,
							true);
					mBitmap = newBitmap;
					tempBitmap.recycle();
					tempBitmap = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			mSaveUri = getImageUri(mImagePath);

			mAspectX = extras.getInt("aspectX");
			mAspectY = extras.getInt("aspectY");
			mOutputX = extras.getInt("outputX");
			mOutputY = extras.getInt("outputY");
			mScale = extras.getBoolean("scale", true);
			mScaleUp = extras.getBoolean("scaleUpIfNeeded", true);
		}

		if (mBitmap == null) {
			Log.d(TAG, "finish!!!");
			finish();
			return;
		}

		// Make UI fullscreen.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		findViewById(R.id.discard).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						Intent in = new Intent();
						in.putExtra(
								AbstractPhotoActivity.RESULT_SELECTED_PHOTO_PATH,
								"");
						setResult(RESULT_OK, in);
						finish();
					}
				});

		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onSaveClicked();
			}
		});
		startFaceDetection();
	}

	private Uri getImageUri(String path) {
		return Uri.fromFile(new File(path));
	}

	private void startFaceDetection() {
		if (isFinishing()) {
			return;
		}

		mImageView.setImageBitmapResetBase(mBitmap, true);

		Util.startBackgroundJob(this, null, "请稍候...\u2026", new Runnable() {
			public void run() {
				final CountDownLatch latch = new CountDownLatch(1);
				final Bitmap b = (mImage != null) ? mImage.fullSizeBitmap(
						IImage.UNCONSTRAINED, 1024 * 1024) : mBitmap;
				mHandler.post(new Runnable() {
					public void run() {
						if (b != mBitmap && b != null) {
							mImageView.setImageBitmapResetBase(b, true);
							mBitmap.recycle();
							mBitmap = b;
						}
						if (mImageView.getScale() == 1F) {
							mImageView.center(true, true);
						}
						latch.countDown();
					}
				});
				try {
					latch.await();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				mRunFaceDetection.run();
			}
		}, mHandler);
	}

	private void onSaveClicked() {
		if (mSaving) {
			return;
		}
		if (mCrop == null) {
			return;
		}

		mSaving = true;
		Rect r = mCrop.getCropRect();

		int width = r.width();
		int height = r.height();
		Bitmap croppedImage = null;
		try {
			croppedImage = Bitmap.createBitmap(width, height,
					mCircleCrop ? Bitmap.Config.ARGB_8888
							: Bitmap.Config.RGB_565);
			{
				Canvas canvas = new Canvas(croppedImage);
				Rect dstRect = new Rect(0, 0, width, height);
				canvas.drawBitmap(mBitmap, r, dstRect, null);
			}
			if (mOutputX != 0 && mOutputY != 0) {
				if (mScale) {
					Bitmap old = croppedImage;
					croppedImage = Util.transform(new Matrix(), croppedImage,
							mOutputX, mOutputY, mScaleUp);
					if (old != croppedImage) {
						old.recycle();
					}
				} else {
					Bitmap b = Bitmap.createBitmap(mOutputX, mOutputY,
							Bitmap.Config.RGB_565);
					Canvas canvas = new Canvas(b);

					Rect srcRect = mCrop.getCropRect();
					Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);

					int dx = (srcRect.width() - dstRect.width()) / 2;
					int dy = (srcRect.height() - dstRect.height()) / 2;

					/* If the srcRect is too big, use the center part of it. */
					srcRect.inset(Math.max(0, dx), Math.max(0, dy));

					/* If the dstRect is too big, use the center part of it. */
					dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

					/* Draw the cropped bitmap in the center */
					canvas.drawBitmap(mBitmap, srcRect, dstRect, null);

					/* Set the cropped bitmap as the new bitmap */
					croppedImage.recycle();
					croppedImage = b;
				}
			}
			Bundle myExtras = getIntent().getExtras();
			if (myExtras != null
					&& (myExtras.getParcelable("data") != null || myExtras
							.getBoolean("return-data"))) {
				Intent in = new Intent();
				in.putExtra(
						AbstractPhotoActivity.RESULT_SELECTED_PHOTO_PATH,
						"");
				setResult(RESULT_OK, in);
				finish();
			} else {
				saveOutput(croppedImage);
			}
		} catch (OutOfMemoryError e) {
			if (croppedImage != null && !croppedImage.isRecycled())
				croppedImage.recycle();
			Toast.makeText(this, "内存不足", Toast.LENGTH_SHORT).show();
			System.gc();
			finish();
		}

	}

	private void saveOutput(Bitmap croppedImage) {
		if (mSaveUri != null) {
			OutputStream outputStream = null;
			try {
				outputStream = mContentResolver.openOutputStream(mSaveUri);
				if (outputStream != null) {
					croppedImage.compress(mOutputFormat, 90, outputStream);
				}
			} catch (IOException ex) {
				Log.e(TAG, "Cannot open file: " + mSaveUri, ex);
			} finally {
				Util.closeSilently(outputStream);
			}
			Intent in = new Intent();
			in.putExtra(
					AbstractPhotoActivity.RESULT_SELECTED_PHOTO_PATH,
					mImagePath);
			setResult(RESULT_OK, in);
		}
		croppedImage.recycle();

		finish();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		BitmapManager.instance().cancelThreadDecoding(mDecodingThreads);
		if (mBitmap != null && !mBitmap.isRecycled())
			mBitmap.recycle();
	}

	Runnable mRunFaceDetection = new Runnable() {
		float mScale = 1F;
		Matrix mImageMatrix;
		FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
		int mNumFaces;

		private void makeDefault() {
			HighlightView hv = new HighlightView(mImageView);

			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();

			Rect imageRect = new Rect(0, 0, width, height);

			int cropWidth = 0, cropHeight = 0;

			if (mAspectX != 0 && mAspectY != 0) {
				if (width > height * mAspectX / mAspectY) {
					cropWidth = height * mAspectX / mAspectY;
					cropHeight = height;
				} else {
					cropWidth = width;
					cropHeight = width * mAspectY / mAspectX;
				}
			} else {
				cropWidth = width;
				cropHeight = height;
			}

			int x = (width - cropWidth) / 2;
			int y = (height - cropHeight) / 2;

			RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
			hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop,
					mAspectX != 0 && mAspectY != 0);
			mImageView.add(hv);
		}

		public void run() {
			mImageMatrix = mImageView.getImageMatrix();

			/*
			 * Bitmap faceBitmap = prepareBitmap();
			 * 
			 * mScale = 1.0F / mScale; if (faceBitmap != null &&
			 * mDoFaceDetection) { FaceDetector detector = new
			 * FaceDetector(faceBitmap.getWidth(), faceBitmap.getHeight(),
			 * mFaces.length); mNumFaces = detector.findFaces(faceBitmap,
			 * mFaces); }
			 * 
			 * if (faceBitmap != null && faceBitmap != mBitmap) {
			 * faceBitmap.recycle(); }
			 */

			mHandler.post(new Runnable() {
				public void run() {
					mWaitingToPick = mNumFaces > 1;
					/*
					 * if (mNumFaces > 0) { for (int i = 0; i < mNumFaces; i++)
					 * { handleFace(mFaces[i]); } } else { makeDefault(); }
					 */
					makeDefault();
					mImageView.invalidate();
					if (mImageView.mHighlightViews.size() == 1) {
						mCrop = mImageView.mHighlightViews.get(0);
						mCrop.setFocus(true);
					}

					if (mNumFaces > 1) {
						Toast t = Toast.makeText(CropImageActivity.this,
								"Multi face crop help", Toast.LENGTH_SHORT);
						t.show();
					}
				}
			});
		}
	};

}