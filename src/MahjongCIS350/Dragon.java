package MahjongCIS350;

public class Dragon extends Tile{

    private String Color;

    public Dragon(String color) {
        super("Dragon");
        Color = color;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }
}
