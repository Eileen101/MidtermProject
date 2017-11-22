package strongestgirls.midproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String[] firstLetterItem = new String[] {"不限","A","B","C","D","E","F","G","H","I","J","K","L","M",
            "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    String[] hero_powerItem = new String[] {"不限","东汉","魏","蜀","吴","袁绍","袁术","刘表","起义军","董卓",
            "刘璋","西晋","少数民族","在野","其他"};
    String[] hero_placeItem = new String[] {"不限","并州","冀州","交州","荆州","凉州","青州","司隶",
            "徐州","兖州","扬州","益州","幽州","豫州"};
    String[] hero_yearItem = new String[] {"不限","公元184年","公元185年","公元186年","公元187年","公元188年","公元189年","公元190年",
            "公元191年","公元182年","公元193年", "公元194年","公元195年","公元196年","公元197年","公元198年","公元199年","公元200年",
            "公元201年","公元202年","公元203年", "公元204年","公元205年","公元206年","公元207年","公元208年","公元209年","公元210年",
            "公元211年","公元212年","公元213年", "公元214年","公元215年","公元216年","公元217年","公元218年","公元219年","公元220年",
            "公元221年","公元222年","公元223年", "公元224年","公元225年","公元226年","公元227年","公元228年","公元229年","公元230年",
            "公元231年","公元232年","公元233年", "公元234年","公元235年","公元236年","公元237年","公元238年","公元239年","公元240年",
            "公元241年","公元242年","公元243年", "公元244年","公元245年","公元246年","公元247年","公元248年","公元249年","公元250年",
            "公元251年","公元252年","公元253年", "公元254年","公元255年","公元256年","公元257年","公元258年","公元259年","公元260年",
            "公元261年","公元262年","公元263年", "公元264年","公元265年","公元266年","公元267年","公元268年","公元269年","公元270年",
            "公元271年","公元272年","公元273年", "公元274年","公元275年","公元276年","公元277年","公元278年","公元279年","公元280年"};
    String[] hero_sexItem = new String[] {"不限","男","女"};
    String[] hero_name = new String[] {"曹操","曹丕","貂蝉","董卓","关羽","刘备","孙权","小乔","周瑜","诸葛亮"};
    private GridView hero_view;
    private Toolbar tb;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private List<Hero> heros = new ArrayList<>();
    private List<Hero> herosTarget = new ArrayList<>();
    private Spinner firstLetterSpinner, hero_powerItemSpinner, hero_placeItemSpinner, hero_yearItemSpinner, hero_sexItemSpinner;
    private ListView listView;
    private EditText searchFrame;
    private Button searchButton, resetButton;
    private ArrayAdapter firstLetterAdapter, hero_powerItemAdapter, hero_placeItemAdapter, hero_yearItemAdapter, hero_sexItemAdapter, listViewAdapter;
    private List<String> firstLetterList, hero_powerItemList, hero_placeItemList, hero_yearItemList, hero_sexItemList;
    private String firstLetterTarget, hero_powerTarget, hero_placeTarget, hero_yearTarget, hero_sexTarget, hero_nameTarget;
    private HeroAdapter herosAdapter;
    private HeroAdapter herosTargetAdapter;
    private int search_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        initDrawerLayout();
        initActionBar();
        initData();
        initViews();
        initSpinner();
        initSearchButton();
        initSearchFrame();
        initResetButton();

        search_flag = 0;


        herosAdapter = new HeroAdapter(getLayoutInflater(), heros);
        herosTargetAdapter = new HeroAdapter(getLayoutInflater(), herosTarget);
        hero_view.setAdapter(herosAdapter);
        hero_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                hero_view.setPadding(0, 50, 0, 50);//长按后，让hero_view上下都分出点空间

                //有错误，还没有改！！！
                String name = " ";
                //String name = heros.get(position).getKey("text");

                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("您要删除"+name+"?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //herosAdapter.remove(position);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
                builder.setCancelable(true);
                return true;
            }
        });
        hero_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent();
                it.setClass(MainActivity.this, DetailActivity.class);
                //不同模式下显示的内容分别从主表和搜索表选取
                Hero theHero = null;
                if (search_flag == 0) {
                    theHero = (Hero) herosAdapter.getItem(position);
                } else if (search_flag == 1){
                    theHero = (Hero) herosTargetAdapter.getItem(position);
                }
                Bundle bundle = new Bundle();
                bundle.putInt("edit",0);
                bundle.putInt("detail",1);
                bundle.putSerializable("Hero", theHero);
                it.putExtras(bundle);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it);
            }
        });

        //注册eventbus
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        int op_tag;    // add-1  , delete-2 , edit-3
        String name;
        Hero hero;
        op_tag = event.op_tag;
        name = event.name;
        if (op_tag == 1) {
            hero = event.hero;
            //有问题
            int pic = hero.getProfile();
            hero.setProfile(R.mipmap.cao_cao);
            heros.add(hero);
            herosAdapter.notifyDataSetChanged();
        }
        if (op_tag == 2) {
            int pos = -1;
            name = event.name;
            for (int i = 0;i < heros.size();i++) {
                if (!heros.get(i).getName().isEmpty() && name.equals(heros.get(i).getName())) {
                    pos = i;
                }
            }
            if (pos != -1) {
                heros.remove(pos);
                herosAdapter.notifyDataSetChanged();
            }
        }
        if (op_tag == 3) {
            int pos = -1;
            name = event.name;
            for (int i = 0;i < heros.size();i++) {
                if (!heros.get(i).getName().isEmpty() && name.equals(heros.get(i).getName())) {
                    pos = i;
                }
            }
            if (pos != -1) {
                heros.remove(pos);
                hero = event.hero;
                heros.add(hero);
                herosAdapter.notifyDataSetChanged();
            }
        }
        hero_view.setAdapter(herosAdapter);
        search_flag = 0;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //加载toolbar的两个按钮
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //toolbar按钮响应事件
        switch (item.getItemId()) {
            case R.id.home: //呼出侧边栏
                actionBarDrawerToggle.onOptionsItemSelected(item);
                break;
            case R.id.add: // 跳转新建界面
                Intent it = new Intent();
                it.setClass(MainActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("edit",1);
                bundle.putInt("detail",0);
                it.putExtras(bundle);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
     * 首字母索引这个实现起来有点复杂，合并之后我来弄
     *
     * @param sortKeyString
     *            数据库中读取出的sort key
     * @return 英文字母或者#
     * @author 周林
     */
    private String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }

    private void initDrawerLayout() {
        final DrawerLayout drawLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawLayout.setScrimColor(Color.TRANSPARENT);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawLayout,
                tb, R.string.drawerOpen, R.string.drawerClose) {
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // 重新初始化heroTarget
                // herosTarget.addAll(heros);
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        actionBarDrawerToggle.syncState();
        drawLayout.setDrawerListener(actionBarDrawerToggle);

        // 弹出左边栏后，使下层布局不能接收响应
        drawLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
            }
            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("AppStore");
        actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键
        actionBar.setHomeAsUpIndicator(R.mipmap.zoom);
    }

    private void initData() {
        // Search View
        // put String into List
        firstLetterList = new ArrayList<>();
        for (int i = 0; i < firstLetterItem.length; i++) {
            firstLetterList.add(firstLetterItem[i]);
        }
        hero_powerItemList = new ArrayList<>();
        for (int i = 0; i < hero_powerItem.length; i++) {
            hero_powerItemList.add(hero_powerItem[i]);
        }
        hero_placeItemList = new ArrayList<>();
        for (int i = 0; i < hero_placeItem.length; i++) {
            hero_placeItemList.add(hero_placeItem[i]);
        }
        hero_yearItemList = new ArrayList<>();
        for (int i = 0; i < hero_yearItem.length; i++) {
            hero_yearItemList.add(hero_yearItem[i]);
        }
        hero_sexItemList = new ArrayList<>();
        for (int i = 0; i < hero_sexItem.length; i++) {
            hero_sexItemList.add(hero_sexItem[i]);
        }

        //heros_events
        String cao_cao_e = "曹操是西园八校尉之一，曾只身行刺董卓，失败后和袁绍共同联合天下诸侯讨伐董卓，后独自发展自身势力，一生中先后战胜了袁术、吕布、张绣、袁绍、刘表、张鲁、马超等割据势力，统一了北方。但是在南下讨伐江东的战役中，曹操在赤壁惨败。后来在和蜀汉的汉中争夺战中，曹操再次无功而返。曹操一生未称帝，他病死后，曹丕继位后不久称帝，追封曹操为魏武皇帝。";
        String cao_pi_e = "在争夺继承权问题上处心积虑，战胜了文才更胜一筹的弟弟曹植，被立为王世子。曹操逝世后，曹丕继位成为魏王，以不参加葬礼之罪逼弟弟曹植写下七步诗，险些将其杀害，又顺利夺下弟弟曹彰的兵权，坐稳了魏王之位。不久，曹丕逼汉献帝让位，代汉称帝，为魏国开国皇帝。刘备伐吴时，曹丕看出刘备要失败，但不听谋士之言，偏要坐山观虎斗，事后又起兵伐吴，结果被徐盛火攻击败。回洛阳后，曹丕大病，临终前托付曹睿给曹真、司马懿等人，终年四十岁。";
        String diao_chan_e = "舍身报国的可敬女子，她为了挽救天下黎民，为了推翻权臣董卓的荒淫统治，受王允所托，上演了可歌可泣的连环计（连环美人计），周旋于两个男人之间，成功的离间了董卓和吕布，最终吕布将董卓杀死，结束了董卓专权的黑暗时期。";
        String dong_zhuo_e = "原为汉河东太守，征讨黄巾不利，却因贿赂十常侍官至西凉刺史。董卓应大将军何进之邀进京铲除宦官，恰逢何进被杀，于是收编何进部曲，又赠赤兔马给吕布，诱其投降并杀死荆州刺史丁原，从此不可一世。董卓废汉少帝，立汉献帝，朝臣多敢怒不敢言；董卓生性残暴，经常纵兵略民，百姓皆恨。后来，司徒王允欲除掉董卓，将美女貂禅许给吕布，又献予董卓，使二人反目，遂与吕布合谋杀死了董卓。";
        String guan_yu_e = "因本处势豪倚势凌人，关羽杀之而逃难江湖。闻涿县招军破贼，特来应募。与刘备、张飞桃园结义，羽居其次。使八十二斤青龙偃月刀随刘备东征西讨。虎牢关温酒斩华雄，屯土山降汉不降曹。为报恩斩颜良、诛文丑，解曹操白马之围。后得知刘备音信，过五关斩六将，千里寻兄。刘备平定益州后，封关羽为五虎大将之首，督荆州事。羽起军攻曹，放水淹七军，威震华夏。围樊城右臂中箭，幸得华佗医治，刮骨疗伤。但未曾提防东吴袭荆州，关羽父子败走麦城，突围中被捕，不屈遭害。\n";
        String liu_bei_e = "刘备，蜀汉的开国皇帝，汉景帝之子中山靖王刘胜的后代。刘备少年孤贫，以贩鞋织草席为生。黄巾起义时，刘备与关羽、张飞桃园结义，成为异姓兄弟，一同剿除黄巾，有功，任安喜县尉，不久辞官；董卓乱政之际，刘备随公孙瓒讨伐董卓，三人在虎牢关战败吕布。后诸侯割据，刘备势力弱小，经常寄人篱下，先后投靠过公孙瓒、曹操、袁绍、刘表等人，几经波折，却仍无自己的地盘。赤壁之战前夕，刘备在荆州三顾茅庐，请诸葛亮出山辅助，在赤壁之战中，联合孙权打败曹操，奠定了三分天下的基础。刘备在诸葛亮的帮助下占领荆州，不久又进兵益州，夺取汉中，建立了横跨荆益两州的政权。后关羽战死，荆州被孙权夺取，刘备大怒，于称帝后伐吴，在夷陵之战中为陆逊用火攻打得大败，不久病逝于白帝城，临终托孤于诸葛亮。";
        String sun_quan_e = "孙权19岁就继承了其兄孙策之位，力据江东，击败了黄祖。后东吴联合刘备，在赤壁大战击溃了曹操军。东吴后来又和曹操军在合肥附近鏖战，并从刘备手中夺回荆州、杀死关羽、大破刘备的讨伐军。曹丕称帝后孙权先向北方称臣，后自己建吴称帝，迁都建业。";
        String xiao_qiao_e = "庐江皖县桥国老次女，秀美绝伦，貌压群芳，又琴棋书画无所不通周瑜攻取皖城，迎娶小乔为妻。周郎小乔英雄美女、郎才女貌 ，被流传为千古佳话。";
        String zhou_yu_e = "偏将军、南郡太守。自幼与孙策交好，策离袁术讨江东，瑜引兵从之。为中郎将，孙策相待甚厚，又同娶二乔。策临终，嘱弟权曰：“外事不决，可问周瑜”。瑜奔丧还吴，与张昭共佐权，并荐鲁肃等，掌军政大事。赤壁战前，瑜自鄱阳归。力主战曹，后于群英会戏蒋干、怒打黄盖行诈降计、后火烧曹军，大败之。后下南郡与曹仁相持，中箭负伤，与诸葛亮较智斗，定假涂灭虢等计，皆为亮破，后气死于巴陵，年三十六岁。临终，上书荐鲁肃代其位，权为其素服吊丧。";
        String zhu_ge_liang_e = "人称卧龙先生，有经天纬地之才，鬼神不测之机。刘皇叔三顾茅庐，遂允出山相助。曾舌战群儒、借东风、智算华容、三气周瑜，辅佐刘备于赤壁之战大败曹操，更取得荆州为基本。后奉命率军入川，于定军山智激老黄忠，斩杀夏侯渊，败走曹操，夺取汉中。刘备伐吴失败，受遗诏托孤，安居平五路，七纵平蛮，六出祁山，鞠躬尽瘁，死而后已。其手摇羽扇，运筹帷幄的潇洒形象，千百年来已成为人们心中“智慧”的代名词。";

        // init heros
        // {"曹操","曹丕","貂蝉","董卓","关羽","刘备","孙权","小乔","周瑜","诸葛亮"};
        heros.add(new Hero("C","曹操","男","豫州","155","220","魏",cao_cao_e,R.mipmap.cao_cao));
        heros.add(new Hero("C","曹丕","男","豫州","187","226","魏",cao_pi_e,R.mipmap.cao_pi));
        heros.add(new Hero("D","貂蝉","女","不详","不详","不详","不详",diao_chan_e,R.mipmap.diao_chan));
        heros.add(new Hero("D","董卓","男","凉州","不详","192","董卓",dong_zhuo_e,R.mipmap.dong_zhuo));
        heros.add(new Hero("G","关羽","男","司隶","不详","219","蜀",guan_yu_e,R.mipmap.guan_yu));
        heros.add(new Hero("L","刘备","男","幽州","161","223","蜀",liu_bei_e,R.mipmap.liu_bei));
        heros.add(new Hero("S","孙权","男","扬州","182","252","吴",sun_quan_e,R.mipmap.sun_quan));
        heros.add(new Hero("X","小乔","女","扬州","不详","不详","吴",xiao_qiao_e,R.mipmap.xiao_qiao));
        heros.add(new Hero("Z","周瑜","男","扬州","175","210","吴",zhou_yu_e,R.mipmap.zhou_yu));
        heros.add(new Hero("Z","诸葛亮","男","徐州","181","224","蜀",zhu_ge_liang_e,R.mipmap.zhu_ge_liang));
    }

    private void initViews() {
        // get spinners' id
        firstLetterSpinner = (Spinner) findViewById(R.id.firstLetter);
        hero_powerItemSpinner = (Spinner) findViewById(R.id.hero_power);
        hero_placeItemSpinner = (Spinner) findViewById(R.id.hero_place);
        hero_yearItemSpinner = (Spinner) findViewById(R.id.hero_year);
        hero_sexItemSpinner = (Spinner) findViewById(R.id.hero_sex);

        // get other views' by id
        listView = (ListView) findViewById(R.id.listView);
        searchFrame = (EditText) findViewById(R.id.searchFrame);
        searchButton = (Button) findViewById(R.id.searchButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        hero_view = (GridView) findViewById(R.id.hero_view);

        // init font

    }

    private void initSpinner() {
        // init spinner's adapter
        firstLetterAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, firstLetterItem);
        firstLetterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstLetterSpinner.setAdapter(firstLetterAdapter);

        hero_powerItemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, hero_powerItemList);
        hero_powerItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hero_powerItemSpinner.setAdapter(hero_powerItemAdapter);

        hero_placeItemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, hero_placeItemList);
        hero_placeItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hero_placeItemSpinner.setAdapter(hero_placeItemAdapter);

        hero_yearItemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, hero_yearItemList);
        hero_yearItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hero_yearItemSpinner.setAdapter(hero_yearItemAdapter);

        hero_sexItemAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, hero_sexItemList);
        hero_sexItemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hero_sexItemSpinner.setAdapter(hero_sexItemAdapter);
    }

    private void initSearchFrame() {
        // 初始化搜索框
        listViewAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, hero_name);
        listView.setAdapter(listViewAdapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                searchFrame.setText(hero_name[i]);
            }
        });
        searchFrame.addTextChangedListener(new TextWatcher() {
            @Override
            // 文本框发生改变前
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            // 文本框发生改变时
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            // 文本框发生改变后
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    listView.setFilterText(editable.toString());
                } else {
                    listView.clearTextFilter();
                }
            }
        });
    }

    private void initSearchButton() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置ssearch_flag为1,进入搜索结果显示
                search_flag = 1;

                // init target heros every searching
                // 初始状态下所有heros都是target，通过属性进行不断筛选，删除herosTarget里面的heros
                herosTarget.clear();
                herosTarget.addAll(heros);

                // 获取最终确定的搜索条件
                firstLetterTarget =  firstLetterSpinner.getSelectedItem().toString();
                hero_powerTarget = hero_powerItemSpinner.getSelectedItem().toString();
                hero_placeTarget = hero_placeItemSpinner.getSelectedItem().toString();
                hero_yearTarget = hero_yearItemSpinner.getSelectedItem().toString();
                hero_sexTarget = hero_sexItemSpinner.getSelectedItem().toString();
                hero_nameTarget = searchFrame.getText().toString();

                // 点击Search按钮后，左边栏缩回
                final DrawerLayout drawLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawLayout.closeDrawer(Gravity.LEFT);
                Toast.makeText(getApplicationContext(),"进行搜索",Toast.LENGTH_SHORT).show();

                if (!firstLetterTarget.equals("不限")) {
                    for (int i = 0; i < herosTarget.size(); i++) {
                        if (!firstLetterTarget.equals(herosTarget.get(i).getFirstLetter())) {
                            herosTarget.remove(i);
                            i--;
                        }
                    }
                }
                if (!hero_powerTarget.equals("不限")) {
                    for (int i = 0; i < herosTarget.size(); i++) {
                        if (!hero_powerTarget.equals(herosTarget.get(i).getPower())) {
                            herosTarget.remove(i);
                            i--;
                        }
                    }
                }
                if (!hero_placeTarget.equals("不限")) {
                    for (int i = 0; i < herosTarget.size(); i++) {
                        if (!hero_placeTarget.equals(herosTarget.get(i).getPlace())) {
                            herosTarget.remove(i);
                            i--;
                        }
                    }
                }
                if (!hero_yearTarget.equals("不限")) {
                    for (int i = 0; i < herosTarget.size(); i++) {
                        int yearTarget = Integer.parseInt(hero_yearTarget.substring(2,5));
                        // 仅死亡年份不详
                        if (!herosTarget.get(i).getBirth_year().equals("不详")&& herosTarget.get(i).getDeath_year().equals("不详")) {
                            int birthYear = Integer.parseInt(herosTarget.get(i).getBirth_year());
                            if (yearTarget < birthYear) {
                                herosTarget.remove(i);
                                i--;
                            }
                        }
                        // 仅出生年份不详
                        if (herosTarget.get(i).getBirth_year().equals("不详")&& !herosTarget.get(i).getDeath_year().equals("不详")) {
                            int deathYear = Integer.parseInt(herosTarget.get(i).getDeath_year());
                            if (yearTarget > deathYear) {
                                herosTarget.remove(i);
                                i--;
                            }
                        }
                        // 出生和死亡年份都不详
                        if (herosTarget.get(i).getBirth_year().equals("不详") && herosTarget.get(i).getDeath_year().equals("不详")) {

                        }
                        // 出生和死亡年份都详细
                        if (!(herosTarget.get(i).getBirth_year().equals("不详") || herosTarget.get(i).getDeath_year().equals("不详"))) {
                            int birthYear = Integer.parseInt(herosTarget.get(i).getBirth_year());
                            int deathYear = Integer.parseInt(herosTarget.get(i).getDeath_year());
                            if (yearTarget > deathYear || yearTarget < birthYear) {
                                herosTarget.remove(i);
                                i--;
                            }
                        }
                    }
                }
                if (!hero_sexTarget.equals("不限")) {
                    for (int i = 0; i < herosTarget.size(); i++) {
                        if (!hero_sexTarget.equals(herosTarget.get(i).getSex())) {
                            herosTarget.remove(i);
                            i--;
                        }
                    }
                }
                if (!hero_nameTarget.equals("")) {
                    for (int i = 0; i < herosTarget.size(); i++) {
                        if (!hero_nameTarget.equals(herosTarget.get(i).getName())) {
                            herosTarget.remove(i);
                            i--;
                        }
                    }
                }
                herosTargetAdapter.notifyDataSetChanged();
                hero_view.setAdapter(herosTargetAdapter);

                if (herosTarget.size() == 0) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("查询结果")
                            .setMessage("很遗憾，未找到您搜索的内容，请换个条件再试试n(*≧▽≦*)n")
                            .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // 点击关闭后弹出左对话框
                                    drawLayout.openDrawer(Gravity.LEFT);
                                }
                            })
                            .show();
                }
            }
        });
    }

    private void initResetButton() {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // init target heros every searching
                // 初始状态下所有heros都是target，通过属性进行不断筛选，删除herosTarget里面的heros
                herosTarget.clear();
                herosTarget.addAll(heros);

                // 重置搜索条件
                firstLetterSpinner.setSelection(0);
                hero_powerItemSpinner.setSelection(0);
                hero_placeItemSpinner.setSelection(0);
                hero_yearItemSpinner.setSelection(0);
                hero_sexItemSpinner.setSelection(0);
                searchFrame.setText("");
//                firstLetterTarget =  firstLetterSpinner.getSelectedItem().toString();
//                hero_powerTarget = hero_powerItemSpinner.getSelectedItem().toString();
//                hero_placeTarget = hero_placeItemSpinner.getSelectedItem().toString();
//                hero_yearTarget = hero_yearItemSpinner.getSelectedItem().toString();
//                hero_sexTarget = hero_sexItemSpinner.getSelectedItem().toString();
//                hero_nameTarget = searchFrame.getText().toString();
            }
        });
    }
}
