package strongestgirls.midproject;

/**
 * Created by Eileen on 2017/11/21.
 */

public class MessageEvent {
    int op_tag;    // add-1  , delete-2 , edit-3
    String name;   //作为搜索tag
    Hero hero;     //附加信息
    public MessageEvent(int op_tag,String name,Hero hero){
        this.op_tag = op_tag;
        this.name = name;
        this.hero = hero;
    }
}
