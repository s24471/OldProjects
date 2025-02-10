public class FramePowerUp extends  PowerUp {
    MovingFrame movingFramev;
    public FramePowerUp(Model model, Tile t) {
        super(model, t);
        spriteNum = 7;
    }

    @Override
    public void use() {
        super.use();
        movingFramev = new MovingFrame();
    }
}

