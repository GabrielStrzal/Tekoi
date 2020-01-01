package com.tekoi.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.strzal.gdxUtilLib.BasicGame;
import com.tekoi.game.TekoiGame;
import com.tekoi.game.config.GameConfig;
import com.tekoi.game.constants.ImagesPaths;
import com.tekoi.game.screens.GameScreen;
import com.tekoi.game.screens.GameState;

public class DialogDisplayController implements Disposable {

    private BasicGame game;
    private GameScreen gameScreen;
    private AssetManager assetManager;


    //text
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;

    //labels and text
    private TypingLabel label;


    //TODO the constructor can receive the current level and load all the correct texts for it.
    public DialogDisplayController(BasicGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.assetManager = game.getAssetManager();

        //text
        atlas = new TextureAtlas("skins/default/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/default/uiskin.json"), atlas);

        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, game.batch);


        initText();


    }

    //display box
    //display text

    private void initText(){
        //text

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();


        // Create some text with tokens
        String text = "{COLOR=GREEN}Hello,{WAIT} world! {WAIT} "
                + "{COLOR=ORANGE}{SLOWER} Did you know orange is my favorite color?" +
                "{JUMP}Test this is not{ENDJUMP}";

        // Create a TypingLabel instance with your custom text
        label = new TypingLabel(text, skin);




        //Add buttons to table
        mainTable.add(label);
        mainTable.row();


        //Add table to stage
        stage.addActor(mainTable);
    }

    public void restart(){
        label.restart();
    }

    public void restart(String newText){
        label.restart(newText);
    }



    public void drawDialog(SpriteBatch batch){
        //TODO - add this box to the stage, no batch needed then
        if (gameScreen.gameState == GameState.DIALOG) {
            Texture infoTexture = assetManager.get(ImagesPaths.MENU_BACKGROUND);
            float height = infoTexture.getHeight() / TekoiGame.PPM;
            float width = infoTexture.getWidth() / TekoiGame.PPM;
            batch.draw(infoTexture,
                    ((GameConfig.SCREEN_WIDTH / TekoiGame.PPM) / 2 - width / 2), ((GameConfig.SCREEN_HEIGHT / TekoiGame.PPM) / 2 - height / 2),
                    width, height);
        }
    }
    public void render(float delta) {
        if (gameScreen.gameState == GameState.DIALOG) {
            stage.act();
            stage.draw();
        }
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
    }
}
