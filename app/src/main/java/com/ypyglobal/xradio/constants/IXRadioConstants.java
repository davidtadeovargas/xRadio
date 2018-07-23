package com.ypyglobal.xradio.constants;


public interface IXRadioConstants {
	
	public static final boolean DEBUG = false;

	public static final String TAG = "DCM";

	public static final int NUMBER_INSTALL_DAYS=0;//it is the number install days to show dialog rate.default is 0
	public static final int NUMBER_LAUNCH_TIMES=3;//it is the number launch times to show dialog rate.default is 3
	public static final int REMIND_TIME_INTERVAL=1;//it is the number repeat days to show dialog rate.default is 1

    public static final String YOUR_CONTACT_EMAIL = "YOUR_CONTACT_EMAIL";
    public static final String URL_FACEBOOK = "URL_FACEBOOK";
    public static final String URL_TWITTER = "URL_TWITTER";
    public static final String URL_WEBSITE = "URL_WEBSITE";
    public static final String URL_INSTAGRAM = "URL_INSTAGRAM";

	public static final boolean OFFLINE_UI_CONFIG = false;

	public static final boolean SHOW_ADS = true; //enable all ads

	public static final boolean AUTO_PLAY_IN_SINGLE_MODE = true; //enable auto play in single mode
	public static final boolean BLUR_BACKGROUND_IN_SINGLE_MODE = true; //Blur background in single mode

	public static final int INTERSTITIAL_FREQUENCY = 3; //click each item radio to show this one

	public static final boolean RESET_TIMER = true; // reset timer when exiting application

	public static final String DIR_CACHE = "xradio";

	public static final String ADMOB_TEST_DEVICE = "D4BE0E7875BD1DDE0C1C7C9CF169EB6E";
	public static final String FACEBOOK_TEST_DEVICE = "fa7ca73be399926111af1f5aa142b2d2";

	public static final int NUMBER_ITEM_PER_PAGE = 10;
	public static final int MAX_PAGE = 20;

	public static final int TYPE_TAB_FEATURED = 2;
	public static final int TYPE_TAB_GENRE = 3;
	public static final int TYPE_TAB_THEMES = 4;
	public static final int TYPE_TAB_FAVORITE = 5;
	public static final int TYPE_UI_CONFIG = 6;
	public static final int TYPE_DETAIL_GENRE = 7;
	public static final int TYPE_SEARCH = 8;
	public static final int TYPE_SINGLE_RADIO = 9;

	public static final String KEY_ALLOW_MORE = "allow_more";
	public static final String KEY_IS_TAB = "is_tab";
	public static final String KEY_TYPE_FRAGMENT = "type";
	public static final String KEY_ALLOW_READ_CACHE = "read_cache";
	public static final String KEY_ALLOW_REFRESH = "allow_refresh";
	public static final String KEY_ALLOW_SHOW_NO_DATA = "allow_show_no_data";
    public static final String KEY_READ_CACHE_WHEN_NO_DATA = "cache_when_no_data";
    public static final String KEY_GENRE_ID = "cat_id";
    public static final String KEY_SEARCH = "search_data";

	public static final String KEY_NUMBER_ITEM_PER_PAGE = "number_item_page";
	public static final String KEY_MAX_PAGE = "max_page";
	public static final String KEY_OFFLINE_DATA = "offline_data";

	public static final String DIR_TEMP = ".temp";

	public static final String FILE_CONFIGURE= "config.json";
	public static final String FILE_RADIOS= "radios.json";
	public static final String FILE_THEMES= "themes.json";
	public static final String FILE_UI= "ui.json";


	public static final int TYPE_APP_SINGLE= 1;
	public static final int TYPE_APP_MULTI= 2;

	public static final int UI_FLAT_GRID= 1;
	public static final int UI_FLAT_LIST= 2;
	public static final int UI_CARD_GRID= 3;
	public static final int UI_CARD_LIST= 4;
	public static final int UI_MAGIC_GRID= 5;

	public static final int UI_BG_JUST_ACTIONBAR = 0;
	public static final int UI_BG_FULL = 1;

	public static final int UI_PLAYER_SQUARE_DISK= 1;
	public static final int UI_PLAYER_CIRCLE_DISK= 2;
	public static final int UI_PLAYER_ROTATE_DISK= 3;

	public static final float RATE_MAGIC_HEIGHT=1.5f;

	public static final String TAG_FRAGMENT_DETAIL_GENRE = "TAG_FRAGMENT_DETAIL_GENRE";
	public static final String TAG_FRAGMENT_DETAIL_SEARCH = "TAG_FRAGMENT_DETAIL_SEARCH";
	public static final boolean ALLOW_DRAG_DROP_WHEN_EXPAND = false;

	public static final String FORMAT_LAST_FM = "http://ws.audioscrobbler.com/2.0/?method=track.search&track=%1$s&api_key=%2$s&format=json&limit=1";

	public static final long DELTA_TIME = 30000;
	public static final long DEGREE = 6;
	public static final long ONE_HOUR = 3600000;

	public static final int MAX_SLEEP_MODE = 120;
	public static final int MIN_SLEEP_MODE = 5;
	public static final int STEP_SLEEP_MODE = 5;




}
