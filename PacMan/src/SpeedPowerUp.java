public class SpeedPowerUp extends PowerUp {
    public SpeedPowerUp(Model model, Tile t) {
        super(model, t);
        spriteNum = 8;
    }

    @Override
    public void use() {
        super.use();
        new Thread(() -> {
            model.player.intervalMs = (int) (model.player.intervalMs * 0.5);
            Util.sleepMs(10000);
            model.player.intervalMs = model.player.intervalMs * 2;
        }).start();

        System.out.println("Zyskano tymczasowe przyśpieszenie");
    }
}