public class ConfusePowerUp extends  PowerUp {
    public ConfusePowerUp(Model model, Tile t) {
        super(model, t);
        spriteNum = 9;
    }

    @Override
    public void use() {
        super.use();
        Ghost.confused = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Util.sleepMs(10000);
                Ghost.confused = false;
            }
        }).start();
    }
}

