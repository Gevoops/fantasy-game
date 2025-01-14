package components;

import scenes.WorldEditorScene;

public class Player extends Component{

    @Override
    public void start(){
        WorldEditorScene.getInstance().setPlayer(this.gameObject);
    }
}
