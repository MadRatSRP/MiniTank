package com.tankzor.game.ui.workshop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import com.tankzor.game.common_value.Dimension;

public class WorkshopMenu extends VerticalGroup {
    Array<ItemButton> listItemButtons;

    public WorkshopMenu(int iconId, String title) {
        fill();

        space(Dimension.buttonSpace / 2);

        final MenuTitle menuTitle = new MenuTitle(iconId, title,
                Gdx.graphics.getWidth(), Dimension.screenTitleHeight);
        addActor(menuTitle);

        listItemButtons = new Array<>();
    }

    public void addItemButton(ItemButton itemButton) {
        listItemButtons.add(itemButton);
        addActor(itemButton);
    }

    public void updateAllButton(int value) {
        for (int i = 0; i < listItemButtons.size; i++) {
            listItemButtons.get(i).update(value);
        }
    }

    public int size() {
        return listItemButtons.size;
    }
}
