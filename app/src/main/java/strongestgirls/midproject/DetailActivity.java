package strongestgirls.midproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public class DetailActivity extends AppCompatActivity {

    private ImageView hero_img;
    private EditText name;
    private TextView hero_power;
    private TextView hero_sex;
    private TextView birth_year;
    private TextView death_year;
    private TextView hero_place;
    private TextView hero_event;
    private Hero theHero;
    private ScrollView editView;
    private ScrollView detailView;
    private RadioGroup radioGroup;
    private Spinner birth_spinner;
    private Spinner death_spinner;
    private Spinner place_spinner;
    private Spinner power_spinner;
    private EditText event;
    private Button confirm_button;
    private Button cancel_button;
    private Button delete_button;
    private Button edit_button;
    private ImageButton back_button;
    private int editrequest;
    private String origin_name;
    private String origin_sex;
    private String origin_power;
    private String origin_birth;
    private String origin_death;
    private String origin_event;
    private String origin_place;
    private String origin_letter;
    private int origin_img;

    private final int PHOTO_REQUEST = 1;

    String[] hero_yearItem = new String[]{"不详", "公元184年", "公元185年", "公元186年", "公元187年", "公元188年", "公元189年", "公元190年",
            "公元191年", "公元192年", "公元193年", "公元194年", "公元195年", "公元196年", "公元197年", "公元198年", "公元199年", "公元200年",
            "公元201年", "公元202年", "公元203年", "公元204年", "公元205年", "公元206年", "公元207年", "公元208年", "公元209年", "公元210年",
            "公元211年", "公元212年", "公元213年", "公元214年", "公元215年", "公元216年", "公元217年", "公元218年", "公元219年", "公元220年",
            "公元221年", "公元222年", "公元223年", "公元224年", "公元225年", "公元226年", "公元227年", "公元228年", "公元229年", "公元230年",
            "公元231年", "公元232年", "公元233年", "公元234年", "公元235年", "公元236年", "公元237年", "公元238年", "公元239年", "公元240年",
            "公元241年", "公元242年", "公元243年", "公元244年", "公元245年", "公元246年", "公元247年", "公元248年", "公元249年", "公元250年",
            "公元251年", "公元252年", "公元253年", "公元254年", "公元255年", "公元256年", "公元257年", "公元258年", "公元259年", "公元260年",
            "公元261年", "公元262年", "公元263年", "公元264年", "公元265年", "公元266年", "公元267年", "公元268年", "公元269年", "公元270年",
            "公元271年", "公元272年", "公元273年", "公元274年", "公元275年", "公元276年", "公元277年", "公元278年", "公元279年", "公元280年"};
    String[] hero_placeItem = new String[]{"并州", "冀州", "交州", "荆州", "凉州", "青州", "司隶",
            "徐州", "兖州", "扬州", "益州", "幽州", "豫州","不详"};
    String[] hero_powerItem = new String[]{"东汉", "魏", "蜀", "吴", "袁绍", "袁术", "刘表",
            "起义军", "董卓", "刘璋", "西晋", "少数民族", "在野", "其他","不详"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        editView = (ScrollView)findViewById(R.id.editView);
        detailView = (ScrollView)findViewById(R.id.detailView);

        //详情显示界面
        hero_img = (ImageView)findViewById(R.id.hero_img);   //头像
        name = (EditText)findViewById(R.id.hero_name);       //名字
        hero_power = (TextView)findViewById(R.id.hero_power);   //势力
        hero_sex = (TextView)findViewById(R.id.hero_sex);       //性别
        hero_place = (TextView)findViewById(R.id.hero_place);   //籍贯
        hero_event = (TextView)findViewById(R.id.hero_event);   //事迹
        birth_year = (TextView)findViewById(R.id.birth_year);   //生年
        death_year = (TextView)findViewById(R.id.death_year);   //死年
        delete_button = (Button)findViewById(R.id.delete);
        edit_button = (Button)findViewById(R.id.edit);
        back_button = (ImageButton) findViewById(R.id.back);

        //新建界面
        radioGroup = (RadioGroup)findViewById(R.id.sexSelect);
        birth_spinner = (Spinner)findViewById(R.id.birth_spinner);
        death_spinner = (Spinner)findViewById(R.id.death_spinner);
        place_spinner = (Spinner)findViewById(R.id.place_spinner);
        power_spinner = (Spinner)findViewById(R.id.power_spinner);
        event = (EditText)findViewById(R.id.edit_event);
        confirm_button = (Button) findViewById(R.id.confirm);
        cancel_button = (Button) findViewById(R.id.cancel);
        editrequest = 0;  //当前没有编辑请求

        //intent接收从主界面传送的数据
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        if (bundle != null) {
            int edit_state = bundle.getInt("edit");
            int detail_state = bundle.getInt("detail");
            //新建界面
            if (edit_state == 1 && detail_state == 0) {
                detailView.setVisibility(View.GONE);
                editView.setVisibility(View.VISIBLE);
                //人物头像和姓名、势力都要设置为默认值
                name.setFocusableInTouchMode(true);
                name.setFocusable(true);
                hero_power.setText("");
                name.setText("请输入姓名");
            }
            //详情界面
            if (detail_state == 1 && edit_state == 0){
                detailView.setVisibility(View.VISIBLE);
                editView.setVisibility(View.GONE);
                theHero = (Hero) it.getSerializableExtra("Hero");
                //保留原人物信息
                origin_letter = theHero.getFirstLetter();
                origin_name = theHero.getName();
                origin_sex = theHero.getSex();
                origin_birth = theHero.getBirth_year();
                origin_death = theHero.getDeath_year();
                origin_event = theHero.getEvent();
                origin_power = theHero.getPower();
                origin_place = theHero.getPlace();
                origin_img = theHero.getProfile();
                //设置人物信息显示
                name.setText(theHero.getName());
                hero_power.setText(theHero.getPower());
                hero_sex.setText(theHero.getSex());
                birth_year.setText(theHero.getBirth_year());
                death_year.setText(theHero.getDeath_year());
                hero_place.setText(theHero.getPlace());
                hero_event.setText(theHero.getEvent());
                hero_img.setImageResource(theHero.getProfile());
            }
        }

        //点击事件调用
        profileClick();
        birthClick();
        deathClick();
        placeClick();
        powerClick();
        buttonClick();
    }

    //头像点击事件
    public void profileClick() {
        //点击头像
        hero_img.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PHOTO_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                hero_img.setImageURI(data.getData());
            }
        }
    }

    //出生点击事件
    public void birthClick() {
        ArrayAdapter<String> birthAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, hero_yearItem);
        birthAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        birth_spinner.setAdapter(birthAdapter);
    }

    //死亡点击事件
    public void deathClick() {
        ArrayAdapter<String> deathAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, hero_yearItem);
        deathAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        death_spinner.setAdapter(deathAdapter);
    }

    //籍贯点击事件
    public void placeClick() {
        ArrayAdapter<String> placeAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, hero_placeItem);
        placeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        place_spinner.setAdapter(placeAdapter);

    }

    //势力点击事件
    public void powerClick() {
        ArrayAdapter<String> powerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, hero_powerItem);
        powerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        power_spinner.setAdapter(powerAdapter);
    }


    //待实现的功能
    //确定按钮的图像传递

    public void buttonClick() {
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editrequest == 0) {
                    String NAME = name.getText().toString();
                    String EVENT = event.getText().toString();
                    if (TextUtils.isEmpty(NAME)) {
                        String message = "请输入姓名♪(･ω･)ﾉ";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(EVENT)) {
                        String message = "请输入事迹♪(･ω･)ﾉ";
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String letter = NAME.substring(0, 1).toUpperCase();
                        String sex = "";
                        for (int i = 0; i < radioGroup.getChildCount(); i++) {
                            RadioButton rb = (RadioButton)radioGroup.getChildAt(i);
                            if (rb.isChecked()) {
                                sex = rb.getText().toString();
                                break;
                            }
                        }
                        String place = place_spinner.getSelectedItem().toString();
                        String birth_year = birth_spinner.getSelectedItem().toString();
                        String death_year = death_spinner.getSelectedItem().toString();
                        String power = power_spinner.getSelectedItem().toString();
                        Hero hero = new Hero(letter, NAME, sex, place, birth_year, death_year, power, EVENT, hero_img.getId());


                        //跳转到主界面
                        //头像问题，会崩
                        EventBus.getDefault().post(new MessageEvent(1,NAME,hero));
                        Intent it = new Intent();
                        it.setClass(DetailActivity.this, MainActivity.class);
                        startActivity(it);
                    }
                } else if (editrequest == 1) {
                    //保存修改的数据，传到主界面
                    Hero hero = new Hero(origin_letter, origin_name, origin_sex, origin_place, origin_birth, origin_death, origin_power, origin_event, origin_img);
                    String NAME = name.getText().toString();
                    String EVENT = event.getText().toString();
                    if (!NAME.equals(origin_name)) {
                        hero.setName(NAME);
                        String letter = NAME.substring(0, 1).toUpperCase();
                        hero.setFirstLetter(letter);
                    }
                    if (!EVENT.equals(origin_event)) {
                        hero.setEvent(EVENT);
                    }
                    String sex = "";
                    for (int i = 0; i < radioGroup.getChildCount(); i++) {
                        RadioButton rb = (RadioButton)radioGroup.getChildAt(i);
                        if (rb.isChecked()) {
                            sex = rb.getText().toString();
                            break;
                        }
                    }
                    String place = place_spinner.getSelectedItem().toString();
                    String birth_year = birth_spinner.getSelectedItem().toString();
                    String death_year = death_spinner.getSelectedItem().toString();
                    String power = power_spinner.getSelectedItem().toString();
                    if (!sex.equals(origin_sex)) {hero.setSex(sex);}
                    if (!place.equals(origin_place)){hero.setPlace(place);}
                    if (!birth_year.equals(origin_birth)){hero.setBirth_year(birth_year);}
                    if (!death_year.equals(origin_death)) {hero.setDeath_year(death_year);}
                    if (!power.equals(origin_power)){hero.setPower(power);}
                    //hero.setProfile(profile.getId());
                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), profile.getId());
                    //跳转到主界面
                    editrequest = 0;
                    Toast.makeText(getApplicationContext(), "已保存信息", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new MessageEvent(3,origin_name,hero));
                    Intent it = new Intent();
                    it.setClass(DetailActivity.this, MainActivity.class);
                    startActivity(it);
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //删除按钮点击事件
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(DetailActivity.this);
                alertDialog1.setTitle("删除此项").setMessage("确认删除这一人物数据？");
                alertDialog1.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"您选择了[确定]",Toast.LENGTH_SHORT).show();
                        //跳转回主界面并从主界面删除
                        Hero hero = new Hero("","","","","","","","",0);
                        EventBus.getDefault().post(new MessageEvent(2,name.getText().toString(),hero));
                        Intent it = new Intent();
                        it.setClass(DetailActivity.this, MainActivity.class);
                        startActivity(it);
                    }
                });
                alertDialog1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"您选择了[取消]",Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog1.create().show();
            }
        });
        //编辑按钮点击事件
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailView.setVisibility(View.GONE);
                editView.setVisibility(View.VISIBLE);
                name.setFocusable(true);
                name.setFocusableInTouchMode(true);
                editrequest = 1;
                //选项设置为人物信息值
                event.setText(origin_event);
                for (int i = 0; i < radioGroup.getChildCount(); i++) {
                    RadioButton rb = (RadioButton)radioGroup.getChildAt(i);
                    if (rb.getText().toString().equals(origin_sex)) {
                        rb.setChecked(true);
                        break;
                    }
                }
                //年份跨度不够大
                setSpinnerSelected(birth_spinner,"公元"+origin_birth+"年");
                setSpinnerSelected(death_spinner,"公元"+origin_death+"年");

                setSpinnerSelected(power_spinner,origin_power);
                setSpinnerSelected(place_spinner,origin_place);
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(DetailActivity.this, MainActivity.class);
                startActivity(it);
            }
        });
    }

    public void setSpinnerSelected(Spinner spinner,String value){
        SpinnerAdapter sa = spinner.getAdapter();
        for (int i = 0;i<sa.getCount();i++){
            if (value.equals(sa.getItem(i).toString())){
                spinner.setSelection(i);
                break;
            }
        }
    }
}


