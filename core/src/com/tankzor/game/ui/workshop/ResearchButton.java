package com.tankzor.game.ui.workshop;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.game_resources.GameImages;
import com.tankzor.game.game_resources.GameSounds;
import com.tankzor.game.common_value.PlayerProfile;
import com.tankzor.game.common_value.research_model.ResearchModel;
import com.tankzor.game.main.Tankzor;
import com.tankzor.game.ui.DocumentScreen;

public class ResearchButton extends ItemButton {
    Label valueLabel, levelLabel;
    Image starImage;
    WorkshopScreen workshopScreen;

    public ResearchButton(WorkshopScreen workshopScreen, int id, float width,
                          float height, TextButtonStyle style) {
        super(id, width, height, style);

        this.workshopScreen = workshopScreen;

        Label.LabelStyle labelStyle = GameImages.getInstance().getLabelStyle();

        valueLabel = new Label("", labelStyle);
        valueLabel.setAlignment(Align.right);
        valueLabel.setFontScale(Dimension.normalFontScale);

        levelLabel = new Label("", labelStyle);
        levelLabel.setAlignment(Align.center);
        levelLabel.setFontScale(Dimension.normalFontScale);

        starImage = new Image(GameImages.getInstance().getIcon(31));
        Drawable drawable = starImage.getDrawable();
        drawable.setMinWidth(Dimension.starIconSize);
        drawable.setMinHeight(Dimension.starIconSize);

        add(levelLabel).align(Align.right).width(100);
        add(valueLabel).align(Align.right).width(180);
        add(starImage).align(Align.right).padRight(Dimension.starIconSize);
        add(addButton).padRight(Dimension.buttonSpace / 2);

        setText(PlayerProfile.getInstance().getResearchModel(id).name);
    }

    @Override
    public void update(int star) {
        ResearchModel researchModel = PlayerProfile.getInstance().getResearchModel(id);
        levelLabel.setText(researchModel.currentLevel + "");
        if (researchModel.isMaximumLevel()) {
            setDisabled(true);
            valueLabel.setText("Max");
            starImage.setVisible(false);
            addButton.setVisible(false);
            return;
        }
        setDisabled(false);
        int starNeeded = researchModel.getStarOfCurrentLevel();
        valueLabel.setText(starNeeded + "");
        starImage.setVisible(true);
        addButton.setVisible(star >= starNeeded);
    }

    @Override
    void onPress() {
        Tankzor parent = workshopScreen.getParent();
        DocumentScreen documentScreen = parent.getDocumentScreen();
        documentScreen.setPreviousScreen(workshopScreen);
        ResearchModel researchModel = PlayerProfile.getInstance().getResearchModel(id);
        documentScreen.setTitle(researchModel.name);
        documentScreen.setContent(researchModel.description);
        parent.setScreen(documentScreen);
    }

    @Override
    void onAddButtonPress() {
        GameSounds.getInstance().playSFX(GameSounds.RESEARCH_SFX_ID);
        PlayerProfile playerProfile = PlayerProfile.getInstance();
        ResearchModel researchModel = PlayerProfile.getInstance().getResearchModel(id);
        playerProfile.addStar(-researchModel.getStarOfCurrentLevel());
        researchModel.levelUp();
        workshopScreen.updateAllWorkshop();
    }
}
