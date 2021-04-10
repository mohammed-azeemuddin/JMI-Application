package com.gc.jmihelp;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gc.jmihelp.model.ChildModel;
import com.gc.jmihelp.model.HeaderModel;
import com.gc.jmihelp.view.ExpandableNavigationListView;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
//import com.techatmosphere.expandablenavigation.model.ChildModel;
//import com.techatmosphere.expandablenavigation.model.HeaderModel;
//import com.techatmosphere.expandablenavigation.view.ExpandableNavigationListView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    DrawerLayout drawerLayout;
    WebView mWebView;
    private Menu optionsMenu;
    Toolbar toolbar;
    NavigationView navigationView;
    AdView mAdView;
    InterstitialAd mInterstitialAd;
    ProgressBar progressBar;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 101;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private int mOriginalOrientation;
    private int mOriginalSystemUiVisibility;

    private ExpandableNavigationListView listView;
    private Context context;
    private DrawerLayout drawer;
    public boolean isEnglish;
    Button eng_hindi_btn;

    List<String> usernames;
    List<String> images;

    NestedScrollView nestedScrollView;
    //    final String url="https://gurucool.xyz/ECCInstitute";
    final String url="https://gurucool.xyz/";

    final String admob_app_id = "ca-app-pub-3940256099942544~3347511713";
    final String admob_banner_id = "ca-app-pub-3940256099942544/6300978111";
    final String admob_inter_id = "ca-app-pub-3940256099942544/1033173712";
    FrameLayout frameLayout;

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewInit();

        listView = findViewById(R.id.expandable_navigation);
        drawer = findViewById(R.id.drawer_layout);
        isEnglish = true;
        eng_hindi_btn=findViewById(R.id.eng_hindi_btn);
        usernames = new ArrayList<>();
        images = new ArrayList<>();

//        if(getIntent().getExtras() == null) {
//            activateEnglish();
//        }
//        else
        if(getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            boolean eng = extras.getBoolean("eng");
            if(eng)
                activateEnglish();
            else
                activateHindi();
        }else{
            activateEnglish();
        }

        eng_hindi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEnglish=!isEnglish;
                if(isEnglish==true){
                    activateEnglish();
                }else{
                    activateHindi();
                }
            }
        });

//        setTitle("Eeduhub");
//        setTitle("ECC");
//        setTitle("Al-Najah Institute");
//        setTitle("JMI Help");

        mWebView.loadUrl(url);

        setMySwipeRefreshLayout();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        //setmFirebaseAnalytics();

        //floatingActionButton();

        setActionBarToogle();

        webSettings();

        if(getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            mWebView.loadUrl(extras.getString("data"));
        }

        //setRTL();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {

                } else {
                    requestPermission(); // Code for permission
                }
            } else {

            }
        }


    }


    public void activateHindi(){
        listView
                .init(this)
                .addHeaderModel(
                        new HeaderModel("विद्यालय", R.drawable.building, true)
                                .addChildModel(new ChildModel("पाठ्यक्रम"))
                                .addChildModel(new ChildModel("कक्षा"))
                                .addChildModel(new ChildModel("टेस्ट सीरीज"))
                                .addChildModel(new ChildModel("अध्ययन सामग्री"))
                                .addChildModel(new ChildModel("संदेह की निकासी"))
                )
                .addHeaderModel(
                        new HeaderModel("प्रवेश", R.drawable.ic_studymaterial, true)
                                .addChildModel(new ChildModel("पाठ्यक्रम"))
                                .addChildModel(new ChildModel("टेस्ट सीरीज"))
                                .addChildModel(new ChildModel("अध्ययन समूह"))
                                .addChildModel(new ChildModel("अध्ययन सामग्री"))
                                .addChildModel(new ChildModel("समाचार लेख"))
                )
                .addHeaderModel(new HeaderModel("पटना विश्वविद्यालय", R.drawable.ic_patna))
                .addHeaderModel(
                        new HeaderModel("कौशल बिहार", R.drawable.couses, true)
                                .addChildModel(new ChildModel("समावेशी विकास"))
                                .addChildModel(new ChildModel("स्किलिंग"))
                                .addChildModel(new ChildModel("आईटी और कंप्यूटर विज्ञान"))
                                .addChildModel(new ChildModel("मनोविज्ञान और मानसिक स्वास्थ्य"))
                                .addChildModel(new ChildModel("व्यवसाय प्रबंधन"))
                                .addChildModel(new ChildModel("भाषा और साहित्य"))
                )
                .addHeaderModel(new HeaderModel("प्रश्न और उत्तर",R.drawable.qna))
                .addHeaderModel(new HeaderModel("पुस्तक बाजार",R.drawable.bookm))
                .addHeaderModel(new HeaderModel("नौकरियां",R.drawable.jobs))
                .addHeaderModel(new HeaderModel("लेख और अपडेट",R.drawable.blog))
                .addHeaderModel(new HeaderModel("स्पर्धाएँ" + "आयोजन",R.drawable.event))
                .addHeaderModel(new HeaderModel("संपर्क करें",R.drawable.contact))
                .build()
                .addOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        listView.setSelected(groupPosition);
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("key", 1);
                        bundle.putBoolean("eng",false);

                        if (groupPosition == 0) {
                            //School submenu
                            //Common.showToast(context, "School Select");
                        } else if (groupPosition == 1) {
                            //Entrance sub Menu
//                            Common.showToast(context, "Entrance Select");
                        } else if (groupPosition == 2) {
                            //Patna
                            bundle.putString("data", "https://gurucool.xyz/timeline&u=PatnaUniversityHelp&ref=se");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "Patna selected");
                        } else if (groupPosition==3) {
                            //Notifications Menu
//                            Common.showToast(context, "Courses selected");
                        } else if (groupPosition==4) {
                            //Notifications Menu
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=discussion");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "QnA selected");
                        } else if (groupPosition==5) {
                            //Notifications Menu
                            bundle.putString("data", "https://gurucool.xyz/products");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "Book Market selected");
                        } else if (groupPosition==6) {
                            //Notifications Menu
                            bundle.putString("data", "https://gurucool.xyz/jobs");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "Articles and Blog selected");
                        } else if(groupPosition==7){
                            bundle.putString("data", "https://gurucool.xyz/blogs");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                        }
                        else if (groupPosition==8) {
                            //Notifications Menu
                            bundle.putString("data", "https://gurucool.xyz/events");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "Events selected");
                        } else if (groupPosition==9) {
                            //Notifications Menu
                            bundle.putString("data", "https://gurucool.xyz/messages/1068");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            // Common.showToast(context, "Contact Us selected");
                        }
                        return false;
                    }
                })
                .addOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        listView.setSelected(groupPosition, childPosition);
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("key", 1);
                        bundle.putBoolean("eng",false);

                        if (groupPosition==0 && childPosition==0) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=281");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            // Common.showToast(context, "School courses select");
                        } else if (groupPosition==0 && childPosition==1) {
                            bundle.putString("data", "https://gurucool.xyz/?link1=classroompro&id=281");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "School classroom select");
                        } else if (groupPosition==0 && childPosition==2) {
                            bundle.putString("data", "https://gurucool.xyz/?link1=testpro&id=281");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "School test series select");
                        } else if (groupPosition==0 && childPosition==3) {
                            bundle.putString("data", "https://gurucool.xyz/?link1=studyhelppro&id=281");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "School study material select");
                        } else if (groupPosition==0 && childPosition==4) {
                            bundle.putString("data", "https://gurucool.xyz/doubtclearancepadhaai");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "School doubt clearance select");
                        } else if (groupPosition==1 && childPosition == 0) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=1076");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Entrance courses select");
                        } else if (groupPosition==1 && childPosition == 1) {
                            bundle.putString("data", "https://gurucool.xyz/?link1=testpro&id=1076");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Entrance test series select");
                        } else if (groupPosition==1 && childPosition == 2) {
                            bundle.putString("data", "https://gurucool.xyz/?link1=classroompro&id=1076");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Entrance study groups select");
                        } else if (groupPosition==1 && childPosition == 3) {
                            bundle.putString("data", "https://gurucool.xyz/?link1=studyhelppro&id=1076");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Entrance study materials select");
                        } else if (groupPosition==1 && childPosition == 4) {
                            bundle.putString("data", "https://gurucool.xyz/hashtag/upsc");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Entrance news and articles select");
                        } else if (groupPosition==3 && childPosition == 0) {
                            bundle.putString("data", "https://gurucool.xyz/courses");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Courses overall dev select");
                        } else if (groupPosition==3 && childPosition == 1) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=49&folder=106");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Courses Skilling select");
                        } else if (groupPosition==3 && childPosition == 2) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=49&folder=46");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Courses IT CSE select");
                        } else if (groupPosition==3 && childPosition == 3) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=49&folder=107");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Courses Psych and Mental select");
                        } else if (groupPosition==3 && childPosition == 4) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=49&folder=105");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "Courses Business and Managment select");
                        } else if (groupPosition==3 && childPosition == 5) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=49&folder=104");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "Courses Lang and Lit select");
                        }
                        drawer.closeDrawer(GravityCompat.START);
                        return false;
                    }
                });

        listView.setSelected(0);
    }

    public void activateEnglish(){
        listView
                .init(this)
                .addHeaderModel(
                        new HeaderModel("School", R.drawable.building, true)
                                .addChildModel(new ChildModel("Courses"))
                                .addChildModel(new ChildModel("Classroom"))
                                .addChildModel(new ChildModel("Test Series"))
                                .addChildModel(new ChildModel("Study Material"))
                                .addChildModel(new ChildModel("Doubt Clearance"))
                )
                .addHeaderModel(
                        new HeaderModel("Entrance", R.drawable.ic_studymaterial, true)
                                .addChildModel(new ChildModel("Courses"))
                                .addChildModel(new ChildModel("Test Series"))
                                .addChildModel(new ChildModel("Study Groups"))
                                .addChildModel(new ChildModel("Study Materials"))
                                .addChildModel(new ChildModel("News and Articles"))
                )
                .addHeaderModel(new HeaderModel("Patna University", R.drawable.ic_patna))
                .addHeaderModel(
                        new HeaderModel("Skill Bihar", R.drawable.couses, true)
                                .addChildModel(new ChildModel("Overall Development"))
                                .addChildModel(new ChildModel("Skilling"))
                                .addChildModel(new ChildModel("IT and Computer Science"))
                                .addChildModel(new ChildModel("Psychology and Mental Health"))
                                .addChildModel(new ChildModel("Business and Management"))
                                .addChildModel(new ChildModel("Language and Literature"))
                )
                .addHeaderModel(new HeaderModel("QnA",R.drawable.qna))
                .addHeaderModel(new HeaderModel("Book Market",R.drawable.bookm))
                .addHeaderModel(new HeaderModel("Jobs",R.drawable.jobs))
                .addHeaderModel(new HeaderModel("Articles and Blog",R.drawable.blog))
                .addHeaderModel(new HeaderModel("Events",R.drawable.event))
                .addHeaderModel(new HeaderModel("Contact Us",R.drawable.contact))
                .build()
                .addOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        listView.setSelected(groupPosition);
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("key", 1);
                        bundle.putBoolean("eng",true);

                        if (groupPosition == 0) {
                            //School submenu
                            //Common.showToast(context, "School Select");
                        } else if (groupPosition == 1) {
                            //Entrance sub Menu
                            //Common.showToast(context, "Entrance Select");
                        } else if (groupPosition == 2) {
                            //Patna
                            bundle.putString("data", "https://gurucool.xyz/timeline&u=PatnaUniversityHelp&ref=se");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "Patna selected");
                        } else if (groupPosition==3) {
                            //Notifications Menu
                            //Common.showToast(context, "Courses selected");
                        } else if (groupPosition==4) {
                            //Notifications Menu
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=discussion");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "QnA selected");
                        } else if (groupPosition==5) {
                            //Notifications Menu
                            bundle.putString("data", "https://gurucool.xyz/products");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "Book Market selected");
                        } else if (groupPosition==6) {
                            //Notifications Menu
                            bundle.putString("data", "https://gurucool.xyz/jobs");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "Articles and Blog selected");
                        }
                        else if(groupPosition==7){
                            bundle.putString("data", "https://gurucool.xyz/blogs");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                        }
                        else if (groupPosition==8) {
                            //Notifications Menu
                            bundle.putString("data", "https://gurucool.xyz/events");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "Events selected");
                        } else if (groupPosition==9) {
                            //Notifications Menu
                            bundle.putString("data", "https://gurucool.xyz/messages/1068");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            // Common.showToast(context, "Contact Us selected");
                        }
                        return false;
                    }
                })
                .addOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        listView.setSelected(groupPosition, childPosition);
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("key", 1);
                        bundle.putBoolean("eng",true);

                        if (groupPosition==0 && childPosition==0) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=281");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            // Common.showToast(context, "School courses select");
                        } else if (groupPosition==0 && childPosition==1) {
                            bundle.putString("data", "https://gurucool.xyz/?link1=classroompro&id=281");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "School classroom select");
                        } else if (groupPosition==0 && childPosition==2) {
                            bundle.putString("data", "https://gurucool.xyz/?link1=testpro&id=281");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "School test series select");
                        } else if (groupPosition==0 && childPosition==3) {
                            bundle.putString("data", "https://gurucool.xyz/?link1=studyhelppro&id=281");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "School study material select");
                        } else if (groupPosition==0 && childPosition==4) {
                            bundle.putString("data", "https://gurucool.xyz/doubtclearancepadhaai");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "School doubt clearance select");
                        } else if (groupPosition==1 && childPosition == 0) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=1076");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Entrance courses select");
                        } else if (groupPosition==1 && childPosition == 1) {
                            bundle.putString("data", "https://gurucool.xyz/?link1=testpro&id=1076");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Entrance test series select");
                        } else if (groupPosition==1 && childPosition == 2) {
                            bundle.putString("data", "https://gurucool.xyz/?link1=classroompro&id=1076");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Entrance study groups select");
                        } else if (groupPosition==1 && childPosition == 3) {
                            bundle.putString("data", "https://gurucool.xyz/?link1=studyhelppro&id=1076");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Entrance study materials select");
                        } else if (groupPosition==1 && childPosition == 4) {
                            bundle.putString("data", "https://gurucool.xyz/hashtag/upsc");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Entrance news and articles select");
                        } else if (groupPosition==3 && childPosition == 0) {
                            bundle.putString("data", "https://gurucool.xyz/courses");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Courses overall dev select");
                        } else if (groupPosition==3 && childPosition == 1) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=49&folder=106");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Courses Skilling select");
                        } else if (groupPosition==3 && childPosition == 2) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=49&folder=46");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Courses IT CSE select");
                        } else if (groupPosition==3 && childPosition == 3) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=49&folder=107");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
//                            Common.showToast(context, "Courses Psych and Mental select");
                        } else if (groupPosition==3 && childPosition == 4) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=49&folder=105");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "Courses Business and Managment select");
                        } else if (groupPosition==3 && childPosition == 5) {
                            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=49&folder=104");
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                            //Common.showToast(context, "Courses Lang and Lit select");
                        }
                        drawer.closeDrawer(GravityCompat.START);
                        return false;
                    }
                });

        listView.setSelected(0);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    void setRTL(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }


    final void setMySwipeRefreshLayout(){
        mySwipeRefreshLayout = findViewById(R.id.swipeContainer1);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    final public void onRefresh() {
                        mWebView.reload();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }


    final void setActionBarToogle(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

//    final void floatingActionButton(){
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            final public void onClick(View view) {
//                fragment = null;
//
//                mWebView.setVisibility(View.VISIBLE);
//                frameLayout.setVisibility(View.GONE);
//                mWebView.loadUrl("https://gurucool.xyz/messages/102");
//            }
//        });
//    }


    final void viewInit(){
        drawerLayout = findViewById(R.id.drawer_layout);
        mWebView = findViewById(R.id.mWebView);
        navigationView =  findViewById(R.id.nav_view);
        toolbar =  findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.content_frame);
        progressBar = findViewById(R.id.progressBar);
        nestedScrollView = findViewById(R.id.nested);
    }

    ProgressDialog pd;


    void webSettings(){
        pd = ProgressDialog.show(this, "", "Loading...",true);


        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDescription,
                                        String mimetype, long contentLength) {

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                String fileName = URLUtil.guessFileName(url,contentDescription,mimetype);

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);

                DownloadManager dManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                dManager.enqueue(request);


            }
        });

        mWebView.setWebViewClient(new WebViewClient()
        {


            public void onReceivedError(WebView mWebView, int i, String s, String d1)
            {
                mWebView.loadUrl("file:///android_asset/net_error.html");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                super.onPageFinished(view, url);
                boolean d = false;
                if(d==false){
                    nestedScrollView.scrollTo(0,0);
                    d=true;
                }

                if(pd!=null && pd.isShowing())
                {
                    pd.dismiss();
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //opening link external browser
                /*if(!url.contains("android_asset")){
                    view.setWebViewClient(null);
                } else {
                    view.setWebViewClient(new WebViewClient());
                }*/
                if (url.endsWith(".pdf")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    // if want to download pdf manually create AsyncTask here
                    // and download file
                    return true;
                }

                if(url.contains("youtube.com") || url.contains("play.google.com") || url.contains("google.com/maps") || url.contains("facebook.com") || url.contains("twitter.com") || url.contains("instagram.com")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
                else if(url.startsWith("mailto")){
                    handleMailToLink(url);
                    return true;
                }
                else if(url.startsWith("tel:")){
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                    return true;
                }
                else if(url.startsWith("sms:")){
                    // Handle the sms: link
                    handleSMSLink(url);

                    // Return true means, leave the current web view and handle the url itself
                    return true;
                }
                else if(url.contains("geo:")) {
                    Uri gmmIntentUri = Uri.parse(url);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                    return true;
                }

                view.loadUrl(url);
                return true;
            }

        });


        mWebView.setWebChromeClient(new WebChromeClient(){

            public Bitmap getDefaultVideoPoster()
            {
                if (mCustomView == null) {
                    return null;
                }
                return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
            }

            public void onHideCustomView()
            {
                ((FrameLayout)getWindow().getDecorView()).removeView(mCustomView);
                mCustomView = null;
                getWindow().getDecorView().setSystemUiVisibility(mOriginalSystemUiVisibility);
                setRequestedOrientation(mOriginalOrientation);
                mCustomViewCallback.onCustomViewHidden();
                mCustomViewCallback = null;
            }

            public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
            {
                if (mCustomView != null)
                {
                    onHideCustomView();
                    return;
                }
                mCustomView = paramView;
                mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                mOriginalOrientation = getRequestedOrientation();
                mCustomViewCallback = paramCustomViewCallback;
                ((FrameLayout)getWindow().getDecorView()).addView(mCustomView, new FrameLayout.LayoutParams(-1, -1));
                getWindow().getDecorView().setSystemUiVisibility(3846);
            }


            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            final protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
            {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try
                {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e)
                {
                    uploadMessage = null;
                    Toast.makeText(getApplicationContext() , "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    //eski---- >   Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
            {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }


            public void onProgressChanged(WebView view, int newProgress){
                progressBar.setProgress(newProgress);
                if(newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        WebSettings webSettings = mWebView.getSettings();

        webSettings.setDomStorageEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.getSaveFormData();
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(true);
        // webSettings.setSupportMultipleWindows(true); //?a href problem
        webSettings.getJavaScriptEnabled();
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        //  webSettings.setJavaScriptCanOpenWindowsAutomatically(false); //(popup)
    }


    protected void handleSMSLink(String url){
        /*
            If you want to ensure that your intent is handled only by a text messaging app (and not
            other email or social apps), then use the ACTION_SENDTO action
            and include the "smsto:" data scheme
        */

        // Initialize a new intent to send sms message
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        // Extract the phoneNumber from sms url
        String phoneNumber = url.split("[:?]")[1];

        if(!TextUtils.isEmpty(phoneNumber)){
            // Set intent data
            // This ensures only SMS apps respond
            intent.setData(Uri.parse("smsto:" + phoneNumber));

            // Alternate data scheme
            //intent.setData(Uri.parse("sms:" + phoneNumber));
        }else {
            // If the sms link built without phone number
            intent.setData(Uri.parse("smsto:"));

            // Alternate data scheme
            //intent.setData(Uri.parse("sms:" + phoneNumber));
        }


        // Extract the sms body from sms url
        if(url.contains("body=")){
            String smsBody = url.split("body=")[1];

            // Encode the sms body
            try{
                smsBody = URLDecoder.decode(smsBody,"UTF-8");
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

            if(!TextUtils.isEmpty(smsBody)){
                // Set intent body
                intent.putExtra("sms_body",smsBody);
            }
        }

        if(intent.resolveActivity(getPackageManager())!=null){
            // Start the sms app
            startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(),"No SMS app found.",Toast.LENGTH_SHORT).show();
        }
    }


    // Custom method to handle web view mailto link
    protected void handleMailToLink(String url){
        // Initialize a new intent which action is send
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        // For only email app handle this intent
        intent.setData(Uri.parse("mailto:"));

        String mString="";
        // Extract the email address from mailto url
        String to = url.split("[:?]")[1];
        if(!TextUtils.isEmpty(to)){
            String[] toArray = to.split(";");
            // Put the primary email addresses array into intent
            intent.putExtra(Intent.EXTRA_EMAIL,toArray);
            mString += ("TO : " + to);
        }

        // Extract the cc
        if(url.contains("cc=")){
            String cc = url.split("cc=")[1];
            if(!TextUtils.isEmpty(cc)){
                //cc = cc.split("&")[0];
                cc = cc.split("&")[0];
                String[] ccArray = cc.split(";");
                // Put the cc email addresses array into intent
                intent.putExtra(Intent.EXTRA_CC,ccArray);
                mString += ("\nCC : " + cc );
            }
        }else {
            mString += ("\n" + "No CC");
        }

        // Extract the bcc
        if(url.contains("bcc=")){
            String bcc = url.split("bcc=")[1];
            if(!TextUtils.isEmpty(bcc)){
                //cc = cc.split("&")[0];
                bcc = bcc.split("&")[0];
                String[] bccArray = bcc.split(";");
                // Put the bcc email addresses array into intent
                intent.putExtra(Intent.EXTRA_BCC,bccArray);
                mString += ("\nBCC : " + bcc );
            }
        }else {
            mString+=("\n" + "No BCC");
        }

        // Extract the subject
        if(url.contains("subject=")){
            String subject = url.split("subject=")[1];
            if(!TextUtils.isEmpty(subject)){
                subject = subject.split("&")[0];
                // Encode the subject
                try{
                    subject = URLDecoder.decode(subject,"UTF-8");
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                // Put the mail subject into intent
                intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                mString+=("\nSUBJECT : " + subject );
            }
        }else {
            mString+=("\n" + "No SUBJECT");
        }

        // Extract the body
        if(url.contains("body=")){
            String body = url.split("body=")[1];
            if(!TextUtils.isEmpty(body)){
                body = body.split("&")[0];
                // Encode the body text
                try{
                    body = URLDecoder.decode(body,"UTF-8");
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                // Put the mail body into intent
                intent.putExtra(Intent.EXTRA_TEXT,body);
                mString+=("\nBODY : " + body );
            }
        }else {
            mString+=("\n" + "No BODY");
        }

        // Email address not null or empty
        if(!TextUtils.isEmpty(to)){
            if(intent.resolveActivity(getPackageManager())!=null){
                // Finally, open the mail client activity
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(),"No email client found.",Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onBackPressed(){
        if(getSupportActionBar().getTitle().equals("Local Page")){
            setTitle("WebView");
            FrameLayout frameLayout = findViewById(R.id.content_frame);
            frameLayout.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            mWebView.loadUrl(url);
        }
        else if(mWebView.canGoBack())
            mWebView.goBack();
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit the application?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        final public void onClick(DialogInterface arg0, int arg1) {
                            MainActivity.super.onBackPressed();
                        }
                    }).create().show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_back) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }
            return true;

        }
        else if(id == R.id.action_refresh){
            mWebView.reload();
        }
        else if(id == R.id.action_share){
            share();
        }
        else if(id == R.id.action_copy){
            copyToPanel(getApplicationContext(),mWebView.getUrl());
        }
        return super.onOptionsItemSelected(item);
    }

    final public void copyToPanel(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied.", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    final void share(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, " Posted By ... : "+mWebView.getUrl());
        startActivity(Intent.createChooser(sharingIntent, "Share"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homepage_actions, menu);
        getMenuInflater().inflate(R.menu.main, optionsMenu);
        return super.onCreateOptionsMenu(menu);
    }




//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//
//        Intent intent = new Intent(MainActivity.this, MainActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt("key", 1);
//        int id = item.getItemId();
//
////        if (id == R.id.contact) {
//////            fragment = null;
//////
//////            mWebView.setVisibility(View.VISIBLE);
//////            frameLayout.setVisibility(View.GONE);
//////
//////            mWebView.loadUrl("https://gurucool.xyz/messages/102");
////            bundle.putString("data", "https://gurucool.xyz/messages/102");
////        } else if (id == R.id.home) {
//////            fragment = null;
//////
//////            mWebView.setVisibility(View.VISIBLE);
//////            frameLayout.setVisibility(View.GONE);
//////            mWebView.loadUrl(url);
////
////            bundle.putString("data", url);
////
////        } else if (id == R.id.blog) {
//////            fragment = null;
//////
//////            mWebView.setVisibility(View.VISIBLE);
//////            frameLayout.setVisibility(View.GONE);
////////            mWebView.loadUrl("https://gurucool.xyz/eeduhub#about");
////////            mWebView.loadUrl("https://gurucool.xyz/alnajahinstitute");
//////            mWebView.loadUrl("https://gurucool.xyz/blogs");
////            bundle.putString("data", "https://gurucool.xyz/blogs");
////
////        }  else if (id == R.id.bookmarket) {
//////            fragment = null;
//////
//////            mWebView.setVisibility(View.VISIBLE);
//////            frameLayout.setVisibility(View.GONE);
////////            mWebView.loadUrl("https://gurucool.xyz/eeduhub#about");
////////            mWebView.loadUrl("https://gurucool.xyz/alnajahinstitute");
//////            mWebView.loadUrl("https://gurucool.xyz/products");
////
////            bundle.putString("data", "https://gurucool.xyz/products");
////
////        }  else if (id == R.id.notice) {
//////            fragment = null;
//////
//////            mWebView.setVisibility(View.VISIBLE);
//////            frameLayout.setVisibility(View.GONE);
////////            mWebView.loadUrl("https://gurucool.xyz/eeduhub#about");
////////            mWebView.loadUrl("https://gurucool.xyz/alnajahinstitute");
//////            mWebView.loadUrl("https://www.jmi.ac.in/bulletinboard/NoticeOfficialorder/latest/1");
////
////            bundle.putString("data", "https://www.jmi.ac.in/bulletinboard/NoticeOfficialorder/latest/1");
////
////        }else if (id == R.id.classroom) {
//////            fragment = null;
//////
//////            mWebView.setVisibility(View.VISIBLE);
//////            frameLayout.setVisibility(View.GONE);
//////            mWebView.loadUrl("https://gurucool.xyz/index.php?link1=classroompro&id=102");
////
////            bundle.putString("data", "https://gurucool.xyz/index.php?link1=classroompro&id=102");
////
////        } else if (id == R.id.courses) {
//////            fragment = null;
//////
//////            mWebView.setVisibility(View.VISIBLE);
//////            frameLayout.setVisibility(View.GONE);
////////            mWebView.loadUrl("https://gurucool.xyz/index.php?link1=educatormode&id=110");
//////            mWebView.loadUrl("https://gurucool.xyz/index.php?link1=educatormode&id=102");
////
////            bundle.putString("data", "https://gurucool.xyz/index.php?link1=educatormode&id=102");
////
////        } else if (id == R.id.studyhelp) {
//////            fragment = null;
//////
//////            mWebView.setVisibility(View.VISIBLE);
//////            frameLayout.setVisibility(View.GONE);
////////            mWebView.loadUrl("https://gurucool.xyz/index.php?link1=studyhelpl");
////////            mWebView.loadUrl("https://gurucool.xyz/");
//////            mWebView.loadUrl("https://gurucool.xyz/index.php?link1=studyhelppro&id=102");
////
////            bundle.putString("data", "https://gurucool.xyz/index.php?link1=studyhelppro&id=102");
////
////        } else if (id == R.id.etest) {
//////            fragment = null;
//////
//////            mWebView.setVisibility(View.VISIBLE);
//////            frameLayout.setVisibility(View.GONE);
////////            mWebView.loadUrl("https://gurucool.xyz/index.php?link1=e_test");
//////            mWebView.loadUrl("https://gurucool.xyz/index.php?link1=testpro&id=102");
////
////            bundle.putString("data", "https://gurucool.xyz/index.php?link1=testpro&id=102");
//////            try {
//////                String s = "154";
//////
//////                String postData = "id=154";
//////
//////                mWebView.postUrl("https://gurucool.xyz/testpro", postData.getBytes());
//////            }catch (Exception e){
//////                Toast.makeText(this, "Something is wrong", Toast.LENGTH_SHORT).show();
//////            }
////
////        } else if (id == R.id.QnA) {
//////            fragment = null;
//////
//////            mWebView.setVisibility(View.VISIBLE);
//////            frameLayout.setVisibility(View.GONE);
////////            mWebView.loadUrl("https://gurucool.xyz/index.php?link1=discussion");
//////            mWebView.loadUrl("https://gurucool.xyz/index.php?link1=discussion");
////
////            bundle.putString("data", "https://gurucool.xyz/index.php?link1=discussion");
////
////        } else if (id == R.id.placement) {
//////            fragment = null;
//////
//////            mWebView.setVisibility(View.VISIBLE);
//////            frameLayout.setVisibility(View.GONE);
//////
////////            mWebView.loadUrl("https://gurucool.xyz/index.php?link1=classroom");
//////            mWebView.loadUrl("https://gurucool.xyz/jobs");
//////            Toast.makeText(this, "This feature is under development", Toast.LENGTH_SHORT).sho
//////
//////            w();
////
////            bundle.putString("data", "https://gurucool.xyz/jobs");
////
////
////        } else if (id == R.id.events) {
//////            fragment = null;
//////
//////            mWebView.setVisibility(View.VISIBLE);
//////            frameLayout.setVisibility(View.GONE);
//////            mWebView.loadUrl("https://gurucool.xyz/events/");
////            bundle.putString("data", "https://gurucool.xyz/events/");
//////            Toast.makeText(this, "This feature is under development", Toast.LENGTH_SHORT).show();
////        }
////        intent.putExtras(bundle);
////
////        finish();
////        startActivity(intent);
//
//
//        return true;
//    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_FILE) {
            if (uploadMessage == null) return;
            uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            uploadMessage = null;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_slideshow) {
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {
        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
