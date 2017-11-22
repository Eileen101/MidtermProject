package strongestgirls.midproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zhoulin on 2017/11/18.
 */

public class HeroAdapter extends BaseAdapter {
    private List<Hero> heros;
    private LayoutInflater mInflater;

    public HeroAdapter(LayoutInflater inflater, List<Hero> data){
        mInflater = inflater;
        heros = data;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        View theHero = mInflater.inflate(R.layout.view_hero_in_grid, null);
        ImageView heroPhoto = (ImageView) theHero.findViewById(R.id.hero_photo);
        TextView heroName = (TextView) theHero.findViewById(R.id.hero_name);
        Hero hero = heros.get(position);

        heroPhoto.setImageResource(hero.getProfile());
        heroName.setText(hero.getName());
        return theHero;
    }

    @Override
    public int getCount() { return heros.size(); }

    @Override
    public Object getItem(int position) { return heros.get(position); }

    @Override
    public long getItemId(int position) { return position; }
}
