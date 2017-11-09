package com.gsccs.smart.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.gsccs.smart.R;
import com.gsccs.smart.SmartApplication;
import com.gsccs.smart.adapter.PhotoAdapter;
import com.gsccs.smart.fragment.EmojiFragment;
import com.gsccs.smart.model.ImageInfo;
import com.gsccs.smart.network.BaseConst;
import com.gsccs.smart.network.InfoHttps;
import com.gsccs.smart.network.TweetHttps;
import com.gsccs.smart.photoCrop.AbstractPhotoActivity;
import com.gsccs.smart.utils.GlobalUtils;
import com.gsccs.smart.view.HorizontalListView;
import com.gsccs.smart.widget.EnterEmojiLayout;
import com.gsccs.smart.widget.EnterLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;


/**
 *  供求信息发布表单
 */
public class DemandFormActivity extends AbstractPhotoActivity
        implements View.OnClickListener,AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    @Bind(R.id.back_ico)
    ImageView mHeadImageView;
    @Bind(R.id.back_head)
    TextView mHeadTextView;

    @Bind(R.id.btn_send)
    ImageButton btnSend;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.inputLayout)
    LinearLayout inputLayout;
    @Bind(R.id.bottom_layout)
    LinearLayout bottomLayout;

    @Bind(R.id.content)
    EditText mContentView;
    
    @Bind(R.id.tagContainer)
    LinearLayout tagContainer;
    @Bind(R.id.type_check)
    ImageView mTagCheckView;

    @Bind(R.id.photolistview)
    HorizontalListView picListView;

    int selectedType= 0;
    ArrayList<String> mSelectPath;
    ArrayList<ImageInfo> imgList = new ArrayList<>();

    private PhotoAdapter mPhotoAdapter;
    /** 用来保存拍完的照片或者选取照片过后的绝对路径(所有的照片的) */
    private String allPhotoFileString = "";
    /** 保存 拍照或相册获取的图片的为文件夹 com.gsccs.smart */
    // private String photoPath = "com.gsccs.smart";
    private String newPhotoName = "";
    /** 最终生成图片的路径 */
    private String resultPath = "";
    private List<String> picFileList = new ArrayList<String>();
    /** 类型 */
    public List<String> typeList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demand_form_activity);
        ButterKnife.bind(this);

        mHeadTextView.setText("发布供求信息");
        mHeadImageView.setOnClickListener(this);

        mPhotoAdapter = new PhotoAdapter(this, picFileList);
        picListView.setAdapter(mPhotoAdapter);
        picListView.setOnItemClickListener(this);
        picListView.setOnItemLongClickListener(this);

        mTagCheckView.setOnClickListener(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContentView.getText().toString().equals("")) {
                    Toast.makeText(DemandFormActivity.this, "请说点什么吧~", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgress(true);
                int userid = SmartApplication.getCurrUser().getId();
                InfoHttps.getInstance(DemandFormActivity.this).demandAdd(userid,selectedType,
                        mContentView.getText().toString(),picFileList,refreshHandler);

            }
        });

        ThreeBounce threeBounce = new ThreeBounce();
        threeBounce.setColor(getResources().getColor(R.color.colorPrimary));
        progressBar.setIndeterminateDrawable(threeBounce);

        typeList.add("供应");
        typeList.add("需求");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
//                this.finish();
            default:
                this.finish();
                return super.onOptionsItemSelected(item);
        }
    }

    private Handler refreshHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case BaseConst.WHAT_DEMAND_ADD:
                    showProgress(false);
                    mContentView.setText("");
                    imgList.clear();
                    //layPhotoContainer.removeAllViews();
                    GlobalUtils.popSoftkeyboard(DemandFormActivity.this, mContentView, false);
                    Toast.makeText(DemandFormActivity.this, getString(R.string.tweet_send_success), Toast.LENGTH_SHORT).show();
                    DemandFormActivity.this.finish();
                    break;
                default:
                    // activityListView.loadCompleted();
                    break;
            }
        }
    };



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                StringBuilder sb = new StringBuilder();
                //layPhotoContainer.removeAllViews();
                for (String p : mSelectPath) {
//                    sb.append(p);
//                    sb.append("\n");

                    View itemView = View.inflate(DemandFormActivity.this, R.layout.photo_check_view, null);
                    ImageView img = (ImageView) itemView.findViewById(R.id.img);
                    itemView.setTag(p);

                    Picasso.with(DemandFormActivity.this)
                            .load(new File(p))
                            .resize(200, 200)
                            .centerCrop()
                            .into(img);
/*
                    if (layPhotoContainer != null) {
                        layPhotoContainer.addView(itemView,
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                    }
*/

                    imgList.add(new ImageInfo(p));
                }
                //yjPublishEdit.setText(sb.toString());
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            inputLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            bottomLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        } else {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            inputLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            bottomLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DemandFormActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onPhotoSelected(String imgPath) {
        if (!TextUtils.isEmpty(imgPath)) {
            allPhotoFileString += (imgPath + ",");
            setListViewData(mPhotoAdapter, allPhotoFileString);
        }
    }

    /** 对listView中的adapter数组数据设置 */
    private void setListViewData(PhotoAdapter adapter, String fileStr) {
        picFileList = splitlistViewPicString(fileStr);
        adapter.setListData(picFileList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 将resultPath中所有的图片路径进行拆分 并且将值添加到ListView中的list集合
     */
    private List<String> splitlistViewPicString(String fileString) {
        String[] everyPicFile = fileString.split(",");
        List<String> imgList = new ArrayList<String>();
        for (String everyPic : everyPicFile) {
            if (!TextUtils.isEmpty(everyPic)) {
                imgList.add(everyPic);
            }
        }
        return imgList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMddHHmmss");
        newPhotoName = "tweet" + sdf3.format(new Date());
        // 选择保存在这个文件夹，可以随软件 的卸载，程序自动删除这些临时缓存的图片
        resultPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath() + "/" + newPhotoName + ".jpg";
        if (position == mPhotoAdapter.getCount() - 1) {
            popup(this, resultPath, true);
        }
    }




    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        delPicListItem(position);
        return false;
    }


    /**
     * 删除上传的照片
     *
     * @param position
     *            listview中的item
     */
    private void delPicListItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);// 创建一个弹出框
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                picFileList.remove(position);
                String newAllFileList = "";
                for (int i = 0; i < picFileList.size(); i++) {
                    newAllFileList += picFileList.get(i) + ",";
                }
                allPhotoFileString = newAllFileList;
                mPhotoAdapter.setListData(picFileList);
                mPhotoAdapter.notifyDataSetChanged();
            }
        });
        builder.setMessage("是否删除该照片").setTitle("温馨提示").show();
    }

    private void addTags() {
        tagContainer.removeAllViews();
        final String[] tagsStr = new String[typeList.size()];
        for (int i = 0; i < typeList.size(); i++) {
            tagsStr[i] = typeList.get(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("类型：");
        builder.setSingleChoiceItems(tagsStr, selectedType,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedType = which;
                        Log.d("DialogInterface",which+":"+tagsStr[which]);
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != tagsStr && tagsStr.length>0){
                    //selectedType = checkeditemid;
                    //activityVo.setTags(tagsStr[checkeditemid]);
                    TextView textView = new TextView(DemandFormActivity.this);
                    textView.setLayoutParams(new ViewGroup.LayoutParams(getResources()
                            .getDimensionPixelSize(R.dimen.layout_x_128), getResources()
                            .getDimensionPixelSize(R.dimen.layout_y_40)));
                    textView.setText(tagsStr[selectedType]);
                    textView.setTextSize(14);
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setBackgroundColor(getResources().getColor(
                            R.color.green));
                    tagContainer.addView(textView);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_ico:
                finish();
                break;
            case R.id.type_check:
                addTags();
                break;
        }
    }
}
