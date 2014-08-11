package sopa;

/**
 * David Schilling - davejs92@gmail.com
 */
public class GameFieldService {

    private GameField gameField;
    private GameEndService gameEndService;

    public GameFieldService(GameField gameField) {
        this.gameField = gameField;
        gameEndService = new GameEndService(gameField.getField().length, gameField.getField()[0].length);
    }

    public boolean solvedPuzzle() {
        return gameEndService.solvedPuzzle(gameField.getStartX(), gameField.getStartY(), gameField.getField().length, gameField.getField()[0].length, gameField.getField());
    }



    public void printBacktracking() {
        gameEndService.printBacktracking();
    }
    public void printField(){
        System.out.println("Field:");
        for(int i = 0; i < gameField.getField()[0].length; i++){
            for(int j = 0; j < gameField.getField().length; j++){
                System.out.print(gameField.getField()[j][i].getTileType() + "\t");
            }
            System.out.println();
        }
    }

    public void shiftLine(boolean horizontal, int row, int steps) {
        if(horizontal) {
            Tile line[] = new Tile[gameField.getField().length-2];
            for(int i = 0; i < gameField.getField().length - 2; i++) {
                line[i] = gameField.getField()[i+1][row+1];
            }
            for(int i = 0; i < gameField.getField().length-2; i++) {
                int newPosition = i + steps;
                newPosition = shiftToPositive(newPosition,gameField.getField().length-2);
                newPosition = newPosition%(gameField.getField().length-2);
                gameField.getField()[newPosition+1][row+1] = line[i];
            }
        } else {
            Tile line[] = new Tile[gameField.getField()[0].length-2];
            for(int i = 0; i < gameField.getField()[0].length-2; i++) {
                line[i] = gameField.getField()[row+1][i+1];
            }
            for(int i = 0; i < gameField.getField()[0].length-2; i++) {
                int newPosition = (i+steps);
                newPosition = shiftToPositive(newPosition,gameField.getField()[0].length-2);
                newPosition = newPosition%(gameField.getField()[0].length-2);
                gameField.getField()[row+1][newPosition+1] = line[i];
            }
        }
    }

    public void setField(Tile[][] field) {
        gameField.setField(field);
    }

    public Tile[][] getField() {
        return gameField.getField();
    }

    private int shiftToPositive(int number, int steps) {
        int shifted = number;
        while (shifted < 0) {
            shifted = shifted + steps;
        }
        return shifted;
    }

}
