package com.gsccs.smart.network;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public interface BaseConst {

	String APP_SHARE_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.gsccs.smart";
	/** 微信支付及分享的 APPID */
	String WX_PAY_APP_ID = "wx503067fd9357c0eb";
	
	SimpleDateFormat SDF_YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat SDF_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	/** 用户出生日期后台传递过来的日期格式 */
	SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	/** 日期选择器默认的日期初始值 */
	SimpleDateFormat SDF_YEAR_MONTH_DATE = new SimpleDateFormat("yyyy年MM月dd日");
	/** 保存Sessionkey值的xml文件 */
	String PREFERENCE_SESSION_KEY = "user_session_key";
	/** 保存到Sharepreference 中 sessionkey的名称 */
	String SESSION_KEY = "session_key";
	/** 是否登录/注册操作常量 */
	String IS_SIGN_IN= "IS_SIGN_IN";

	/** 服务器路径 */
	String SERVER_IP_PORT = "117.156.25.172:7001";
	//String SERVER_IP_PORT = "192.168.1.14:7080/zhjt";
	//
	String SERVER_URL = "http://"+SERVER_IP_PORT+"/rest";
	/** 图片上传的地址前缀 */
	String UPLOAD_IMAGE_URL = "http://"+SERVER_IP_PORT+"/rest";
	/** 文章详情页 */
	String APP_ARTICLE_URL = "http://"+SERVER_IP_PORT+"/app/";
	/** 关于我们Url */
	String APP_ABOUTUS_URL = "http://"+SERVER_IP_PORT+"/app/aboutus.html";
	/** 随手拍上传图片 */
	String UPLOAD_IMAGE_ACTION_TWEET = "Tweet";
	/** 个人头像上传图片 */
	String UPLOAD_IMAGE_ACTION_AVATAR = "Avatar";
	/** 失物招领上传图片 */
	String UPLOAD_IMAGE_ACTION_LOST = "Lost";

	/** 手机验证码信息 后台方法名 */
	String METHOD_SMS_REGISTER= "sms.register";
	/** 注册信息提交 后台方法名 */
	String METHOD_USER_REGISTER = "user.register";
	/** 手机号登录 */
	String METHOD_USER_LOGIN = "user.login";

	/* ************** 信息界面相关接口 ******************* */
	/** 信息首页数据接口 */
	String METHOD_APP_INDEX = "app.index";
	/** 信息首页分页查询—— 查询更多 */
	String METHOD_INDEX_ARTICLE_PAGELIST = "index.article.pageList";
	/* ********************* 个人信息 ***begin***************** */
	/** 更新用户经纬度坐标 */
	String METHOD_USESR_POSTION_UPDATE = "user.position.update";
	/** 完善用户信息 */
	String METHOD_IMPROVE_USERINFO = "user.update";
	/** 用户详情查询 */
	String METHOD_USER_DETAILS = "user.details";
	/** 找回登录密码（发送验证码） */
	String METHOD_GETBACK_LOGINPWD_SMSCODE = "getback.loginpwd.smscode";
	/** 找回登录密码（重设登录密码） */
	String METHOD_GETBACK_LOGINPWD_SUBMIT = "getback.loginpwd.submit";
	/** 修改登录密码 */
	String METHOD_RESET_LOGINPWD = "reset.loginpwd";
	/** 加V认证 */
	String METHOD_USER_CERTIFY_SUBMIT = "user.certify.submit";
	/** 绑定手机号码(发送验证码） */
	String METHOD_PHONE_BINDED_SMSCODE = "phone.binded.smscode";
	/** 绑定手机号码信息提交（提交信息） */
	String METHOD_PHONE_BINDED_SUBMIT = "phone.binded.submit";
	/** 修改手机号码（给原手机号码发送的短信验证码） */
	String METHOD_RESET_PHONE_STEPONE = "reset.phone.stepone";
	/** 修改手机号码（给新手机号码发送的短信验证码） */
	String METHOD_RESET_PHONE_STEPTWO = "reset.phone.steptwo";
	/** 修改手机号码（绑定新的手机号码） */
	String METHOD_RESET_PHONE_SUBMIT = "reset.phone.submit";
	/** 退出登录 */
	String METHOD_USER_LOGOUT = "user.logout";
	/* ********************* 个人信息 ***end***************** */

	/** 分页查询文章列表 */
	String METHOD_ARTICLE_PAGELIST = "article.pageList";
	/** 文章详情 */
	String METHOD_ARTICLE_DETAILS = "article.details";
	/**分页获取公告列表**/
	String METHOD_NOTICE_PAGELIST = "notice.pageList";
	/** 加载更多信息提问（分页查询） */
	String METHOD_ARTICLE_COLLECT_PAGELIST = "article.collect.pageList";
	/** 加载更多信息提问（分页查询） */
	String METHOD_ARTICLE_COLLECT = "article.collect";
	/** 加载更多信息提问（分页查询） */
	String METHOD_ARTICLE_COMMENT_PAGELIST = "article.comment.pageList";
	/** 添加评论或回复 */
	String METHOD_ARTICLE_COMMENT_ADD = "article.comment.add";
	/** 删除评论或回复 */
	String METHOD_ARTICLE_COMMENT_DEL = "article.comment.del";


	/* ************** 交通指数相关接口 ******************* */
	/** 交通指数接口 */
	String METHOD_TRAFFIC_DATA = "traffic.list";
	/** 摄像头数据接口 */
	String METHOD_CAMEAR_PAGELIST = "camera.pageList";


	/** 分页查询消息 */
	String METHOD_TWEET_PAGELIST = "tweet.pageList";
	/** Tweet消息 */
	String METHOD_TWEET_ADD = "tweet.add";
	/** 删除消息 **/
	String METHOD_TWEET_DEL = "tweet.del";
	/** 消息点赞 **/
	String METHOD_TWEET_PRAISE_ADD = "tweet.parise";
	/** 详情 */
	String METHOD_TWEET_DETAILS = "tweet.details";
	/** 加载更多消息评论（分页查询） */
	String METHOD_TWEET_COMMENT_PAGELIST = "tweet.comment.pageList";
	/** 添加评论或回复 */
	String METHOD_TWEET_COMMENT_ADD = "tweet.comment.add";
	/** 删除评论或回复 */
	String METHOD_TWEET_COMMENT_DEL = "tweet.comment.del";
	/** 加载类型 */
	String METHOD_TWEET_TYPE_LIST = "tweet.getTypeList";

	/** 从后台获取Token值 */
	String METHOD_GET_TOKEN = "qiniu.upload.token";

	/** 道路救援分页查询消息 */
	String METHOD_ASSIST_PAGELIST = "roadassist.pageList";
	/** 便捷洗车分页查询消息 */
	String METHOD_WASHCAR_PAGELIST = "washcar.pageList";
	/** 失物招领分页查询消息 */
	String METHOD_LOSTFOUND_PAGELIST = "lostfound.pageList";
	/** 添加失物招领信息 */
	String METHOD_LOSTFOUND_ADD = "lostfound.add";
	/** 失物招领分页查询评论消息 */
	String METHOD_LOSTFOUND_COMMON_PAGELIST = "lostcomment.pageList";
	/** 添加评论或回复 */
	String METHOD_LOSTFOUND_COMMON_ADD = "lostcomment.add";
	/** 删除评论或回复 */
	String METHOD_LOSTFOUND_COMMON_DEL = "lostfound.comment.del";
	/** 添加供求信息 */
	String METHOD_DEMAND_ADD = "supply.add";
	/** 供求信息分页查询消息 */
	String METHOD_DEMAND_PAGELIST = "supply.pageList";
	/** 供求信息分页查询评论消息 */
	String METHOD_DEMAND_COMMENT_PAGELIST = "supply.comment.pageList";
	/** 添加评论或回复 */
	String METHOD_DEMAND_COMMENT_ADD = "supply.comment.add";
	/** 家政服务分页查询消息 */
	String METHOD_DOMESTIC_PAGELIST = "domestic.pageList";
	/** 黄页电话分页查询消息 */
	String METHOD_YELLOW_PAGELIST = "yellowpage.pageList";

	/** 上传图片 */
	String METHOD_UPLOAD_IMAGE = "upload.image";

	/** 查询停车位 */
	String METHOD_PARK_PAGELIST = "park.pageList";

	/** 新增停车位 */
	String METHOD_PARK_ADD = "park.add";

	/** 发布的信息 */
	String METHOD_MYINFO_PAGELIST = "info.pageList";
	/** 发布的信息 */
	String METHOD_MYINFO_DEL = "info.del";
	/** 获取系统版本号 */
	String METHOD_APP_VERSION_GET = "app.version";

	/** 手机验证码 handle msg.what 值 */
	int WHAT_SEND_SMS_CODE = 0x01;
	/** 手机帐号登录 handle msg.what 值 */
	int WHAT_USER_LOGIN_PHONE = 0x02;
	/** 用户注册操作 */
	int WHAT_USER_REGISTER = 0x031;
	/** 用户注册短信验证码操作 */
	int WHAT_SMS_REGISTER = 0x032;
	/** 信息首页数据接口 */
	int WHAT_APP_INDEX = 0x04;
	/** 信息首页分页查询—— 查询更多 */
	int WHAT_INDEX_ARTICLE_PAGELIST = 0x05;
	/** 更新用户经纬度坐标 */
	int WHAT_USESR_POSTION_UPDATE = 0x06;
	/** 完善用户信息 */
	int WHAT_IMPROVE_USERINFO = 0x07;
	/** 用户详情查询 */
	int WHAT_USER_DETAILS = 0x08;
	/** 找回登录密码（发送验证码） */
	int WHAT_GETBACK_LOGINPWD_SMSCODE = 0x09;
	/** 找回登录密码（重设登录密码） */
	int WHAT_GETBACK_LOGINPWD_SUBMIT = 0x10;
	/** 修改登录密码 */
	int WHAT_RESET_LOGINPWD = 0x11;
	/** 加V认证 */
	int WHAT_USER_CERTIFY_SUBMIT = 0x12;
	/** 绑定手机号码(发送验证码） */
	int WHAT_PHONE_BINDED_SMSCODE = 0x13;
	/** 绑定手机号码信息提交（提交信息） */
	int WHAT_PHONE_BINDED_SUBMIT = 0x14;
	/** 修改手机号码（给原手机号码发送的短信验证码） */
	int WHAT_RESET_PHONE_STEPONE = 0x15;
	/** 修改手机号码（给新手机号码发送的短信验证码） */
	int WHAT_RESET_PHONE_STEPTWO = 0x16;
	/** 修改手机号码（绑定新的手机号码） */
	int WHAT_RESET_PHONE_SUBMIT = 0x17;
	/** 退出登录 */
	int WHAT_USER_LOGOUT = 0x18;

	/** 分页查询文章列表 */
	int WHAT_ARTICLE_PAGELIST = 0x20;
	/** 信息详情 */
	int WHAT_ARTICLE_DETAILS = 0x201;
	/** 加载更多信息提问（分页查询） */
	int WHAT_ARTICLE_COMMENT_PAGELIST = 0x202;
	/** 添加评论或回复 */
	int WHAT_ARTICLE_COMMENT_ADD = 0x203;
	/** 删除评论或回复 */
	int WHAT_ARTICLE_COMMENT_DEL = 0x204;// 25被后台使用了，后台："code":"25","errorToken":"@@$-ERROR_TOKEN$-@@","message":"服务方法(index.article.pageList:1.0)的签名无效","solution":"签名无效

	/** 加载更多信息提问（分页查询） */
	int WHAT_ARTICLE_COLLECT_PAGELIST = 0x205;
	/** 添加评论或回复 */
	int WHAT_ARTICLE_COLLECT = 0x206;

	/** 分页查询公告列表 */
	int WHAT_NOTICE_PAGELIST = 0x21;

	/** 分页查询交通指数列表 */
	int WHAT_TRAFFIC_DATA = 0x22;
	int WHAT_CAMERA_PAGELIST = 0x221;

	/** 分页查询晒晒消息（用于晒晒主页或我的晒晒列表） */
	int WHAT_TWEET_PAGELIST = 0x28;
	/** 发布晒晒消息 */
	int WHAT_TWEET_SEND = 0x29;
	/** 晒晒详情 */
	int WHAT_TWEET_DETAILS = 0x30;
	/** 加载更多消息评论（分页查询） */
	int WHAT_TWEET_COMMENT_PAGELIST = 0x301;
	/** 晒晒—— 添加评论或回复 */
	int WHAT_TWEET_COMMENT_ADD = 0x302;
	/** 晒晒—— 晒删除评论或回复 */
	int WHAT_TWEET_COMMENT_DEL = 0x303;
	/** 晒晒—— 点赞（匿名版） */
	int WHAT_TWEET_PRAISE_ADD = 0x304;
	/** 晒晒—— 删除晒晒消息 */
	int WHAT_TWEET_DEL = 0x306;

	int WHAT_TWEET_TYPE_LIST = 0x307;

	int WHAT_UPLOAD_IMAGE = 0x37;

	/** 从后台获取token值 */
	int WHAT_GET_TOKEN = 0x40;

	/** 分页查询道路救援消息 */
	int WHAT_ASSIST_PAGELIST = 0x41;
	/** 分页查询便捷洗车消息 */
	int WHAT_WASHCAR_PAGELIST = 0x42;
	/** 分页查询失物招领消息 */
	int WHAT_LOSTFOUND_PAGELIST = 0x43;
	/** 新增失物招领消息 */
	int WHAT_LOSTFOUND_ADD = 0x431;
	/** 消息评论（分页查询） */
	int WHAT_LOSTFOUND_COMMENT_PAGELIST = 0x44;
	/** 失物招领 添加评论或回复 */
	int WHAT_LOSTFOUND_COMMENT_ADD = 0x441;
	/** 失物招领删除评论或回复 */
	int WHAT_LOSTFOUND_COMMENT_DEL = 0x442;
	/** 分页查询供求信息 */
	int WHAT_DEMAND_PAGELIST = 0x451;
	/** 新增供求信息 */
	int WHAT_DEMAND_ADD = 0x453;
	/** 分页查询供求信息评论 */
	int WHAT_DEMAND_COMMENT_PAGELIST = 0x452;

	int WHAT_DEMAND_COMMENT_ADD = 0x453;


	/** 分页查询家政服务消息 */
	int WHAT_DOMESTIC_PAGELIST = 0x49;
	/** 分页查询家政消息评论 */
	int WHAT_DOMESTIC_COMMENT_PAGELIST = 0x50;

	/** 分页查询黄页电话消息 */
	int WHAT_YELLOW_PAGELIST = 0x471;

	/** 信息分页查询 */
	int WHAT_MYINFO_PAGELIST = 0x500;
	/** 信息删除 */
	int WHAT_MYINFO_DEL = 0x501;

	/** 搜索停车位 */
	int WHAT_PARK_PAGELIST = 0x70;
	/** 　用户反馈　 */
	int WHAT_PARK_ADD = 0x75;

	/** 获取系统版本号 */
	int WHAT_APP_VERSION_GET = 0x90;

	/** 网络请求失败，非业务类型失败 */
	int CODE_ERROR_FAILURE = 9999;
	/** 失败标志 后台申请成功 ，后台返回的数据中信息表示行为失败 */
	int CODE_SYSTEM_FAILURE = 9000;
	/** 请求成功的code标识 */
	int CODE_SYSTEM_SUCCESS = 200;
	/** 后台请求提示 签名失败，服务不可用，即 MD5验证失败 */
	int CODE_ERROR_SOLUTION_FAILURE = 25;


	public static final int UPVOTE_STATUS_YES = 1;
	public static final int UPVOTE_STATUS_NO = 0;
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";


	public static final String APK_DOWNLOAD_URL = "APK_DOWNLOAD_URL";
	public static final String APK_UPDATE_CONTENT = "updateMessage";   // 更新消息
	public static final String TOOLBAR_TITLE = "toolbar_title";

	//酒泉市中心经纬度
	public static double lat=98.50;
	public static double lng=39.71;
}
