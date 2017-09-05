package es.esy.silverwolf.gamecard6;

import android.widget.Button;

public class AnimalCard {
    public int x;
    public int y;
    public Button button;

    public AnimalCard(Button button, int x, int y) {
        this.x = x;
        this.y = y;
        this.button = button;
    }
}
