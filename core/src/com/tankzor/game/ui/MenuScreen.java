package com.tankzor.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tankzor.game.common_value.Dimension;
import com.tankzor.game.common_value.GameImages;
import com.tankzor.game.main.Tankzor;
import com.tankzor.game.ui.workshop.WorkshopScreen;

/**
 * Created by Admin on 12/27/2016.
 */
public class MenuScreen extends BaseScreen {
    // Constants
    private final String BUTTON_LABEL_CAMPAIGN = "Campaign";
    private final String BUTTON_LABEL_ONLINE_MATCH = "Online Match";
    public static final String BUTTON_LABEL_WORKSHOP = "Workshop";
    private final String BUTTON_LABEL_SETTINGS = "Settings";

    private Stage screenStage;
    private TextButton campaignButton;
    private TextButton onlineMatchButton;
    private TextButton workshopButton;
    private TextButton settingButton;
    private TankBackground tankBackground;

    public MenuScreen(Tankzor parent, Viewport viewport, SpriteBatch gameBatch, InputMultiplexer gameInputMultiplexer) {
        super(parent, viewport, gameBatch, gameInputMultiplexer);
    }

    @Override
    protected void initViews() {
        final Skin skin = GameImages.getInstance().getUiSkin();

        tankBackground = new TankBackground();

        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.down = skin.getDrawable(GameImages.KEY_BUTTON_BACKGROUND);
        buttonStyle.down.setMinWidth(Dimension.buttonWidth);
        buttonStyle.down.setMinHeight(Dimension.buttonHeight);
        buttonStyle.font = GameImages.getInstance().getGameFont();
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.disabledFontColor = Color.DARK_GRAY;
        buttonStyle.overFontColor = Color.LIGHT_GRAY;

        campaignButton = createStylizedTextButton(BUTTON_LABEL_CAMPAIGN, buttonStyle);
        campaignButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                onButtonTouchUpClicked(BUTTON_LABEL_CAMPAIGN);
            }
        });

        onlineMatchButton = createStylizedTextButton(BUTTON_LABEL_ONLINE_MATCH, buttonStyle);
        onlineMatchButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                onButtonTouchUpClicked(BUTTON_LABEL_ONLINE_MATCH);
            }
        });

        workshopButton = createStylizedTextButton(BUTTON_LABEL_WORKSHOP, buttonStyle);
        workshopButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
               onButtonTouchUpClicked(BUTTON_LABEL_WORKSHOP);
            }
        });

        settingButton = createStylizedTextButton(BUTTON_LABEL_SETTINGS, buttonStyle);
        settingButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                onButtonTouchUpClicked(BUTTON_LABEL_SETTINGS);
            }
        });
    }

    private TextButton createStylizedTextButton(String buttonName,
                                                TextButton.TextButtonStyle buttonStyle) {
        final TextButton button = new TextButton(buttonName, buttonStyle);
        button.setSize(Dimension.buttonWidth, Dimension.buttonHeight);
        button.getLabel().setFontScale(Dimension.normalFontScale);
        return button;
    }

    private void onButtonTouchUpClicked(String buttonId) {
        final Tankzor parent = getParent();

        switch (buttonId) {
            case BUTTON_LABEL_CAMPAIGN: {
                parent.setScreen(parent.getListMissionScreen());
                break;
            }
            case BUTTON_LABEL_ONLINE_MATCH: {
                final ListRoomScreen listRoomScreen = parent.getListRoomScreen();
                try {
                    listRoomScreen.initAppWarp();
                    listRoomScreen.showEnterNameDialog();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                parent.setScreen(listRoomScreen);
                break;
            }
            case BUTTON_LABEL_WORKSHOP: {
                final WorkshopScreen workshopScreen = parent.getWorkshopScreen();
                workshopScreen.setPreviousScreen(MenuScreen.this);
                parent.setScreen(workshopScreen);
                break;
            }
            case BUTTON_LABEL_SETTINGS: {
                final SettingsScreen settingsScreen = parent.getSettingsScreen();
                settingsScreen.setPreviousScreen(MenuScreen.this);
                parent.setScreen(parent.getSettingsScreen());
                break;
            }
        }
    }

    @Override
    protected void addViews() {
        screenStage = new Stage(getViewport(), getBatch());
        screenStage.addActor(tankBackground);

        final int widthScreen = Gdx.graphics.getWidth();
        final int heightScreen = Gdx.graphics.getHeight();

        final Table table = new Table();
        table.add(campaignButton).padBottom(Dimension.buttonSpace).align(Align.center).row();
        table.add(onlineMatchButton).padBottom(Dimension.buttonSpace).align(Align.center).row();
        table.add(workshopButton).padBottom(Dimension.buttonSpace).align(Align.center).row();
        table.add(settingButton).padBottom(Dimension.buttonSpace).align(Align.center).row();
        table.setBounds(0, 0, widthScreen, heightScreen);

        screenStage.addActor(table);
    }

    @Override
    public void show() {
        super.show();
        gameMultiplexer.addProcessor(screenStage);
    }

    @Override
    public void hide() {
        gameMultiplexer.removeProcessor(screenStage);
    }

    @Override
    public void render(float delta) {
        screenStage.act();
        screenStage.draw();
    }

    @Override
    public void dispose() {
        screenStage.dispose();
    }
}
