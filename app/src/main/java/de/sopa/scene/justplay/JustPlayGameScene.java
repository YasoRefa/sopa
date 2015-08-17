package de.sopa.scene.justplay;

import de.sopa.model.game.GameServiceImpl;
import de.sopa.model.game.TimeBasedGameServiceImpl;
import de.sopa.model.justplay.JustPlayLevel;
import de.sopa.model.justplay.JustPlayLevelResult;
import de.sopa.observer.JustPlaySceneObserver;
import de.sopa.scene.game.GameScene;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.text.Text;

/**
 * David Schilling - davejs92@gmail.com
 **/
public class JustPlayGameScene extends GameScene implements JustPlaySceneObserver {

    private final JustPlayLevel justPlayLevel;
    private TimeBasedGameServiceImpl timeBasedGameService;
    private Text leftTime;
    private boolean leaveScene;


    public JustPlayGameScene(JustPlayLevel justPlayLevel) {
        super(justPlayLevel.getLevel());
        this.justPlayLevel = justPlayLevel;
        leaveScene = false;
        timeBasedGameService = new TimeBasedGameServiceImpl(justPlayLevel.getLeftTime());
        timeBasedGameService.start();
        timeBasedGameService.attach(this);
        leftTime.setText(String.valueOf(justPlayLevel.getLeftTime()));
    }

    @Override
    protected void addCustomLabels() {
        leftTime = new Text(camera.getWidth() * 0.67f, camera.getHeight() * 0.83f, resourcesManager.scoreFont, "", 6, vbom);
        attachChild(leftTime);
        Text leftTimeText= new Text(camera.getWidth() * 0.67f, camera.getHeight() * 0.81f, resourcesManager.levelFont, "Left Time", vbom);
        leftTimeText.setScaleCenter(0, 0);
        leftTimeText.setScale(0.3f);
        attachChild(leftTimeText);

    }

    @Override
    protected void addButtons() {

    }

    @Override
    protected void initializeLogic() {
        gameService = new GameServiceImpl(this.level);
        gameService.attach(this);
    }

    @Override
    public void onBackKeyPressed() {
        if(!leaveScene) {
            leaveScene = true;
            engine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
                @Override
                public void onTimePassed(TimerHandler pTimerHandler) {
                    engine.unregisterUpdateHandler(pTimerHandler);
                    storyService.loadMenuSceneFromJustPlayGameScene();
                }
            }));
        }


    }

    @Override
    public void onSolvedGame() {
        if(!leaveScene) {
            leaveScene = true;
            engine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
                @Override
                public void onTimePassed(TimerHandler pTimerHandler) {
                    engine.unregisterUpdateHandler(pTimerHandler);
                    storyService.loadJustPlayScoreSceneFromJustPlayScene(new JustPlayLevelResult(timeBasedGameService.getRemainingTime(), gameService.getLevel().getMovesCount()));
                }
            }));
        }

    }
    @Override
    public void onLostGame() {
        if(!leaveScene) {
            leaveScene = true;
            engine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() {
                @Override
                public void onTimePassed(TimerHandler pTimerHandler) {
                    engine.unregisterUpdateHandler(pTimerHandler);
                    storyService.loadJustPlayScoreSceneFromJustPlayScene(new JustPlayLevelResult(-1, gameService.getLevel().getMovesCount()));
                }
            }));
        }
    }

    @Override
    public void updateJustPlayScene() {
        engine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                leftTime.setText(String.valueOf(timeBasedGameService.getRemainingTime()));
            }
        }));
        if(timeBasedGameService.getRemainingTime() == 0 && !gameService.solvedPuzzle()){
            onLostGame();
        }
    }
}